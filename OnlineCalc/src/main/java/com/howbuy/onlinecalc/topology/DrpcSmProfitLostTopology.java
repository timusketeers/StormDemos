package com.howbuy.onlinecalc.topology;

import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.tuple.Fields;

import com.howbuy.onlinecalc.aggregator.SmProfitLostAggregator;
import com.howbuy.onlinecalc.function.SingleSmjlProfitLostFunc;
import com.howbuy.onlinecalc.function.SmjlRydmSplitFunc;

/**
 * ˽ļ�������ӯ�����������.
 * 
 * @author li.zhang
 * 
 */
public class DrpcSmProfitLostTopology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * storm�ķ������groupingֻ�Ǿ��������ĸ��ڵ���ĸ�������ִ�����bolt, ������������ͼ�е����boltִ�в�ִ��;��ʵ�ϣ�
		 * 
		 * ����ͼ�ϵ�ÿ��bolt���ǻᱻִ�е�.
		 */
		String functionName = "FDC_SM_JS_JRRQZDYLZDHC";
		TridentTopology builder = new TridentTopology();
		builder.newDRPCStream(functionName)
	           .each(new Fields("args"),new SmjlRydmSplitFunc(),new Fields("rydm"))
	           .shuffle()
	           .parallelismHint(50)
	           .each(new Fields("rydm"), new SingleSmjlProfitLostFunc(),new Fields("single-smjl-profit-lost-rslt"))
	           .aggregate(new Fields("single-smjl-profit-lost-rslt"), new SmProfitLostAggregator(), new Fields("joined-smjl-profit-lost-rslt"))
	           .project(new Fields("joined-smjl-profit-lost-rslt"));
		Config conf = new Config();
		conf.setNumWorkers(2);
		conf.setDebug(true);

		try {
			if (null == args || 0 >= args.length) {
				LocalCluster cluster = new LocalCluster();
				cluster.submitTopology("calc_fund_profit_lost", conf, builder.build());
				Thread.sleep(1000);
				cluster.shutdown();
			} else {
				StormSubmitter.submitTopology(args[0], conf, builder.build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
