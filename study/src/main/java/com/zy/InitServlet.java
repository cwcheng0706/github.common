package com.zy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zy.zookeeper.watcher.ConfigCenter;
import com.zy.zookeeper.watcher.ZooKeeperWatcher;


public class InitServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5115479711523314217L;
	
	private static Log logger = LogFactory.getLog(InitServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		logger.debug("--init servlet--");
		
		try {
			
			ZooKeeperWatcher zw1 = new ZooKeeperWatcher();
			zw1.connect("172.16.2.199:2181,172.16.2.199:2182,172.16.2.199:2183", "/app1/test");
			new Thread(zw1).start();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 				
		
	}

}
