package com.revoluxx.qlc.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.glassfish.tyrus.client.ClientManager;

import com.revoluxx.qlc.client.data.parser.ResponseParser;
import com.revoluxx.qlc.client.enums.CommandCategory;
import com.revoluxx.qlc.client.exception.QlcApiClientException;
import com.revoluxx.qlc.client.exception.QlcApiNoResponseException;
import com.revoluxx.qlc.client.ws.QlcApiClientAuthConfigurator;
import com.revoluxx.qlc.client.ws.QlcApiSynchronousExecutor;

/**
 * Main class of the QLC+ WebSocket API client, providing calling facilities.
 * This class is thread-safe, only one instance needs to be created per application/QLC+ instance.
 * To start with a new instance of this QlcApiClient, use the embedded Builder class, ie for default configuration:<br>
 * QlcApiClient.builder().build();<br>
 * @see Builder
 */
public class QlcApiClient extends Endpoint implements Closeable {

	private final String host;
	private final int port;
	private final String wsPath;
	private final String authUserName;
	private final String authPassword;
	private final boolean autoReconnect;
	private final int autoReconnectMaxAttempts;
	
	private final ClientEndpointConfig clientEndpointConfig;
	private final WebSocketContainer wsClient;
	private final QlcApiSynchronousExecutor wsExecutor;
	
	private final Map<String, Lock> locksByCommandCall;

	private Session wsSession;

	/**
	 * Private constructor: use the Builder to create an instance of the QlcApiClient
	 * 
	 * @param builder
	 */
	private QlcApiClient(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.wsPath = builder.wsPath;
		this.autoReconnect = builder.autoReconnect;
		this.autoReconnectMaxAttempts = builder.autoReconnectMaxAttempts;
		this.authUserName = builder.authUserName;
		this.authPassword = builder.authPassword;
		
		if (builder.clientEndpointConfig == null) {
			if (authUserName != null || authPassword != null) {
				final Configurator authConfigurator = new QlcApiClientAuthConfigurator(authUserName, authPassword);
				this.clientEndpointConfig = ClientEndpointConfig.Builder.create().configurator(authConfigurator).build();
			} else {
				this.clientEndpointConfig = ClientEndpointConfig.Builder.create().build();
			}
		} else {
			this.clientEndpointConfig = builder.clientEndpointConfig;
		}
		
		if (builder.wsClient == null) {
			this.wsClient = ClientManager.createClient();
		} else {
			this.wsClient = builder.wsClient;
		}
		
		this.wsExecutor = new QlcApiSynchronousExecutor();
		this.locksByCommandCall = new ConcurrentHashMap<String, Lock>();
	}

	/**
	 * Open or reopen the WebSocket connection to QLC+, try to create a new WS session.<br>
	 * To be done once prior to execute any API query/command ! 
	 * 
	 * @throws DeploymentException internal error while initializing websocket client
	 * @throws IOException communication error while establishing connection (QLC+ websocket server unavailable/not started ?)
	 * @throws URISyntaxException host/port/path provided are invalid url for the websocket
	 * @throws QlcApiClientException Unexpected error while opening session
	 */
	public void connect() throws DeploymentException, IOException, URISyntaxException, QlcApiClientException {
		if (wsSession == null || !wsSession.isOpen()) {
			final URI wsUri = getWsUri();
			wsSession = wsClient.connectToServer(this, clientEndpointConfig, wsUri);
			if (!wsSession.isOpen()) {
				throw new QlcApiClientException("WS session has not been opened");
			}
		}
	}

	/**
	 * Execute/send the provided query/command to QLC+ API, with response/result expected.<br>
	 * This execution is <i>synchronous by responseHeader</i>: each query will wait for the completion of another previous one expecting the same responseHeader. 
	 * 
	 * @param <T> - the expected type of the command result (inferred from query definition)
	 * @param query - the command to execute: use QlcApiQuery to create one
	 * @return the API call response/result
	 * @throws IOException
	 * @throws QlcApiClientException
	 * @throws QlcApiNoResponseException
	 * @see QlcApiQuery
	 */
	public <T> T executeQuery(final QlcApiQuery<? extends ResponseParser<T>> query) throws IOException, QlcApiClientException, QlcApiNoResponseException {
		if (query.getResponseHeader() == null) {
			throw new QlcApiClientException("This query cannot be executed in response mode, call executeQueryWithoutResult instead");
		}
		locksByCommandCall.putIfAbsent(query.getResponseHeader(), new ReentrantLock());
		Lock commandCallLock = locksByCommandCall.get(query.getResponseHeader());
		commandCallLock.lock();
		String wsResult = null;
		try {
			wsResult = wsExecutor.callApi(query);
		} finally {
			commandCallLock.unlock();
		}
		return query.getResponseParser().parseResponse(extractResponseBody(wsResult, query.getResponseHeader()), query.getResponseHeader());
	}

