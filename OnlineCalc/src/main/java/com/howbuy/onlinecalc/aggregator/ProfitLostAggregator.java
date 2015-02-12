package com.howbuy.onlinecalc.aggregator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.howbuy.onlinecalc.redis.JedisPoolUtils;
import com.howbuy.onlinecalc.utils.GmProfitLost;

@SuppressWarnings("serial")
public class ProfitLostAggregator implements Aggregator {
	private Set<String> records = null;

	@SuppressWarnings("rawtypes")
	public void prepare(Map conf, TridentOperationContext context) {
		this.records = new HashSet<String>();
	}

	public void cleanup() {
		this.records = null;
	}

	public String init(Object batchId, TridentCollector collector) {
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
			JSONArray jsonarr = JSONArray.parseArray(record);
			for (int i = 0; i < jsonarr.size(); i++) {
				GmProfitLost obj = JSON.parseObject(jsonarr.getJSONObject(i).toJSONString(), GmProfitLost.class);
				sb.append(obj.getJjdm());
				sb.append(",");
				sb.append(obj.getJzrq());
				sb.append(",");
				sb.append(obj.getZdyl());
				sb.append(",");
				sb.append(obj.getZdhc());
				sb.append(",");
				sb.append(obj.getZblb());
				sb.append(";");
			}
		}
		System.out.println(sb.toString());
		/*Jedis jedis = JedisPoolUtils.getJedis();
		String key = UUID.randomUUID().toString();
		jedis.set(key, sb.toString());
		jedis.expire(key, 12 * 60 * 60);
		JedisPoolUtils.returnRes(jedis);*/
		collector.emit(new Values(sb.toString()));
	}
}
