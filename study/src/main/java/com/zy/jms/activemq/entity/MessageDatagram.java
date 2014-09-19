package com.zy.jms.activemq.entity;

import java.util.Date;

import javax.jms.Session;

public class MessageDatagram {

	private Date date = new Date();

	private String queueName;
	
	private boolean transacted = false;
	
	private int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
	
	private Data data;

	private String type;

	public Date getDate() {
		return date;
	}

	public Data getData() {
		return data;
	}

	public String getType() {
		return type;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean isTransacted() {
		return transacted;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public int getAcknowledgeMode() {
		return acknowledgeMode;
	}

	public void setAcknowledgeMode(int acknowledgeMode) {
		this.acknowledgeMode = acknowledgeMode;
	}

}
