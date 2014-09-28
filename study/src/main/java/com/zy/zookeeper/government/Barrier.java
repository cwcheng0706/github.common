package com.zy.zookeeper.government;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * Barrier
 */
public class Barrier extends SyncPrimitive {
	
	private static Log logger = LogFactory.getLog(Barrier.class);
			
	int size;
	String name;

	/**
	 * Barrier constructor
	 *
	 * @param address
	 * @param root
	 * @param size
	 */
	Barrier(String address, String root, int size) {
		super(address);
		this.root = root;
		this.size = size;

		// Create barrier node
		if (zk != null) {
			try {
				Stat s = zk.exists(root, false);
				if (s == null) {
					zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
			} catch (KeeperException e) {
				logger.debug("Keeper exception when instantiating queue: " + e.toString());
			} catch (InterruptedException e) {
				logger.debug("Interrupted exception");
			}
		}

		// My node name
//		try {
//			name = new String(InetAddress.getLocalHost().getCanonicalHostName().toString());
//		} catch (UnknownHostException e) {
//			logger.error(e.toString());
//		}

	}

	/**
	 * Join barrier
	 *
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */

	boolean enter(String nodeName) throws KeeperException, InterruptedException {
		logger.debug("--enter " + nodeName);
//		zk.create(root + "/" + nodeName, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		zk.create(root + "/" + nodeName, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
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

	/**
	 * Wait until all reach barrier
	 *
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */

	boolean leave(String nodeName) throws KeeperException, InterruptedException {
		logger.debug("------leave " + nodeName);
		zk.delete(root + "/" + nodeName, 0);
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
