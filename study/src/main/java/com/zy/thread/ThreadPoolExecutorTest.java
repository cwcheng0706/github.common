package com.zy.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

	static ThreadPoolExecutor executor = null;
	static BlockingQueue<Runnable> workQueue = null;

	public static void main(String[] args) throws Exception {
		test1();

		moitor();

	}

	public static void test1() {
		int corePoolSize = 4;
		int maximumPoolSize = 100;
		long keepAliveTime = 60;
		TimeUnit unit = TimeUnit.SECONDS;
//		workQueue = new ArrayBlockingQueue<Runnable>(40);
		workQueue = new LinkedBlockingQueue<Runnable>();
		

		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

		for (int i = 0; i < 200; i++) {
			System.out.println("execute " + "Thread_" + i);
			executor.submit(new RunnableWorker<Object>("Thread_" + i));
		}
		
		System.out.println("");
	}

	public static void moitor() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					int activeCount = executor.getActiveCount();
					int poolSize = executor.getPoolSize();
					long taskCount = executor.getTaskCount();
					long comCount = executor.getCompletedTaskCount();

					System.out.println("activeCount【" + activeCount + "】" + " poolSize【" + poolSize + "】" + "  comCount【" + comCount + "】" + "  taskCount【" + taskCount + "】");
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}, "monitor-thread");

		t.start();
	}

}

class RunnableWorker<T> implements Runnable {

	private String name;

	public RunnableWorker(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println(name + "【" + Thread.currentThread().getName() + "】 work start..");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name + "【" + Thread.currentThread().getName() + "】 work end..");
	}

}
