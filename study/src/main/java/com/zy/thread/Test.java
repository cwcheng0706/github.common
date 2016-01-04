/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年1月4日 下午8:24:30
 */
package com.zy.thread;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年1月4日 下午8:24:30
 */
public class Test {

	private Counter counter;
	// 为了统一启动线程，这样好计算多线程并发运行的时间
	private CyclicBarrier barrier;
	private int threadNum;// 线程数
	private int loopNum;// 每个线程的循环次数
	private String testName;

	public Test(Counter counter, int threadNum, int loopNum, String testName) {
		this.counter = counter;
		barrier = new CyclicBarrier(threadNum + 1); // 关卡计数=线程数 +1
		this.threadNum = threadNum;
		this.loopNum = loopNum;
		this.testName = testName;
	}

	public static void main(String args[]) throws Exception {
		int threadNum = 2000;
		int loopNum = 1000;
		new Test(new SynchronizedCounter(), threadNum, loopNum, "内部锁").test();
		new Test(new ReentrantUnfairCounterLockCounter(), threadNum, loopNum, "不公平重入锁").test();
		new Test(new ReentrantFairLockCounter(), threadNum, loopNum, "公平重入锁").test();
	}

	public void test() throws Exception {
		try {
			for (int i = 0; i < threadNum; i++) {
				new TestThread(counter, loopNum).start();
			}
			long start = System.currentTimeMillis();
			barrier.await(); // 等待所有任务线程创建,然后通过关卡，统一执行 所有线程
			barrier.await(); // 等待所有任务计算完成
			long end = System.currentTimeMillis();
			System.out.println(this.testName + " count value:" + counter.getValue());
			System.out.println(this.testName + " 花费时间:" + (end - start) + "毫秒");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	class TestThread extends Thread {
		int loopNum = 100;
		private Counter counter;

		public TestThread(final Counter counter, int loopNum) {
			this.counter = counter;
			this.loopNum = loopNum;
		}

		public void run() {
			try {
				barrier.await();// 等待所有的线程开始
				for (int i = 0; i < this.loopNum; i++)
					counter.increment();
				barrier.await();// 等待所有的线程完成
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

class ReentrantFairLockCounter implements Counter {
	private volatile long count = 0;
	private Lock lock;

	public ReentrantFairLockCounter() {
		// true就是公平锁
		lock = new ReentrantLock(true);
	}

	public long getValue() {
		return count;
	}

	public void increment() {
		lock.lock();
		try {
			count++;
		} finally {
			lock.unlock();
		}
	}
}

class ReentrantUnfairCounterLockCounter implements Counter {
	private volatile long count = 0;
	private Lock lock;

	public ReentrantUnfairCounterLockCounter() {
		// 使用非公平锁，true就是公平锁
		lock = new ReentrantLock(false);
	}

	public long getValue() {
		return count;
	}

	public void increment() {
		lock.lock();
		try {
			count++;
		} finally {
			lock.unlock();
		}
	}
}

class SynchronizedCounter implements Counter {
	private long count = 0;

	public long getValue() {
		return count;
	}

	public synchronized void increment() {
		count++;
	}
}

interface Counter {
	public long getValue();

	public void increment();
}
