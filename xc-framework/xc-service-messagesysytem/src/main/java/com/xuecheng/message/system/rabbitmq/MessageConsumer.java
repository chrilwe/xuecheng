package com.xuecheng.message.system.rabbitmq;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.xuecheng.framework.common.client.XcServiceList;
import com.xuecheng.framework.common.exception.ExceptionCast;
import com.xuecheng.framework.common.model.response.ResponseResult;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.message.Message;
import com.xuecheng.framework.domain.message.response.MessageCode;
import com.xuecheng.message.system.client.CourseClient;
import com.xuecheng.message.system.client.SearchClient;

@Component
public class MessageConsumer {
	
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
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CourseClient courseClient;
	@Autowired
	private SearchClient searchClient;
	
	/**
	 * 分布式事务
	 * 完成课程发布状态后保证课程信息最终添加到搜索库中
	 * @param exchange
	 * @param queue
	 * @param routingKey
	 */
	public void addCourseToEsListener(String exchange,String queue,String routingKey) {
		try {
			Channel channel = this.channel(exchange, queue, routingKey);
			DefaultConsumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String messageId = new String(body,"utf-8");
					//调用消息服务，根据messageId查询消息
					ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_MESSAGESYSTEM);
					URI uri = choose.getUri();
					String url = uri + "/message/system/findById/"+messageId;
					
					ResponseEntity<Message> response = restTemplate.getForEntity(url, Message.class);
					Message message = response.getBody();
					if(message == null) {
						ExceptionCast.cast(MessageCode.MESSAGE_ISNOT_EXISTS);
					}
					//将课程id取出来，调用课程服务查询课程信息
					String courseId = message.getMessageBody();
					CoursePub coursePub = courseClient.findCoursePubByCourseId(courseId);
					//调用搜索服务，将发布课程信息添加到索引库
					ResponseResult result = searchClient.addCourseToEs(coursePub);
				}
			};
			/**
			         * 监听队列String queue, boolean autoAck,Consumer callback
			         * 参数明细
			         * 1、队列名称
			         * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
			为false则需要手动回复
			         * 3、消费消息的方法，消费者接收到消息后调用此方法
			         */
			channel.basicConsume(queue,true,consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Channel channel(String exchange,String queue,String routingKey) {
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channel;
	}
}
