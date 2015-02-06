package com.zy.zookeeper.government.leader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test {
	
	final static CountDownLatch latch = new CountDownLatch(3);
	
	public static void main(String[] args) throws Exception {
		for(int i = 0 ; i < 3 ; i++ ) {
			MyThread t = new MyThread(latch);
			Thread tt = new Thread(t);
			tt.start();
		}
		latch.await();
		
		
		if (!latch.await(10000, TimeUnit.SECONDS)) {
			System.out.println("超时");
		}else {
			System.out.println("正常结束");
		}
	}

}
class MyThread implements Runnable {
	
	CountDownLatch latch =null;
	
	public MyThread(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			MyProcessor processor = new MyProcessor();
			processor.start();
			
//			Thread.sleep(10);
			
			processor.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		latch.countDown();
		
	}
	
}

class MyProcessor{
	public void start(){
		System.out.println(Thread.currentThread().getName() + "=========start==========");
	}
	public void stop() {
		System.out.println(Thread.currentThread().getName() + "=========end==========");
	}
}
