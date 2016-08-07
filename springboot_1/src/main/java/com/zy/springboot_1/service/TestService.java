/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午9:14:06
 */
package com.zy.springboot_1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午9:14:06
 */
@Service
public class TestService {

	@Value("${user.name}")
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
