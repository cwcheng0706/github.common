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
	
	private String account;
	private String passwork;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPasswork() {
		return passwork;
	}

	public void setPasswork(String passwork) {
		this.passwork = passwork;
	}
}
