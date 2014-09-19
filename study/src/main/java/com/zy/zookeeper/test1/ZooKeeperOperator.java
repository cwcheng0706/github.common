package com.zy.zookeeper.test1;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperOperator extends AbstractZooKeeper {
	
	public static ZooKeeperOperator instance = new ZooKeeperOperator();
	
	
	public ZooKeeperOperator() {
		connect("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183");
	}
	
	
	private static Log logger = LogFactory.getLog(ZooKeeperOperator.class.getName());

	/**
	 * 创建持久态的znode,比支持多层创建.比如在创建/parent/child的情况下,无/parent.无法通过
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月9日 下午3:38:26
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void create(String path, byte[] data) throws KeeperException, InterruptedException {
		
		Stat tat = zooKeeper.exists(path, this);
		if(null == tat) {
			/**
			 * 此处采用的是CreateMode是PERSISTENT 表示The znode will not be automatically
			 * deleted upon client's disconnect. EPHEMERAL 表示The znode will be
			 * deleted upon the client's disconnect.
			 */
			zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}else {
			logger.warn("此节点【" + path + "】已经存在..");
		}
		
	}
	
	public void monitor(String path) {
		try {
			zooKeeper.exists(path, true);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String path) throws InterruptedException, KeeperException {
		
		Stat tat = zooKeeper.exists(path, this);
		
		if(null != tat) {
			zooKeeper.delete(path,tat.getVersion());
			
			logger.debug("Delete , version:" + tat.getVersion());
		}else {
			logger.warn("节点【" + path + "】不存在！");
		}
	}

	/**
	 * 获取节点信息
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月9日 下午3:39:43
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void getChild(String path) throws KeeperException, InterruptedException {
		try {
			List<String> list = zooKeeper.getChildren(path, false);
			if (list.isEmpty()) {
				logger.debug(path + "中没有节点");
			} else {
				logger.debug(path + "中存在节点");
				for (String child : list) {
					logger.debug("节点为：" + child);
				}
			}
		} catch (KeeperException.NoNodeException e) {
			logger.error(e);
		}
	}

	public byte[] getData(String path) throws KeeperException, InterruptedException {
		return zooKeeper.getData(path, this, null);
	}

	
}
