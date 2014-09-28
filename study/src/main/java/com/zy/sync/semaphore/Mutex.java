package com.zy.sync.semaphore;

import java.util.concurrent.Semaphore;

public class Mutex {
	private Semaphore s = new Semaphore(1);

	public void acquire() throws InterruptedException {
		s.acquire();
	}

	public void release() {
		s.release();
	}

	public boolean attempt(int ms) throws InterruptedException {
		return s.tryAcquire(ms);
	}
}
