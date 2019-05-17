package com.xuecheng.service.topology.spout;

import java.util.Map;
import java.util.Properties;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;

import kafka.consumer.ConsumerConfig;

public class TopNSpout extends BaseRichSpout {
	
	private SpoutOutputCollector collector;
	
	/**
	 * 初始化spout
	 */
	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}
	
	/**
	 * 解析数据，并将数据发送到blot
	 */
	@Override
	public void nextTuple() {
		
	}
	
	/**
	 * kafaka消费端，采集数据
	 */
	private class KafakaConsumer {
		
		private void getMessage() {
			Properties props = new Properties();
			props.put("zookeeper.connect", "192.168.43.51:2181,192.168.43.104:2181,192.168.43.126:2181");
			props.put("group.id", "taotao-itemCount-group");
	        props.put("zookeeper.session.timeout.ms", "60000");
	        props.put("zookeeper.sync.time.ms", "2000");
	        props.put("auto.commit.interval.ms", "1000");
	        
	        ConsumerConfig config = new ConsumerConfig(props);
		}
	}

}
