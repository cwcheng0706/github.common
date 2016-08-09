/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午1:14:56
 */
package com.zy.springboot_1.entity;

import java.io.Serializable;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月7日 下午1:14:56
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277234226153623691L;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
