package com.yicj.study.mq.method3;

import org.apache.activemq.ActiveMQConnectionFactory;
import reactor.core.publisher.Mono;

import javax.jms.*;
import java.util.UUID;

public class SenderQueue {

	private static ThreadLocal<Connection> local = new ThreadLocal<>() ;

	public static void main(String[] args) throws Exception{
		//1.获取连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin",
				"admin",
				"tcp://192.168.221.128:61616"
		);
		//2.获取一个向ActiveMQ的连接
		Connection connection = connectionFactory.createConnection();
		connection.start();
		//3.获取session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//4.找目的地，获取destination，消费端也会从这个目的地取消息
		Queue queue = session.createQueue("user");
		//Topic topic = session.createTopic("ff");

		//5.消息生产者
		MessageProducer producer = session.createProducer(queue);
		//设置消息是否持久化 默认是持久化的
		//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		Message textMessage = session.createTextMessage("hi");
		String cid=UUID.randomUUID().toString();
		textMessage.setJMSCorrelationID(cid);
		textMessage.setStringProperty("type","C");


		producer.send(textMessage);
		System.out.println("P 消息发送完毕");

		//等待消息回复
		MessageConsumer consumer = session.createConsumer(queue,"JMSCorrelationID='"+cid+"' and type='P'");
		Mono.create(monoSink -> {
			try {
				consumer.setMessageListener(message -> {
					try {
						//7.关闭连接
						monoSink.success("P 收到消息确认");
						connection.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				});
			}catch (Exception e){
				e.printStackTrace();
			}
		}).subscribe(value ->{
			System.out.println("==========> value : " + value);
		}) ;

	}
}