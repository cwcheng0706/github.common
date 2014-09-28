package com.zy.zookeeper.government;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;

public class Test {

	private static Log logger = LogFactory.getLog(Test.class);

	public static String root = "/app1/barrier";
	public static String address = "172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183";
	public static int barrierSize = 3;

	public static void main(String args[]) {
		// queueTest(args);

		for (int i = 0; i < barrierSize; i++) {
			barrierTest(i + "");
		}

	}

	public static void queueTest(String args[]) {
		Queue q = new Queue(args[1], "/app1");

		logger.debug("Input: " + args[1]);
		int i;
		Integer max = new Integer(args[2]);

		if (args[3].equals("p")) {
			logger.debug("Producer");
			for (i = 0; i < max; i++)
				try {
					q.produce(10 + i);
				} catch (KeeperException e) {

				} catch (InterruptedException e) {

				}
		} else {
			logger.debug("Consumer");

			for (i = 0; i < max; i++) {
				try {
					int r = q.consume();
					logger.debug("Item: " + r);
				} catch (KeeperException e) {
					i--;
				} catch (InterruptedException e) {

				}
			}
		}
	}

	public static void barrierTest(final String nodeName) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				Barrier b = new Barrier(address, root, new Integer(barrierSize));
				try {
					boolean flag = b.enter(nodeName);
					logger.debug("Entered barrier: " + barrierSize);
					if (!flag) {
						logger.debug("Error when entering the barrier");
					}
				} catch (Exception e) {
					logger.error(e);
				}

				// Generate random integer
				Random rand = new Random();
				int r = rand.nextInt(100);
				// Loop for rand iterations
				for (int i = 0; i < r; i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						logger.error(e);
					}
				}
				try {
					b.leave(nodeName);
				} catch (KeeperException e) {

				} catch (InterruptedException e) {

				}
				logger.debug("Left barrier");

			}
		},nodeName).start();

	}
}
