package com.zy.thread.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 非阻塞队列，允许线程操作队列
 * 
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年9月12日 下午5:21:56
 */
public class ConcurrentLinkedQueueTest {
	
	private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
	
	// 线程个数
	private static int count = 2; 
	
	// CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
	private static CountDownLatch latch = new CountDownLatch(count);

	public static void main(String[] args) throws InterruptedException {
		
		long timeStart = System.currentTimeMillis();

		// 线程池中的两个线程同时消费捅有100000个任务的队列中的任务
		ExecutorService executor = Executors.newFixedThreadPool(4);

		// 生产任务放置队列
		ConcurrentLinkedQueueTest.offer();

		// 线程池中的两线程 进行消费
		for (int i = 0; i < count; i++) {
			executor.submit(new Poll());
		}

		// 使得主线程(main)阻塞直到latch.countDown()为零才继续执行
		latch.await();
		System.out.println("cost time " + (System.currentTimeMillis() - timeStart) + "ms");
		executor.shutdown();
	}

	/**
	 * 生产
	 */
	public static void offer() {
		for (int i = 0; i < 100000; i++) {
			queue.offer(i);
		}
	}

	/**
	 * 消费
	 * 
	 * @author 林计钦
	 * @version 1.0 2013-7-25 下午05:32:56
	 */
	static class Poll implements Runnable {
		public void run() {
			// while (queue.size()>0) {
			while (!queue.isEmpty()) {
				System.out.println(queue.poll());
			}
			latch.countDown();
		}
	}
}
