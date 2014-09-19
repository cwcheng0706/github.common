package com.zy.zookeeper.test2;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class AppServer {
	private String groupNode = "app1";
	private String subNode = "sub";

	public void connectZookeeper(String address) throws Exception {

		ZooKeeper zk = new ZooKeeper("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", 5000, new Watcher() {
			public void process(WatchedEvent event) {
				// 不做处理
			}
		});

		// 在"/sgroup"下创建子节点
		// 子节点的类型设置为EPHEMERAL_SEQUENTIAL, 表明这是一个临时节点, 且在子节点的名称后面加上一串数字后缀
		// 将server的地址数据关联到新创建的子节点上
		String createdPath = zk.create("/" + groupNode + "/" + subNode, address.getBytes("utf-8"), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);
		
		System.out.println("create: " + createdPath);
	}

	/**
	 * server的工作逻辑写在这个方法中 此处不做任何处理, 只让server sleep
	 */
	public void handle() throws InterruptedException {
		Thread.sleep(Long.MAX_VALUE);
	}

	public static void main(String[] args) throws Exception {
		// 在参数中指定server的地址

		AppServer as = new AppServer();
		as.connectZookeeper("test event");

		as.handle();
	}
}
