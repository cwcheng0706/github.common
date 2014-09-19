package com.zy.zookeeper.test1;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class TestZoo {

	private static Log logger = LogFactory.getLog(TestZoo.class);
	
	public static void main(String[] args) throws Exception {
		
		
		
		ZooKeeper zooKeeper = new ZooKeeper("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", 50000, null);

		zooKeeper.create("/app1/test/file", "朱勇".getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zooKeeper.delete("/app1/test/file",-1);
		
		try {
//			ZooKeeperOperator zkoperator = ZooKeeperOperator.instance;
//			
//			zkoperator.create("/app1/test/file", "测试file".getBytes("UTF-8"));
//			zkoperator.delete("/app1/test/file");
			
			
//			zkoperator.connect("172.16.2.199:2181");
//			zkoperator.connect("172.16.2.199:2182");
//			zkoperator.connect("172.16.2.199:2183");


//			String zktest = "1111111";
//			zkoperator.create("/app1/test", zktest.getBytes("UTF-8"));
			
			
//			String node = new String(zkoperator.getData("/app1"),"UTF-8");
//			logger.debug("node【" + node +"】");

//			zkoperator.create("/app1/test1", "测试1".getBytes("UTF-8"));
//			zkoperator.getChild("/app1");
//			zkoperator.delete("/app1/test1");
			
//			zkoperator.create("/app1/test2", "测试2".getBytes("UTF-8"));
//			zkoperator.getChild("/app1");
//			zkoperator.delete("/app1/test2");
			
//			zkoperator.create("/app1/test3", "测试3".getBytes("UTF-8"));
//			zkoperator.getChild("/app1");
//			zkoperator.delete("/app1/test3");

//			zkoperator.close();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
