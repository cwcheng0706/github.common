package com.zy.zookeeper.test1;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class AbstractZooKeeper implements Watcher {
	private static Log logger = LogFactory.getLog(AbstractZooKeeper.class.getName());

	// 缓存时间
	private static final int SESSION_TIME = 2000;
	protected static ZooKeeper zooKeeper;
	protected CountDownLatch countDownLatch = new CountDownLatch(1);
	
	protected void connect(String hosts){
		try {
			zooKeeper = new ZooKeeper(hosts, SESSION_TIME, this);
			countDownLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		
		logger.debug("----watcher---");
		logger.debug("path:" + event.getPath() + "  事件类型：" + event.getType());
		
		if (event.getState() == KeeperState.SyncConnected) {
			countDownLatch.countDown();
		}
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	public void close() throws InterruptedException {
		zooKeeper.close();
	}
}
