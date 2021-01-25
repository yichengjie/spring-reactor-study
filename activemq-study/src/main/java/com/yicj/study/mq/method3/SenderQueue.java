package com.yicj.study.mq.method3;

import org.apache.activemq.ActiveMQConnectionFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import javax.jms.*;
import java.util.UUID;
import java.util.function.Consumer;

public class SenderQueue {

	private static ThreadLocal<Connection> local = new ThreadLocal<>() ;

	public Session createSession() throws JMSException {
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
		local.set(connection);
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}


	public Mono send(String msg, Session session) throws JMSException {
		//4.找目的地，获取destination，消费端也会从这个目的地取消息
		Queue queue = session.createQueue("user");
		//Topic topic = session.createTopic("ff");
		//5.消息生产者
		MessageProducer producer = session.createProducer(queue);
		//设置消息是否持久化 默认是持久化的
		//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		Message textMessage = session.createTextMessage(msg);
		String cid=UUID.randomUUID().toString();
		textMessage.setJMSCorrelationID(cid);
		textMessage.setStringProperty("type","C");
		producer.send(textMessage);
		System.out.println("P 消息发送完毕");
		//等待消息回复
		MessageConsumer consumer = session.createConsumer(queue,"JMSCorrelationID='"+cid+"' and type='P'");
		return Mono.create(new Consumer<MonoSink<String>>() {
			@Override
			public void accept(MonoSink<String> sink) {
				try {
					consumer.setMessageListener(new MessageListener() {
						@Override
						public void onMessage(Message message) {
							sink.success("P 收到消息确认");
						}
					});
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}) ;
	}


	public static void main(String[] args) throws Exception{
		SenderQueue senderQueue = new SenderQueue() ;
		Session session = senderQueue.createSession();
		Mono mono = senderQueue.send("hello world", session);
		mono.subscribe(s -> {
			System.out.println("=======> " + s) ;
			Connection connection = local.get();
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}) ;
	}
 
}