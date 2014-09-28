package com.zy.zookeeper.Barrier;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Barrier implements Watcher {
	
	private static Log logger = LogFactory.getLog(Barrier.class);

	public String name = "init";
	
	private static final String addr = "172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183";
	private ZooKeeper zk = null;
	private Integer mutex;
	private int size = 0;
	private String root;

	public Barrier(String root, int size) {
		this.root = root;
		this.size = size;

		try {
			zk = new ZooKeeper(addr, 10 * 1000, this);
			mutex = new Integer(-1);
			Stat s = zk.exists(root, false);
			if (s == null) {
				zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void process(WatchedEvent event) {
		synchronized (mutex) {
			logger.debug("--notify-" + name);
			mutex.notify();
		}
	}

	public boolean enter(String name) throws Exception {
		logger.debug("---enter---");
		zk.create(root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);
				if (list.size() < size) {
					mutex.wait();
				} else {
					return true;
				}
			}
		}
	}

	public boolean leave(String name) throws KeeperException, InterruptedException {
		logger.debug("---leave---");
		zk.delete(root + "/" + name, 0);
		while (true) {
			synchronized (mutex) {
				List<String> list = zk.getChildren(root, true);
				if (list.size() > 0) {
					mutex.wait();
				} else {
					return true;
				}
			}
		}
	}

}