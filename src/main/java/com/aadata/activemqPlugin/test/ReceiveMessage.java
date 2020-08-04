package com.aadata.activemqPlugin.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiveMessage {
	
	public static void main(String[] args) throws Exception {
		// 第一步：创建一个ConnectionFactory对象。
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");
				// 第二步：从ConnectionFactory对象中获得一个Connection对象。
				Connection connection = connectionFactory.createConnection();
				// 第三步：开启连接。调用Connection对象的start方法。
				connection.start();
				// 第四步：使用Connection对象创建一个Session对象。
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
				Topic topic = session.createTopic("test");
				// 第六步：使用Session对象创建一个Consumer对象。
				MessageConsumer consumer = session.createConsumer(topic);
				// 第七步：接收消息。
				consumer.setMessageListener(new MessageListener() {
					
					public void onMessage(Message message) {
						try {
							TextMessage textMessage = (TextMessage) message;
							String text = null;
							//取消息的内容
							text = textMessage.getText();
							// 第八步：打印消息。
							System.out.println(text);
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				});
				//等待键盘输入 避免直接关闭
				System.in.read();
				// 第九步：关闭资源
				consumer.close();
				session.close();
				connection.close();

	}
}
