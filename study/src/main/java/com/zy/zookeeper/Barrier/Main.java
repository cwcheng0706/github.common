package com.zy.zookeeper.Barrier;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
	
	public static boolean end = false;

	public static void main(String[] args) throws UnknownHostException {
		
		System.out.println(InetAddress.getLocalHost().getCanonicalHostName().toString());

		for(int i = 0 ; i < 3; i++ ) {
			Work w = new Work(i+1);
			Thread t = new Thread(w);
			t.start();
			
			
		}
	}
}

class Work implements Runnable {
	
	private int size;
	public Work(int size) {
		this.size = size;
	}

	final Integer mutex = new Integer(-1);

	@Override
	public void run() {
		while(true) {
			System.out.println(Thread.currentThread().getName() + "=============");
			synchronized (mutex) {
				
				if(Main.end) {
					
					System.out.println(Thread.currentThread().getName() + "  end..");
					mutex.notifyAll();
					break;
				}else {
					try {
						System.out.println(Thread.currentThread().getName() + "  wait..");
						if(size == 3) {
							Main.end = true;
						}
						mutex.wait();
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
			
	}

}
