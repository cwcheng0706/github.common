/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月8日 上午12:18:32
 */
package com.zy.springboot_1.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.zy.springboot_1.config.spring.CustomerAuthenticationProvider;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月8日 上午12:18:32
 */
@Configuration
public class SpringSecConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecConfig.class);
	
	@Autowired
	private CustomerAuthenticationProvider authenticationProvider;
			
	protected void configure(HttpSecurity http) throws Exception {
		
		AuthenticationSuccessHandler successHandler = new AuthenticationSuccessHandler() {
			public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth)
					throws IOException, ServletException {
				LOGGER.info("--------AuthenticationSuccessHandler-------");
				String name = auth.getName();
				Object principal = auth.getPrincipal();
				Object obj = auth.getDetails();
				Object credentials = auth.getCredentials();
				
				req.getRequestDispatcher("/index").forward(req, resp);
			}
		};
		
		LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler() {
			public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth)
					throws IOException, ServletException {
				LOGGER.info("--------LogoutSuccessHandler-------");
				
			}
		};
		
		LogoutHandler logoutHandler = new LogoutHandler() {
			public void logout(HttpServletRequest req, HttpServletResponse resp, Authentication auth) {
				LOGGER.info("--------LogoutHandler-------");
				
			}
		};
		
		
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/user").permitAll()
		 	.antMatchers("/login*").permitAll()
		 	.antMatchers("/test*","/err*").permitAll()
		 	.anyRequest().authenticated() //表示除了上面配置的  其它都需要安全验证，一般这行放置最后
		 	;
		
		//登录
		http
		 	.formLogin().loginPage("/login").permitAll()
		 	.successHandler(successHandler)
//		 	.successForwardUrl("/index")
//		 	.failureForwardUrl("/error")
		;
		
		
		//登出
//		http.logout()                                                                
//			.logoutUrl("/logout")                                                 
//			.logoutSuccessUrl("/logoutSuccess")                                           
//			.logoutSuccessHandler(logoutSuccessHandler)              
//			.invalidateHttpSession(true)//清空session                                          
//			.addLogoutHandler(logoutHandler)                    
////			.deleteCookies(cookieNamesToClear)                    
//			.and()
//			;
	}
	
//	@Bean
//    public LoginSuccessHandler loginSuccessHandler(){
//       return new LoginSuccessHandler();//code6
//    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		LOGGER.info("--------configureGlobal-------");
	        auth
	            .inMemoryAuthentication()
	                .withUser("admin123").password("admin123456").roles("USER");
	        
	        auth.authenticationProvider(authenticationProvider);
	}
	
	
	
	/**
	 * 以下可以自己实现 多个WebSecurityConfigurerAdapter  实现分离
	 * @Project: springboot_1
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2016年8月8日 上午12:50:50
	 */
//	@Configuration
//	@Order(1)                                                  
//	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//				.antMatcher("/api/**")                               
//				.authorizeRequests()
//					.anyRequest().hasRole("ADMIN")
//					.and()
//				.httpBasic();
//		}
//	}
	
//	@Configuration   
//	@Order(2)
//	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//		protected void configure(HttpSecurity http) throws Exception {
//			http.authorizeRequests()
//					.anyRequest().authenticated()
//					.and()
//				.formLogin();
//		}
//	}
}
