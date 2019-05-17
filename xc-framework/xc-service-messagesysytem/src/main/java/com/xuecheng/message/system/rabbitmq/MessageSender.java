package com.xuecheng.message.system.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class MessageSender {
	
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;
	
	
	public void sendMessage(String exchange, String queue, String routingKey, String message) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setHost(host);
			connectionFactory.setPort(port);
			connectionFactory.setUsername(username);
			connectionFactory.setPassword(password);
			connectionFactory.setVirtualHost(virtualHost);
			
			connection = connectionFactory.newConnection();
			//创建与交换机交流的通道
			channel = connection.createChannel();
			/**
			 * 创建交换机,参数明细
			 * @Param 1.交换机名称
			 * @param 2.交换机类型 fanout、topic、direct、headers
			 */
			channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
			/**
			 * 参数明细：
			 * 1、队列名称
			 * 2、是否持久化
			 * 3、是否独占此队列
			 * 4、队列不用是否自动删除
			 * 5、参数
			 */
			channel.queueDeclare(queue, true, false, false, null);
			/**
			 * 交换机绑定队列
			 * 参数明细
			 * 1、队列名称
			 * 2、交换机名称
			 * 3、路由key
			 */
			channel.queueBind(queue,exchange,routingKey);
			/**
			 * 参数明细
			 * 1、交换机名称，不指令使用默认交换机名称 Default Exchange
			 * 2、routingKey（路由key），根据key名称将消息转发到具体的队列，这里填写队列名称表示消
			息将发到此队列
			 * 3、消息属性
			 * 4、消息内容
			 */
			channel.basicPublish(exchange, routingKey, null, message.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}
	}
}
