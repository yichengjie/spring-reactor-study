package com.yicj.study.mq.method2;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ReceiverQueue {

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
        //5.消息消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //6.从目的地获取消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("接收到一条消息");
                    System.out.println("开始发送确认消息");
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    producer.send(session.createTextMessage("xxxx..."));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read() ;
    }
}
