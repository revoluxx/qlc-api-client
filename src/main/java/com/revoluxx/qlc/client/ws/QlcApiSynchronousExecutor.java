package com.revoluxx.qlc.client.ws;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;

public class QlcApiSynchronousExecutor implements MessageHandler.Whole<String> {

	private final ReentrantLock executionLock = new ReentrantLock();

	private RemoteEndpoint.Basic remote;

	private Map<String, SyncSemaphoreBuffer> awaitingCommandResponses = new ConcurrentHashMap<String, SyncSemaphoreBuffer>();

	public QlcApiSynchronousExecutor() {
	}

	public String callApi(final String command) {
		String result = null;
		try {
			final SyncSemaphoreBuffer semaphore = new SyncSemaphoreBuffer();
			awaitingCommandResponses.put(command, semaphore);
			executionLock.lock();
			try {
				remote.sendText(command);
			} finally {
				if (executionLock.isHeldByCurrentThread()) {
					executionLock.unlock();
				}
			}
			semaphore.getWriterBarrier().countDown();
			semaphore.getReadBarrier().await(3L, TimeUnit.SECONDS);
			result = semaphore.getData();
			awaitingCommandResponses.remove(command);
		} catch (InterruptedException | IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public void onMessage(String message) {
		if (message.startsWith("QLC+API")) {
			String[] splitted = message.split("\\|");
			if (splitted.length >= 2) {
				SyncSemaphoreBuffer semaphore = awaitingCommandResponses.get(splitted[0] + '|' + splitted[1]);
				if (semaphore != null) {
					try {
						semaphore.getWriterBarrier().await(3L, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					semaphore.setData(message);
					semaphore.getWriterBarrier().countDown();
				}
			}
		}
	}

	public void setRemote(RemoteEndpoint.Basic remote) {
		this.remote = remote;
	}

	public ReentrantLock getExecutionLock() {
		return executionLock;
	}

}
