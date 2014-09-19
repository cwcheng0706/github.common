package com.zy.jms.activemq.entity;

public class StringData extends Data{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5833393391498631433L;
	
	private String content;
	
	public String toString(){
		return this.content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
