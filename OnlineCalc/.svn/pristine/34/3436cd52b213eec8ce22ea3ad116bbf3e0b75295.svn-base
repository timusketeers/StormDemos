package com.howbuy.onlinecalc.function;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import com.howbuy.onlinecalc.utils.FundService;

@SuppressWarnings("serial")
public class SmjlProfitLostFunc extends BaseFunction {
	public void execute(TridentTuple tuple, TridentCollector collector) {
		try {
			String rydm = tuple.getString(0);
			String jjdm = tuple.getString(1);
			String qsrq = tuple.getString(2);
			String jsrq = tuple.getString(3);
			double[] result = FundService.caclRealSm(jjdm, qsrq, jsrq);
			collector.emit(new Values(jjdm, rydm, result[0], result[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
