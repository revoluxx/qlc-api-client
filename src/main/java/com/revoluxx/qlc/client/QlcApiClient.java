package com.revoluxx.qlc.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.glassfish.tyrus.client.ClientManager;

import com.revoluxx.qlc.client.data.GetFunctionsListRecord;
import com.revoluxx.qlc.client.exception.QlcApiClientException;
import com.revoluxx.qlc.client.ws.QlcApiClientAuthConfigurator;
import com.revoluxx.qlc.client.ws.QlcApiSynchronousExecutor;

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

	private Session wsSession;

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
	}

	public void connect() throws DeploymentException, IOException, URISyntaxException, QlcApiClientException {
		if (wsSession == null || !wsSession.isOpen()) {
			final URI wsUri = getWsUri();
			wsSession = wsClient.connectToServer(this, clientEndpointConfig, wsUri);
			if (!wsSession.isOpen()) {
				throw new QlcApiClientException("WS session has not been opened");
			}
		}
	}

	public synchronized List<GetFunctionsListRecord> getFunctionsList() throws IOException, InterruptedException {
		final String command = "QLC+API|getFunctionsList";
		String wsResult = wsExecutor.callApi(command);
		final List<GetFunctionsListRecord> result = formatList(wsResult);
		return result;
	}

	private List<GetFunctionsListRecord> formatList(final String data) {
		List<GetFunctionsListRecord> result = null;
		if (data != null) {
			String[] splitedData = data.split("\\|");
			if (splitedData.length >= 4 && (splitedData.length % 2) == 0) {
				result = new ArrayList<GetFunctionsListRecord>();
				int i = 2;
				while (i < splitedData.length) {
					result.add(new GetFunctionsListRecord(splitedData[i++], splitedData[i++]));
				}
			}
		}
		return result;
	}

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
		if (autoReconnect && !CloseReason.CloseCodes.NORMAL_CLOSURE.equals(closeReason.getCloseCode())) {
			try {
				wsExecutor.getExecutionLock().tryLock(5L, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				System.out.println("Abnormal session closing: " + closeReason.getReasonPhrase());
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