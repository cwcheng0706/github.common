/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月8日 上午12:18:32
 */
package com.zy.springboot_1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月8日 上午12:18:32
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").permitAll()
		 	.antMatchers("/loginIndex").permitAll()
		 	.antMatchers("/test*","/err*").permitAll()
		 	
		 	.anyRequest().authenticated() //表示除了上面配置的  其它都需要安全验证，一般这行放置最后
		 	
		;
		
		http.logout()                                                                
			.logoutUrl("/loginIndex")                                                 
			.logoutSuccessUrl("/")                                           
//			.logoutSuccessHandler(logoutSuccessHandler)                              
			.invalidateHttpSession(true)                                             
//			.addLogoutHandler(logoutHandler)                                         
//			.deleteCookies(cookieNamesToClear)                                       
			.and()
			;
	}
	
	
	
	/**
	 * 以下可以自己实现 多个WebSecurityConfigurerAdapter  实现分离
	 * @Project: springboot_1
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2016年8月8日 上午12:50:50
	 */
	@Configuration
	@Order(1)                                                        
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http
				.antMatcher("/api/**")                               
				.authorizeRequests()
					.anyRequest().hasRole("ADMIN")
					.and()
				.httpBasic();
		}
	}
	@Configuration                                                   
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.anyRequest().authenticated()
					.and()
				.formLogin();
		}
	}
}
