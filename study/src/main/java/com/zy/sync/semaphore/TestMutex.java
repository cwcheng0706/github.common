package com.zy.sync.semaphore;

public class TestMutex {
	public static void main(String[] args) throws InterruptedException {
		Mutex mutex = new Mutex();
		mutex.acquire();
		mutex.release();
		mutex.release();
		new MyThread(mutex).start();
		new MyThread(mutex).start();
	}

}

class MyThread extends Thread {
	private Mutex mutex;

	public MyThread(Mutex mutex) {
		this.mutex = mutex;
	}

	public void run() {
		try {
			mutex.acquire();
		} catch (InterruptedException e1) {
			throw new RuntimeException(e1);
		}
		for (int i = 0; i < 10; i++) {
			System.out.print(i);
			if (i % 3 == 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		mutex.release();
	}
}
