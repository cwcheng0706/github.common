/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2015年1月18日 上午10:10:28
 */
package com.zy.multithread;

import java.util.concurrent.Callable;

/**
 * @Project: study
 * @Author zy
 * @Company: 
 * @Create Time: 2015年1月18日 上午10:10:28
 */
public class Callabler implements Callable<String> {
	
	private String str;
	public Callabler(String str) {
		this.str = str;
	}

	public String call() throws Exception {
		System.out.println("--Callabler 【" + str + "】start ");
		
		Thread.sleep(3000);
		
		System.out.println("--Callabler 【" + str + "】end ");
		return str + " success";
	}
	
}