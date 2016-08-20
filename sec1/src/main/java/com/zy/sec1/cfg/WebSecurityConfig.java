/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月20日 下午8:06:56
 */
package com.zy.sec1.cfg;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.zy.sec1.entity.User;

/**
 * @Project: sec1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月20日 下午8:06:56
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
//		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
		
		auth.authenticationProvider(customerAuthenticationProvider());
	}

	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info("-------configure-------");
		super.configure(http);
//		http
//			.authorizeRequests()
//				.anyRequest().authenticated()
//				.and()
//			.formLogin()
//				.and()
//			.httpBasic();
		
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/home").permitAll()
				.antMatchers("/error").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login") 
				.permitAll()
				.and()
			.logout()                                                                
				.logoutUrl("/my/logout")                                                 
				.logoutSuccessUrl("/my/index")                                           
//				.logoutSuccessHandler(logoutSuccessHandler)                              
				.invalidateHttpSession(true)                                             
//				.addLogoutHandler(logoutHandler)                                         
//				.deleteCookies(cookieNamesToClear)
				;        
		
//		http.csrf().disable();
	}
	
	@Bean
	public AuthenticationProvider customerAuthenticationProvider() {
		return new CustomerAuthenticationProvider();
	}
	@Bean
	public UserDetailsService CustomerUserDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	private class CustomerAuthenticationProvider implements AuthenticationProvider{
		
		@Autowired
		UserDetailsService userDetailsService;
		
		public boolean supports(Class<?> authentication) {
			LOGGER.info("--------supports------");
			return true;
		}
		
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			LOGGER.info("--------authenticate------");
			
			//username
	        System.out.println("user name: "+authentication.getName());
	        //password
	        System.out.println("password: "+authentication.getCredentials());
	        System.out.println("getPrincipal: "+authentication.getPrincipal());
	        System.out.println("getAuthorities: "+authentication.getAuthorities());
	        System.out.println("getDetails: "+authentication.getDetails());
	        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
	        System.out.println("IP:" + webAuthenticationDetails.getRemoteAddress());
	        System.out.println("SESSIONID:" + webAuthenticationDetails.getSessionId());
	        
	        	
	        //
	        UserDetails userDetails = (UserDetails)this.userDetailsService.loadUserByUsername(authentication.getName());
	       
	        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(),userDetails.getAuthorities());
	        return result;
	        
		}
	}
	
	private class CustomerUserDetailsService implements UserDetailsService{
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
			//自定义 查询 数据库对应角色 
			ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
	        list.add(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));
	        User details = new User(username, "koala", list);
	        return details;
		}
		
	}
}
