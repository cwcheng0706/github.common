package com.zy.web.vo;

public class UserVo {

	private String name;
	private String password;
	private String[] remember;

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getRemember() {
		return remember;
	}

	public void setRemember(String[] remember) {
		this.remember = remember;
	}


}
