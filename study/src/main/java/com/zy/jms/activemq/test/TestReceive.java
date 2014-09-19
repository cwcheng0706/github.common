package com.zy.jms.activemq.test;

import com.zy.jms.activemq.MessageConnector;
import com.zy.jms.activemq.entity.MessageDatagram;
import com.zy.jms.activemq.entity.StringData;

public class TestReceive {

	public static void main(String[] args) throws Exception {
		
		
		
		
		MessageDatagram gram = new MessageDatagram();
		gram.setTransacted(true);
		gram.setQueueName("queue-zy");
		StringData data = (StringData) MessageConnector.getInstance().receive(gram );
		
		
		System.out.println("接收消息：" + data);
	}

}
