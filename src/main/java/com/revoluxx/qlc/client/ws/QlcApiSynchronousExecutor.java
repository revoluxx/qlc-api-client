package com.revoluxx.qlc.client.ws;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;

import com.revoluxx.qlc.client.QlcApiQuery;

public class QlcApiSynchronousExecutor implements MessageHandler.Whole<String> {

	private final ReentrantLock executionLock = new ReentrantLock();

	private RemoteEndpoint.Basic remote;

	private Map<String, SyncSemaphoreBuffer> awaitingCommandResponses = new ConcurrentHashMap<String, SyncSemaphoreBuffer>();

	public QlcApiSynchronousExecutor() {
	}

	public String callApi(final QlcApiQuery<?> command) {
		String result = null;
		try {
			final SyncSemaphoreBuffer semaphore = new SyncSemaphoreBuffer();
			executionLock.lock();
			try {
				awaitingCommandResponses.put(command.getResponseHeader(), semaphore);
				remote.sendText(command.getCommand());
			} finally {
				if (executionLock.isHeldByCurrentThread()) {
					executionLock.unlock();
				}
			}
			semaphore.getWriterBarrier().countDown();
			semaphore.getReadBarrier().await(3L, TimeUnit.SECONDS);
			result = semaphore.getData();
			awaitingCommandResponses.remove(command.getResponseHeader());
		} catch (InterruptedException | IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public void callApiWithoutReply(final QlcApiQuery<?> command) throws IOException {
		remote.sendText(command.getCommand());
	}

	@Override
	public void onMessage(String message) {
		final SyncSemaphoreBuffer semaphore = retrieveAwaitingSemaphore(message);
		if (semaphore != null) {
			try {
				semaphore.getWriterBarrier().await(3L, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			semaphore.setData(message);
			semaphore.getReadBarrier().countDown();
		}
	}

	private SyncSemaphoreBuffer retrieveAwaitingSemaphore(final String message) {
		for (final Entry<String, SyncSemaphoreBuffer> entry : awaitingCommandResponses.entrySet()) {
			if (message.startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void setRemote(RemoteEndpoint.Basic remote) {
		this.remote = remote;
	}

	public ReentrantLock getExecutionLock() {
		return executionLock;
	}

}
