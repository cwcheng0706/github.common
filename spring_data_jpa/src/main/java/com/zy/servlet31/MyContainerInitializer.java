package com.zy.servlet31;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zy.servlet31.application.StudyApplicationInitializer;

@HandlesTypes(StudyApplicationInitializer.class)
public class MyContainerInitializer implements ServletContainerInitializer {

	private static Log logger = LogFactory.getLog(MyContainerInitializer.class);

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		logger.info("==============================");

		if (null != c) {
			for (Iterator<Class<?>> iterator = c.iterator(); iterator.hasNext();) {
				Class<?> clazz = (Class<?>) iterator.next();
				
				StudyApplicationInitializer instance = null;
				try {
					instance = (StudyApplicationInitializer)clazz.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(null != instance) {
					instance.onStartup(ctx);
				}
				
				
				WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ctx);
				
				logger.info(clazz);
			}
		}

		logger.info("==============================");
	}
}
