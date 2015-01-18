/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2015年1月17日 下午9:42:38
 */
package com.zy.multithread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Project: study
 * @Author zy
 * @Company: 
 * @Create Time: 2015年1月17日 下午9:42:38
 */
public class Test {

	/**
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年1月17日 下午9:42:38
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		//executor.awaitTermination(3, TimeUnit.SECONDS);
		
//		Runnabler worker = new Runnabler("1");
//		executor.execute(worker);
		
		
		List<Callabler> tasks = new ArrayList<Callabler>();
		for(int i = 0 ; i < 10 ; i++) {
			Callabler c = new Callabler(String.valueOf(i));
			tasks.add(c);
		}
		List<Future<String>> futures = executor.invokeAll(tasks);
		for(int i = 0 ; i < tasks.size() ; i++) {
			Future<String> ret = futures.get(i);
			System.out.println(ret.get());
			
		}
		
		
		
		
		System.out.println("======提交任务完成======");
	}
	
	

}
