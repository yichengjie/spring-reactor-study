package com.yicj.study.mq.method1;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

//QueueRequestor
public class SenderQueue {
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
        QueueSession session = (QueueSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.找目的地，获取destination，消费端也会从这个目的地取消息
        Queue queue = session.createQueue("user");
        //Topic topic = session.createTopic("ff");
        //5.消息生产者
        QueueRequestor queueRequestor=new QueueRequestor(session,queue);
        Message textMessage = session.createTextMessage("hi");
        TextMessage responseMessage = (TextMessage) queueRequestor.request(textMessage);
        System.out.println(responseMessage.getText());
        //7.关闭连接
        //connection.close();
    }

}
