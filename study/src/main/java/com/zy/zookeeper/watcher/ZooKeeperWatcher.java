package com.zy.zookeeper.watcher;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperWatcher implements Watcher, Runnable {
	
	private static Log logger = LogFactory.getLog(ZooKeeperWatcher.class);

	private ZooKeeper zooKeeper = null;
	private String znode;
	private String hosts;

	public ZooKeeper getZooKeeper() {
		return this.zooKeeper;
	}

	public boolean connect(String hosts, String znode) {
		boolean ret = false;
		this.znode = znode;
		this.hosts = hosts;
		try {
			this.zooKeeper = new ZooKeeper(hosts, 2000, this);
			this.zooKeeper.exists(znode, true);
			ret = true;
		} catch (IOException e) {
			logger.error("连接Zookeeper服务器异常【" + e + "】");
		}catch (KeeperException | InterruptedException e) {
			logger.error("监控节点，Exist【" + this.znode + "】异常【" + e + "】");
		}
		return ret;
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
		logger.info(event.toString());
		try {
			
			if(event.getState() == KeeperState.Expired) {
				logger.info("重新连接...");
				connect(this.hosts,this.znode);
			}else {
				// 只能监控当前节点 。不能监控子节点
				//this.zooKeeper.exists(znode, true);
				
				this.zooKeeper.getChildren(znode, true);
			}
		} catch(SessionExpiredException e){
			logger.error(e +"重新注册事件。");
		}catch (KeeperException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
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
