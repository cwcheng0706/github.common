package com.zy.sync;

public class TestSync {

	public static void main(String[] args) {
		
		final A a = new A();
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				A.b();
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					A.a();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		
		Thread t3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				A.b();
			}
		});
		
		t1.start();
		t2.start();
		t3.start();
		

	}

}

class A{
	
	
	static byte[] lock = new byte[0];
	
	
	public static void a(){
		System.out.println(Thread.currentThread().getName() + "------a()-------");
	}
	
	public static void b(){
		synchronized (lock) {
			System.out.println(Thread.currentThread().getName() + "-----b()--- start");
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(Thread.currentThread().getName() + "-----b()--- end");
		}
	}
}
