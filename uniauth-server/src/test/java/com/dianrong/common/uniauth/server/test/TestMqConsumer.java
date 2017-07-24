package com.dianrong.common.uniauth.server.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

/**
 * 消费MQ消息.
 *
 * @author wanglin
 */
public class TestMqConsumer {

  private static final String QUEUE_NAME = "uniauth.develop";

  /**
   * 测试方法入口.
   */
  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setVirtualHost("/msgbus");
    factory.setHost("127.0.0.1");
    factory.setPort(5672);
    factory.setUsername("administrator");
    factory.setPassword("password");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(message);
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }

}