	/**
	 * Execute/send the provided one way query/command to QLC+ API, without waiting for any response/result.
	 * This execution is asynchrounous and non-blocking.
	 * 
	 * @param query - the command to execute: use QlcApiQuery to create one
	 * @throws IOException
	 * @throws QlcApiClientException
	 */
	public void executeQueryWithoutResponse(final QlcApiQuery<? extends ResponseParser<?>> query) throws IOException, QlcApiClientException {
		if (query.getResponseHeader() != null) {
			throw new QlcApiClientException("This query cannot be executed in no-response mode, call executeQuery instead");
		}
		wsExecutor.callApiWithoutReply(query);
	}

	protected String extractResponseBody(final String response, final String responseHeader) {
		String responseBody = response.substring(responseHeader.length());
		if (responseBody.isEmpty()) {
			responseBody = null;
		} else if (responseBody.charAt(0) == CommandCategory.COMMAND_SEPARATOR) {
			responseBody = responseBody.substring(1);
		}
		return responseBody;
	}

	/**
	 * Close the opened WebSocket session.
	 */
	public void close() throws IOException {
		if (wsSession != null && wsSession.isOpen()) {
			wsSession.close();
		}
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		wsExecutor.setRemote(session.getBasicRemote());
		session.addMessageHandler(wsExecutor);
	}

	@Override
	public void onClose(Session session, CloseReason closeReason) {
		// In case of abnormal session closure and when autoReconnect is active, block any current query and try to reconnect
		if (autoReconnect && !CloseReason.CloseCodes.NORMAL_CLOSURE.equals(closeReason.getCloseCode())) {
			try {
				wsExecutor.getExecutionLock().tryLock(5L, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				System.out.println("Abnormal session closure: " + closeReason.getReasonPhrase());
				boolean reconnected = false;
				for(int attempts = 0; !reconnected && attempts < autoReconnectMaxAttempts; attempts++) {
					System.out.println(attempts + " reconnection attempt");
					try {
						connect();
						reconnected = true;
					} catch (DeploymentException | IOException | URISyntaxException | QlcApiClientException ex) {
						System.out.println(ex.getMessage());
						try {
							Thread.sleep(3000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (reconnected) {
					System.out.println("Reconnection attempt successful !");
				} else {
					System.out.println("Reconnection attempts unsuccessful");
				}
			} finally {
				if (wsExecutor.getExecutionLock().isHeldByCurrentThread()) {
					wsExecutor.getExecutionLock().unlock();
				}
			}
		}
	}

	public URI getWsUri() throws URISyntaxException {
		final StringBuilder sbWsUrl = new StringBuilder("ws://");
		sbWsUrl.append(host).append(":").append(port);
		sbWsUrl.append(wsPath);
		return new URI(sbWsUrl.toString());
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String host = "127.0.0.1";
		private int port = 9999;
		private String wsPath = "/qlcplusWS";
		private boolean autoReconnect = true;
		private int autoReconnectMaxAttempts = 5;
		
		private ClientEndpointConfig clientEndpointConfig;
		private WebSocketContainer wsClient;
		
		private String authUserName;
		private String authPassword;

		public Builder setHost(String host) {
			this.host = host;
			return this;
		}
		
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}
		
		public Builder setWsPath(String wsPath) {
			this.wsPath = wsPath;
			return this;
		}

		public Builder setAutoReconnect(boolean autoReconnect) {
			this.autoReconnect = autoReconnect;
			return this;
		}

		public Builder setAutoReconnectMaxAttempts(int autoReconnectMaxAttempts) {
			this.autoReconnectMaxAttempts = autoReconnectMaxAttempts;
			return this;
		}

		public Builder setClientEndpointConfig(ClientEndpointConfig clientEndpointConfig) {
			this.clientEndpointConfig = clientEndpointConfig;
			return this;
		}

		public Builder setWsClient(WebSocketContainer wsClient) {
			this.wsClient = wsClient;
			return this;
		}

		public Builder setAuthUserName(String authUserName) {
			this.authUserName = authUserName;
			return this;
		}
		
		public Builder setAuthPassword(String authPassword) {
			this.authPassword = authPassword;
			return this;
		}
		
		public QlcApiClient build() {
			return new QlcApiClient(this);
		}
	
	}

}
