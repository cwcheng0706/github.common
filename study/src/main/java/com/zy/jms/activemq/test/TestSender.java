package com.zy.jms.activemq.test;

import com.zy.jms.activemq.MessageConnector;
import com.zy.jms.activemq.entity.MessageDatagram;
import com.zy.jms.activemq.entity.StringData;

public class TestSender {

	public static void main(String[] args) throws Exception {
		
		MessageDatagram gram = new MessageDatagram();
		StringData stringData = new StringData();
		stringData.setContent("你好哇。。。");
		gram.setData(stringData);
		gram.setQueueName("queue-zy");
		gram.setType("test");
		
		MessageConnector.getInstance().send(gram);
		
		
	}
}
