/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2015年1月18日 下午1:38:02
 */
package com.zy.multithread.timeout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
//		executor.execute(new WorkTimeoutThread(1000));
//		executor.submit(new WorkTimeoutThread(1000));
		
		System.out.println("main end");
	}
}

class WorkTimeoutThread extends Thread{
	
	private long timeoutSecond;
	
	public WorkTimeoutThread(long timeoutSecond) {
		this.setName("WorkThread");
		Thread daemon = new Thread(new DaemonThread(System.currentTimeMillis(),timeoutSecond,this));
		daemon.setDaemon(true);
		this.timeoutSecond = timeoutSecond;
		daemon.start();
		
	}

	@Override
	public void run() {
		System.out.println("work start");
		try{
			
			Thread.sleep(4000);
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
		this.setDaemon(true);
		this.setName("DeamonThread");
		
	}
	
	@Override
	public void run() {
		while(true) {
			long t = System.currentTimeMillis() - startTime;
			System.out.println("当前线程[name=" + this.getName() + " Daemon=" + this.isDaemon() + "]  " + this.isInterrupted() +"  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "-计算超时...." + t);
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