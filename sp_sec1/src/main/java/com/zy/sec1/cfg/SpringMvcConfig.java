/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午11:11:11
 */
package com.zy.sec1.cfg;

import org.jfree.chart.servlet.DisplayChart;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
	
	@Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new DisplayChart(), "/chart/*");// ServletName默认值为首字母小写，即myServlet
    }

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

//	@Bean
//	public FilterRegistrationBean getDemoFilter(){
//		DemoFilter demoFilter=new DemoFilter();
//		FilterRegistrationBean registrationBean=new FilterRegistrationBean();
//		registrationBean.setFilter(demoFilter);
//		List<String> urlPatterns=new ArrayList<String>();
//		urlPatterns.add("/*");//拦截路径，可以添加多个
//		registrationBean.setUrlPatterns(urlPatterns);
//		registrationBean.setOrder(1);
//		return registrationBean;
//	}
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
//	@Bean
//	public ServletListenerRegistrationBean<EventListener> getDemoListener(){
//		ServletListenerRegistrationBean<EventListener> registrationBean
//		                           =new ServletListenerRegistrationBean<>();
//		registrationBean.setListener(new DemoListener());
////		registrationBean.setOrder(1);
//		return registrationBean;
//	}
}