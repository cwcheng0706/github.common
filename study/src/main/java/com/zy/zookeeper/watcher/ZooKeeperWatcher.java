package com.zy.zookeeper.watcher;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperWatcher implements Watcher, Runnable {

	private static Log logger = LogFactory.getLog(ZooKeeperWatcher.class);

	private ZooKeeper zooKeeper = null;
	private String znode;


	public ZooKeeper getZooKeeper() {
		return this.zooKeeper;
	}

	public void connect(String hosts, String znode) throws IOException, InterruptedException, KeeperException {
		this.zooKeeper = new ZooKeeper(hosts, 2000, this);
		this.znode = znode;
		this.zooKeeper.exists(znode, true);
	}

	// public void setData(byte[] data) {
	// try {
	// Stat s = this.zooKeeper.exists(znode, false);
	// this.zooKeeper.setData(znode, data, s.getVersion());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event.toString());
		try {
			this.zooKeeper.exists(znode, true);// 不知道为什么一定要加上这句话，下次事件到来时，才会触发process事件
												// 只能监控当前节点 。不能监控子节点
			this.zooKeeper.getChildren(znode, true);
		} catch (Exception e) {
			logger.error("线程事件处理异常.." + e);
		}
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				while (true) {
					wait();
				}
			}
		} catch (Exception e) {
			logger.error("线程休眠异常.." + e);
		}
	}

}
