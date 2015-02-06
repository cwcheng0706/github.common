package com.zy.servlet31.application;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zy.servlet31.DynamicServlet;

public class DefaultStudyApplicationInitializer implements StudyApplicationInitializer {
	
	private static Log logger = LogFactory.getLog(DefaultStudyApplicationInitializer.class);

	@Override
	public void onStartup(ServletContext ctx) {
		logger.info("动态注册Servlet...");
		
		ctx.addServlet("dynamicServlet", DynamicServlet.class)
		   .addMapping("/rest");
		
	}

}
