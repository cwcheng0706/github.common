/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午11:11:11
 */
package com.zy.sec1.cfg;

import java.util.ArrayList;
import java.util.List;


import org.jfree.chart.servlet.DisplayChart;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.zy.sec1.servlet.filter.CustomerHttpSessionFilter;


@Configuration
@EnableWebMvc
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
	
	public static final List<String> OnlineUserList = new ArrayList<String>();


	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new DisplayChart(), "/chart/*");// ServletName默认值为首字母小写，即myServlet
    }

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public FilterRegistrationBean getHttpSessionFilter(){
		CustomerHttpSessionFilter customerHttpSessionFilter=new CustomerHttpSessionFilter();
		FilterRegistrationBean registrationBean=new FilterRegistrationBean();
		registrationBean.setFilter(customerHttpSessionFilter);
		List<String> urlPatterns=new ArrayList<String>();
		urlPatterns.add("/login");//拦截路径，可以添加多个
		
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(1);
		return registrationBean;
	}
	
//	@Bean
//	public ServletRegistrationBean getDemoServlet(){
//		DemoServlet demoServlet=new DemoServlet();
//		ServletRegistrationBean registrationBean=new ServletRegistrationBean();
//		registrationBean.setServlet(demoServlet);
//		List<String> urlMappings=new ArrayList<String>();
//		urlMappings.add("/demoservlet");////访问，可以添加多个
//		registrationBean.setUrlMappings(urlMappings);
//		registrationBean.setLoadOnStartup(1);
//		return registrationBean;
//	}
//	
	
//	private class CustomerHttpSessionListener implements HttpSessionListener{
//
//		private final Logger LOGGER = LoggerFactory.getLogger(CustomerHttpSessionListener.class);
//				
//		public void sessionCreated(HttpSessionEvent se) {
//			LOGGER.info("---------sessionCreated--------");
//		}
//
//		public void sessionDestroyed(HttpSessionEvent se) {
//			LOGGER.info("---------sessionDestroyed--------");
//		}
//		
//	}
	
//	@Bean
//	public ServletListenerRegistrationBean<HttpSessionListener> getCustomerHttpSessionListener(){
//		ServletListenerRegistrationBean<HttpSessionListener> registrationBean = new ServletListenerRegistrationBean<HttpSessionListener>();
//		registrationBean.setListener(new CustomerHttpSessionListener());
////		registrationBean.setOrder(1);
//		return registrationBean;
//	}
	
//	@Bean
//	public ServletListenerRegistrationBean<CustomerHttpSessionEventPublisher> getHttpSessionEventPublisherListener(){
//		ServletListenerRegistrationBean<CustomerHttpSessionEventPublisher> registrationBean = new ServletListenerRegistrationBean<CustomerHttpSessionEventPublisher>();
//		registrationBean.setListener(httpSessionEventPublisher);
////		registrationBean.setOrder(1);
//		return registrationBean;
//	}
}