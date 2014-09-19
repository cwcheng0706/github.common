package com.zy.jms.activemq.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.jms.activemq.MessageConnector;

public class StringMessageListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(StringMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.debug("--------listen start-----------");
		
		ActiveMQObjectMessage msg = (ActiveMQObjectMessage)message;
		
		try {
			logger.debug("data: "+ msg.getObject());
		} catch (JMSException e) {
			logger.error(e.getMessage());
		}
		
		MessageConnector.getInstance().Reply(message);
		logger.debug("--------listen end -----------");
	}

}
