/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年3月11日 下午8:36:40
 */
package com.zy.lock.redis.distributed;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisLockMain {

	public static int count = 0;
	
	public static void main(String[] args) throws Exception {
		// 创建jedis池配置实例
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置池配置项值
		config.setMaxTotal(1024);
		config.setMaxIdle(200);
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

		// 根据配置实例化jedis池
		JedisPool pool = new JedisPool(config, "172.16.230.181", 6379);
		
		
		
		
		ExecutorService executor = Executors.newFixedThreadPool(20);
		
		List<Callabler> ts = new ArrayList<Callabler>();
		for(int i = 0 ; i < 100;i++) {
			Locker l = new Locker(pool);
			ts.add(l);
		}
		
		executor.invokeAll(ts);
		

	}

}

class Locker extends Callabler{
	
	JedisPool pool;
	
	public Locker(JedisPool pool) {
		this.pool = pool;
	}

	public Object call() throws Exception {
		IRedisLockHandler lock = new RedisLockHandler(pool);
		
		
		
////		if (lock.tryLock("abcd", 20, TimeUnit.SECONDS)) {
//		if (lock.lock("abcd")) {
////			System.out.println(" get lock ...");
//			RedisLockMain.count = RedisLockMain.count +1;
////			Thread.sleep(20000);
//			lock.unLock("abcd");
//			System.out.println(RedisLockMain.count);
//		} else {
//			System.out.println(" not get lock ...");
//		}
		
		
		
		
		lock.lock("abcd");
		RedisLockMain.count = RedisLockMain.count +1;
		lock.unLock("abcd");
		System.out.println(RedisLockMain.count);
		Thread.sleep(1500);
		
		
		
		return null;
	}
	
}

abstract class Callabler implements Callable<Object>{

}