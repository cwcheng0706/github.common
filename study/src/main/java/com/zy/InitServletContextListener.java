package com.zy;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class InitServletContextListener implements ServletContextListener {

	private static Log logger = LogFactory.getLog(InitServletContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("-----init listener-----");
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("-----destory listener-----");
	}

}
