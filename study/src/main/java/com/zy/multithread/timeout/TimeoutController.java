package com.zy.multithread.timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TimeoutController {

	public static void main(String[] args) throws Exception{
		ExecutorService executos = Executors.newFixedThreadPool(100);
		TimeoutRunner runner = new TimeoutRunner(3000);
		
		executos.execute(runner);
		
		TimeoutController.execute(runner, 3000);
	}
	

	public static void execute(Thread task, long timeout) throws TimeoutException {
		task.start();
		try {
			task.join(timeout);
		} catch (InterruptedException e) {
			/* if somebody interrupts us he knows what he is doing */
		}
		if (task.isAlive()) {
			task.interrupt();
			throw new TimeoutException();
		}
	}

	public static void execute(Runnable task, long timeout) throws TimeoutException {
		Thread t = new Thread(task, "Timeout guard");
		t.setDaemon(true);
		execute(t, timeout);
	}

	public static class TimeoutException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** Create an instance */
		public TimeoutException() {
		}
	}
}

class TimeoutRunner implements Runnable {

	private long timeout;
	
	public TimeoutRunner(long timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
