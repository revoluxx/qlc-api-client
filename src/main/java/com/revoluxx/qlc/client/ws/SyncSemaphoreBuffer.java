package com.revoluxx.qlc.client.ws;

import java.util.concurrent.CountDownLatch;

public class SyncSemaphoreBuffer {

	private CountDownLatch readBarrier = new CountDownLatch(1);
	private CountDownLatch writerBarrier = new CountDownLatch(1);
	private String data;

	public CountDownLatch getReadBarrier() {
		return readBarrier;
	}
	public CountDownLatch getWriterBarrier() {
		return writerBarrier;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
