package com.zy.core;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.zy.core.config.SpringAppConfig;

/**
 * 
 * @Project: spring_data_jpa_anno
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年10月16日 上午9:37:34
 */
public class DefaultWebApplicationInitializer implements WebApplicationInitializer {
	
	private final static Logger logger = Logger.getLogger(DefaultWebApplicationInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		registerDispatcherServlet(servletContext);
		
		registerContextLoaderListener(servletContext);
		
		registerOpenEntityManagerInViewFilter(servletContext);
		
		registerCharacterEncodingFilter(servletContext);
		
		registerHttpPutFormContentFilter(servletContext);
		
		registerIntrospectorCleanupListener(servletContext);
	}
	
	
	private void registerContextLoaderListener(ServletContext servletContext) {
		logger.info("开始注册ContextLoaderListener....");
		try{
			//使用xml方式
//			servletContext.setInitParameter("contextConfigLocation", "classpath*:applicationContext-*.xml");
//			servletContext.addListener("org.springframework.web.context.ContextLoaderListener");
			
			//全注解方式
//			servletContext.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
//			servletContext.addListener("org.springframework.web.context.ContextLoaderListener");
			
			
	        AnnotationConfigWebApplicationContext  applicationContext = new AnnotationConfigWebApplicationContext();
	        applicationContext.register(SpringAppConfig.class);
	        servletContext.addListener(new ContextLoaderListener(applicationContext));
			
		}catch(Exception e) {
			logger.error("注册ContextLoaderListener异常【" + e + "】");
		}
		logger.info("成功注册ContextLoaderListener....");
	}
	
	private void registerDispatcherServlet(ServletContext servletContext) {
		logger.info("开始注册DispatcherServlet....");
		try{
			
			/** 配置DispatcherServlet 相当在web.xml配置 **/
			ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet());
			
			registration.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
			
			registration.setLoadOnStartup(1);
			registration.addMapping("/");
			
			
		}catch(Exception e) {
			logger.error("注册DispatcherServlet异常【" + e + "】");
		}
		logger.info("成功注册DispatcherServlet....");
	}
	
	private void registerOpenEntityManagerInViewFilter(ServletContext servletContext) {
		logger.info("开始注册OpenEntityManagerInViewFilter....");
		try{
			
			FilterRegistration.Dynamic registration = servletContext.addFilter("OpenEntityManagerInViewFilter", new OpenEntityManagerInViewFilter());
			
			/**
			 * 指定org.springframework.orm.jpa.LocalEntityManagerFactoryBean在spring配置文件中的名称,默认值为entityManagerFactory 
			 * 如果LocalEntityManagerFactoryBean在spring中的名称不是entityManagerFactory,该参数一定要指定,否则会出现找不到entityManagerFactory的例外
			 */
			registration.setInitParameter("entityManagerFactoryBeanName", "entityManagerFactoryBean");
			
			boolean isMatchAfter = true;
			EnumSet<DispatcherType> dispatcherTypes =EnumSet.of(DispatcherType.REQUEST,DispatcherType.FORWARD);
			String urlPatterns = "/*";
			registration.addMappingForUrlPatterns(dispatcherTypes, isMatchAfter, urlPatterns );
			
		}catch(Exception e) {
			logger.error("注册OpenEntityManagerInViewFilter异常【" + e + "】");
		}
		logger.info("成功注册OpenEntityManagerInViewFilter....");
	}
	
	private void registerCharacterEncodingFilter(ServletContext servletContext) {
		logger.info("开始注册CharacterEncodingFilter....");
		try{
			
			FilterRegistration.Dynamic registration = servletContext.addFilter("encodingFilter",new CharacterEncodingFilter());
			
			registration.setInitParameter("encoding","UTF-8");
			registration.setInitParameter("forceEncoding","true");
			
			boolean isMatchAfter = true;
			EnumSet<DispatcherType> dispatcherTypes =EnumSet.of(DispatcherType.REQUEST,DispatcherType.FORWARD);
			String urlPatterns = "/*";
			registration.addMappingForUrlPatterns(dispatcherTypes, isMatchAfter, urlPatterns);
			
		}catch(Exception e) {
			logger.error("注册CharacterEncodingFilter异常【" + e + "】");
		}
		logger.info("成功注册CharacterEncodingFilter....");
	}
	
	private void registerHttpPutFormContentFilter(ServletContext servletContext) {
		logger.info("开始注册HttpPutFormContentFilter....");
		try{
			
			FilterRegistration.Dynamic registration = servletContext.addFilter("httpPutFormFilter",new HttpPutFormContentFilter());
			
			boolean isMatchAfter = true;
			EnumSet<DispatcherType> dispatcherTypes =EnumSet.of(DispatcherType.REQUEST,DispatcherType.FORWARD);
			String urlPatterns = "/*";
			registration.addMappingForUrlPatterns(dispatcherTypes, isMatchAfter, urlPatterns);
			
		}catch(Exception e) {
			logger.error("注册HttpPutFormContentFilter异常【" + e + "】");
		}
		logger.info("成功注册HttpPutFormContentFilter....");
	}
	
	private void registerIntrospectorCleanupListener(ServletContext servletContext) {
		servletContext.addListener("org.springframework.web.util.IntrospectorCleanupListener");
		
		//or
//		org.springframework.web.util.IntrospectorCleanupListener introspectorCleanupListener = new org.springframework.web.util.IntrospectorCleanupListener();
//		servletContext.addListener(introspectorCleanupListener);
	}
}
