package com.howbuy.onlinecalc.spout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.howbuy.onlinecalc.sequence.MessageIdSeq;
import com.howbuy.onlinecalc.utils.FundUtils;
import com.howbuy.onlinecalc.utils.FundVo;

/**
 * 如果外部有消息发送过来要求我们计算所有基金的最大盈亏的时候，我们才开始计算，我们的拓扑才往下走.
 * 
 * @author li.zhang
 * 
 */
@SuppressWarnings("serial")
public class TriggerCalcSpout extends BaseRichSpout {
	private Map<?, ?> conf;

	private TopologyContext ctx;

	private SpoutOutputCollector collector;

	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext ctx, SpoutOutputCollector collector) {
		this.ctx = ctx;
		this.collector = collector;
		this.conf = conf;
	}

	public void nextTuple() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String topic = "calc_cmd_topic";
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(createConsumerConfig());

		// topickMap描述读取哪个topic，需要几个线程读.
		int threadNum = 1;
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		topicMap.put(topic, threadNum);

		Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer.createMessageStreams(topicMap);
		List<KafkaStream<byte[], byte[]>> streams = streamMap.get(topic);// 每个线程对应于一个KafkaStream
		KafkaStream<byte[], byte[]> stream = streams.get(0);// 这里因为上面topicMap中设置的线程数是1.所以直接取第一个元素.可参考docs目录下的资料.
		ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

		/**
		 * 当没有消息的时候iterator.hasNext()方法会阻塞在这里,考虑到这种阻塞,
		 * 
		 * 为了防止cpu占用率高，所以在这里建议线程休眠一段时间.
		 */
		while (iterator.hasNext()) {
			String message = new String(iterator.next().message());
			System.err.println("get data:" + message);

			// 计算单个基金.
			long messageId = MessageIdSeq.nextSeq();
			if (!"all".equals(message)) {
				List<String> fundcode = new ArrayList<String>();
				fundcode.add(message);
				// emit的时候如果没有指定messageId这个参数，将不会触发ack()和fail()机制.参见emit()方法的API.
				collector.emit(new Values(fundcode), messageId);
			}
			// 计算所有基金.
			else {
				// 按照每个不同的基金分发到下一个拓扑计算节点.
				List<FundVo> fundVos = FundUtils.getFundInfo();
				List<String> fundCodes = new ArrayList<String>();
				for (FundVo vo : fundVos) {
					fundCodes.add(vo.getJjdm());
				}
				Values tuple = new Values(fundCodes);
				collector.emit(tuple, messageId);
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("fundcode"));
	}

	/**
	 * storm框架是对每个"元组tuple"的执行结果进行确认成功或者失败的，所以上面将所有基金作为一个元组发送出去.
	 */
	@Override
	public void ack(Object msgId) {
		// 所有基金都成功处理完成...
		System.out.println(msgId + ": process success.....");
	}

	/**
	 * storm框架是对每个"元组tuple"的执行结果进行确认成功或者失败的，所以上面将所有基金作为一个元组发送出去.
	 */
	@Override
	public void fail(Object msgId) {
		// 中途处理失败.
		System.out.println(msgId + ": encounter error.....");

	}

	private ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", "zookeeper:12181,zookeeper:22181,zookeeper:32181");
		props.put("group.id", "1");
		props.put("auto.commit.interval.ms", "10000");
		props.put("zookeeper.session.timeout.ms", "10000");
		return new ConsumerConfig(props);
	}

	public Map<?, ?> getConf() {
		return conf;
	}

	public TopologyContext getCtx() {
		return ctx;
	}

	public SpoutOutputCollector getCollector() {
		return collector;
	}
}
