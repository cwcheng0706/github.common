package com.zy.jms.activemq;

public class MessageConfig {

	private String userName;

	private String userPasswd;

	private String transport;

	private String host;

	private String port;
	
	public MessageConfig() {
		transport = "tcp";
		host = "172.16.2.199";
		port = "61616";
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public String getTransport() {
		return transport;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}


}
