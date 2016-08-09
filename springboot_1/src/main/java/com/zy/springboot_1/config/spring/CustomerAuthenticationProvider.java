/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月9日 下午10:43:10
 */
package com.zy.springboot_1.config.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.zy.springboot_1.entity.UserDetails;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月9日 下午10:43:10
 */
@Component
public class CustomerAuthenticationProvider implements AuthenticationProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAuthenticationProvider.class);

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		LOGGER.info("--------CustomerAuthenticationProvider-------");
		UserDetails user = new UserDetails();
		String password = String.valueOf(auth.getCredentials());
		LOGGER.info("password: " + password);
		user.setUsername(auth.getName());
		user.setPassword(password);
		
		return new UsernamePasswordAuthenticationToken(user,password);
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}
