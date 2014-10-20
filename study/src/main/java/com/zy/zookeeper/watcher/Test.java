package com.zy.zookeeper.watcher;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class Test {

	public static void main(String[] args) throws Exception {
		watchPath("/app1/test/dd");
//		configWatch("朱勇1111");
		
		
//		testBatchCreateForWatcher();
		
	}
	
	public static void testBatchCreateForWatcher() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", 2000, new Watcher() {
			public void process(WatchedEvent event) {
			}
		});
		for(int i = 0 ; i < 100 ;i++) {
			zooKeeper.create("/app1/test/dd/" + i +"_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		}
	}

	public static void configWatch(String nodeData) {
		ConfigCenter cc = new ConfigCenter("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", "/app1/test");
		cc.updateConfig(nodeData);
	}

	public static void watchPath(String path) throws IOException, InterruptedException, KeeperException {
		ZooKeeperWatcher zw1 = new ZooKeeperWatcher();
		zw1.connect("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", path);
		new Thread(zw1).start();
		
	}

	public static void watchPath1() throws IOException, InterruptedException, KeeperException {
		ZooKeeperWatcher zw2 = new ZooKeeperWatcher();
		zw2.connect("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", "/app1/test");
		new Thread(zw2).start();
	}

}
