/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年7月29日 下午11:05:01
 */
package com.zy.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年7月29日 下午11:05:01
 */
public class ConcurrentHashMapTest {

	public static ConcurrentHashMap<String, String> lockMap = new ConcurrentHashMap<String, String>();

	public static void main(String[] args) {

		// System.out.println(lockMap.putIfAbsent("1", "1"));

		int corePoolSize = 20;
		int maximumPoolSize = 40;
		long keepAliveTime = 60;
		TimeUnit unit = TimeUnit.SECONDS;
		LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

		final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				workQueue);

		new Thread() {
			public void run() {
				for (int i = 0; i < 4; i++) {
					ConcurrentHashMapRunner runner = new ConcurrentHashMapRunner("Thread1_" + String.valueOf(i),
							String.valueOf(i));
					executor.execute(runner);
				}
			}
		}.start();

		new Thread() {
			public void run() {
				for (int i = 0; i < 4; i++) {
					ConcurrentHashMapRunner runner = new ConcurrentHashMapRunner("Thread2_" + String.valueOf(i),
							String.valueOf(i));
					executor.execute(runner);
				}
			}
		}.start();

	}
}

class ConcurrentHashMapRunner implements Runnable {
	private static Log logger = LogFactory.getLog(ConcurrentHashMapRunner.class);

	private String key;
	private String name;

	public ConcurrentHashMapRunner(String name, String key) {
		this.key = key;
		this.name = name;
	}

	public void run() {
//		logger.debug(name + " prepare===" + key);
		if (null == ConcurrentHashMapTest.lockMap.putIfAbsent(key, String.valueOf(key))) {
			logger.debug(name + " doWork===" + key);
		}

//		if (!ConcurrentHashMapTest.lockMap.containsKey(key)) {
//			ConcurrentHashMapTest.lockMap.put(key, key);
//			logger.debug(name + " doWork===" + key);
//		}
	}

}
