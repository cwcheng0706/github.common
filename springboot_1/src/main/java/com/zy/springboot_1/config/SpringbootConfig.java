/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午12:55:58
 */
package com.zy.springboot_1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午12:55:58
 */
@Component
public class SpringbootConfig {

	@Value("${user.name}")
	private String name;
	
	@Value("${user.password}")
	private String password;
	
	@Value("${timout}")
	private String timout;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTimout() {
		return timout;
	}

	public void setTimout(String timout) {
		this.timout = timout;
	}

}
