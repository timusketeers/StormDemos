package com.howbuy.onlinecalc.aggregator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSON;
import com.howbuy.onlinecalc.redis.JedisPoolUtils;
import com.howbuy.onlinecalc.utils.SmProfitLost;
import redis.clients.jedis.Jedis;
import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

@SuppressWarnings("serial")
public class SmProfitLostAggregator implements Aggregator {
	private Set<String> records = null;

	public void prepare(Map conf, TridentOperationContext context) {
		this.records = new HashSet<String>();
	}

	public void cleanup() {
		this.records = null;
	}

	public Object init(Object batchId, TridentCollector collector) {
		return null;
	}

	public void aggregate(Object val, TridentTuple tuple, TridentCollector collector) {
		String record = tuple.getString(0);
		records.add(record);
	}

	public void complete(Object val, TridentCollector collector) {
		if (null == records) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		for (String record : records) {
			SmProfitLost obj = JSON.parseObject(record, SmProfitLost.class);
			sb.append(obj.getRydm());
			sb.append(",");
			sb.append(obj.getZdyl());
			sb.append(",");
			sb.append(obj.getZdhc());
			sb.append(";");
		}
		/*Jedis jedis = JedisPoolUtils.getJedis();
		String key = UUID.randomUUID().toString();
		jedis.set(key, sb.toString());
		jedis.expire(key, 12 * 60 * 60);
		JedisPoolUtils.returnRes(jedis);*/
		collector.emit(new Values(sb.toString()));
	}

}
