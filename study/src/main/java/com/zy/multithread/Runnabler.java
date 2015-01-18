/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2015年1月18日 上午10:10:51
 */
package com.zy.multithread;

/**
 * @Project: study
 * @Author zy
 * @Company: 
 * @Create Time: 2015年1月18日 上午10:10:51
 */
public class Runnabler implements Runnable {

	private String str;
	public Runnabler(String str) {
		this.str = str;
	}
	
	public void run() {
		System.out.println("--Runnabler【" + str + "】 start ");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
		
		System.out.println("--Runnabler【" + str + "】 end ");
	}
	
}
