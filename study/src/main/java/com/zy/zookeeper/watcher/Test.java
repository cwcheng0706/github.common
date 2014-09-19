package com.zy.zookeeper.watcher;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

public class Test {

	public static void main(String[] args) throws Exception {
		startServer1();
//		configWatch("朱勇1111");
		
		
	}

	public static void configWatch(String nodeData) {
		ConfigCenter cc = new ConfigCenter("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", "/app1/test");
		cc.updateConfig(nodeData);
	}

	public static void startServer1() throws IOException, InterruptedException, KeeperException {
		ZooKeeperWatcher zw1 = new ZooKeeperWatcher();
		zw1.connect("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", "/app1/test");
		new Thread(zw1).start();
		
	}

	public static void startServer2() throws IOException, InterruptedException, KeeperException {
		ZooKeeperWatcher zw2 = new ZooKeeperWatcher();
		zw2.connect("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", "/app1/test");
		new Thread(zw2).start();
	}

}
