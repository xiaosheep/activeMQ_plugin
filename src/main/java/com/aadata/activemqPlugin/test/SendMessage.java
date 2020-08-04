package com.aadata.activemqPlugin.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SendMessage {
	
	public static void main(String[] args) throws JMSException {
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("admin","admin123","tcp://localhost:61616");
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		//3.启动连接
		connection.start();
		//4.获取session(会话对象)  参数1：是否启动事务  参数2：消息确认方式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建主题对象 发送的主题列
		Topic topic = session.createTopic("test");
		//6.创建消息生产者对象
		MessageProducer producer = session.createProducer(topic);
		//7.创建消息对象（文本消息）
		TextMessage textMessage = session.createTextMessage("拦截器拦截器！！！！！！！！！！！！");
		//8.发送消息
		producer.send(textMessage);
		producer.close();
		
//		Queue queue = session.createQueue("test-queue");
//		//	第六步：使用Session对象创建一个Producer对象。
//		MessageProducer messageProducer = session.createProducer(queue);
//		//	第七步：创建一个Message对象，创建一个TextMessage对象。
//		//		TextMessage textMessage = new ActiveMQTextMessage();
//		//		textMessage.setText("hello");
//		TextMessage textMessage = session.createTextMessage("hello");
//		//	第八步：使用Producer对象发送消息。
//		messageProducer.send(textMessage);
//		//9.关闭资源
//		messageProducer.close();
		
		session.close();
		connection.close();
	}
}
