/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2015年1月18日 下午1:38:02
 */
package com.zy.multithread;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project: study
 * @Author zy
 * @Company: 
 * @Create Time: 2015年1月18日 下午1:38:02
 */
public class ThreadTimeoutExecutor {

	public static void main(String[] args) throws Exception{
		
		Thread t  = new WorkTimeoutThread(3000);
		t.start();
		
		System.out.println("main end");
	}
}

class WorkTimeoutThread extends Thread{
	
	private long timeoutSecond;
	
	public WorkTimeoutThread(long timeoutSecond) {
		Thread daemon = new Thread(new DaemonThread(System.currentTimeMillis(),timeoutSecond,this));
		daemon.setDaemon(true);
		this.timeoutSecond = timeoutSecond;
		daemon.start();
		
	}

	@Override
	public void run() {
		System.out.println("work start");
		try{
			
			Thread.sleep(3400);
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("work end");
	}

	public long getTimeoutSecond() {
		return timeoutSecond;
	}

	public void setTimeoutSecond(long timeoutSecond) {
		this.timeoutSecond = timeoutSecond;
	}
}

class DaemonThread extends Thread {

	private long startTime;
	private long timeoutSecond;
	final private Thread workThread;
	
	public DaemonThread(long startTime,long timeoutSecond,Thread workThread){
		this.startTime = startTime;
		this.timeoutSecond = timeoutSecond;
		this.workThread = workThread;
		
	}
	
	@Override
	public void run() {
		while(true) {
			long t = System.currentTimeMillis() - startTime;
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "-计算超时...." + t);
			try {
				if(timeoutSecond < t) {
					timeout();
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年1月18日 下午2:20:52
	 */
	private void timeout() {
		workThread.interrupt();
	}
	
}