/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年3月11日 下午8:30:43
 */
package com.zy.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Project: study
 * @Author zy
 * @Company:
 * @Create Time: 2016年3月11日 下午8:30:43
 */
public class MyLock {
	public static void main(String[] args) {

	}

}

class MyLock1 {
	private AtomicBoolean locked = new AtomicBoolean(false);

	public boolean lock() {
		return locked.compareAndSet(false, true);
	}

}

class MyLock2 {
	private boolean locked = false;

	public synchronized boolean lock() {
		if (!locked) {
			locked = true;
			return true;
		}
		return false;
	}
}
