package com.zy.jms.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.jms.activemq.entity.Data;
import com.zy.jms.activemq.entity.MessageDatagram;
import com.zy.jms.activemq.listener.StringMessageListener;

public class MessageConnector {

	private static final Logger logger = LoggerFactory.getLogger(MessageConnector.class);

	private ConnectionFactory connectionFactory = null;

	// Connection ：JMS 客户端到JMS Provider 的连接
	private Connection connection = null;

	private MessageConfig config;

	private MessageConnector() {

		if (null == config) {
			config = new MessageConfig();
			logger.error("初始化配置文件为空..");
			// throw new RuntimeException("没有初始化配置文件，请查看是否有配置文件..");
		}

		connectionFactory = createConnectionFactory(config);

		initConnection();

	}
	
	private static MessageConnector connector = new MessageConnector();
	
	public static MessageConnector getInstance() {
		return connector;
	}

	private void initConnection() {
		// 构造从工厂得到连接对象
		try {
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
		} catch (JMSException e) {
			logger.error("初始化连接异常..");
			throw new RuntimeException(e);
		}
	}

	private String assembleBrokerUrl(MessageConfig config) {
		String brokerUrl = "";

		brokerUrl = config.getTransport() + "://" + config.getHost() + ":" + config.getPort();

		return brokerUrl;
	}

	private ConnectionFactory createConnectionFactory(MessageConfig config) {

		String brokerUrl = assembleBrokerUrl(config); // tcp://172.16.2.199:61616

		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory = null;

		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(config.getUserName(), config.getUserPasswd(), brokerUrl);

		return connectionFactory;
	}

	public void send(MessageDatagram gram) throws Exception {

		// 获取操作连接
		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

		// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
		Queue destination = session.createQueue(gram.getQueueName());

		// 得到消息生成者【发送者】
		MessageProducer producer = session.createProducer(destination);

		// 设置不持久化，此处学习，实际根据项目决定
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		// 构造消息，此处写死，项目就是参数，或者方法获取
		ObjectMessage message = session.createObjectMessage(gram.getData());

		// 发送消息到目的地方
		logger.debug("发送消息：" + gram.getData());

		// 发送
		producer.send(message);

		session.commit();

		session.close();

		// connection.close();

	}
	
	public boolean Reply(Message message) {
		boolean ret = true;
		Session session;
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue destination = session.createQueue("queue-zy");
			
			session.createProducer(destination);
			
			
		} catch (JMSException e) {
			ret = false;
			logger.error(e.getMessage());
		}
		
		return ret;
	}

	public Data receive(MessageDatagram datagram) throws Exception {
		logger.debug("---receive start --");
		Data data = null;
		try {
			// 获取操作连接
			final Session session = connection.createSession(datagram.isTransacted(), datagram.getAcknowledgeMode());

			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			Queue destination = session.createQueue(datagram.getQueueName());

			MessageConsumer consumer = session.createConsumer(destination);

			if (datagram.isTransacted()) {
				MessageListener listener = new StringMessageListener();
				consumer.setMessageListener(listener);
				
			} else {
				// 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
				ActiveMQObjectMessage o = (ActiveMQObjectMessage) consumer.receive();
				data = (Data) o.getObject();

				if (null != data) {
					logger.debug("收到消息" + data);
				} else {
					logger.warn("收到消息为空..");
				}
			}
			
		} catch (Exception e) {
			logger.error("接收消息异常.." + e);
			throw new RuntimeException(e);
		}
		logger.debug("---receive end --");
		return data;

	}

}
