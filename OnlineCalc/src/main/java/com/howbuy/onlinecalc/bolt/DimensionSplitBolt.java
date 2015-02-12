package com.howbuy.onlinecalc.bolt;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * ���bolt����Ӳ�ͬ��ά�ȷַ�����һ����ͬά�ȵļ���ڵ�.
 * 
 * ���磬������ڵ��ͬʱ��һ��fundCodeԪ��ַ�������ά�ȵļ��㵥Ԫ
 * 
 * (����һ����ӯ���ļ��㵥Ԫ�� ������ӯ���ļ��㵥Ԫ�� 6����ӯ���ļ��㵥Ԫ.)
 * 
 * @author li.zhang
 * 
 */
@SuppressWarnings("serial")
public class DimensionSplitBolt extends BaseRichBolt {
	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext ctx, OutputCollector collector) {
		this.collector = collector;
	}

	@SuppressWarnings("unchecked")
	public void execute(Tuple tuple) {
		System.out.println("[DimensionSplitBolt]=================");
		List<String> fundcodes = (List<String>) tuple.getValues().get(0);
		for (int i = 0; i < fundcodes.size(); i++) {
			Object fundcode = fundcodes.get(i);
			collector.emit(tuple, new Values(fundcode));
		}

		collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("fundcode"));
	}
}