package com.zy.multithread.timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池执行任务。但这里面加了超时控制
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2015年2月6日 下午2:10:15
 */
public class ThreadExecutoTimeout {

	public static void main(String[] args) throws Exception{
		ExecutorService executos = Executors.newFixedThreadPool(3);
		
		while(true) {
			System.out.println("************************");
			TimeoutRunner runner = new TimeoutRunner(3000);
			executos.execute(runner);
			
			
			Thread.sleep(1000);
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
		Thread task = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		task.setDaemon(true);
		task.start();
		try {
			task.join(timeout);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (task.isAlive()) {
			task.interrupt();
			System.out.println("================");
		}
		
		
	}
	
}

class TimeoutException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Create an instance */
	public TimeoutException() {
	}
}