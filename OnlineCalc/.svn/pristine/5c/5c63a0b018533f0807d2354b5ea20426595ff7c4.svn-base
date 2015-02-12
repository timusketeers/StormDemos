package com.howbuy.onlinecalc.function;

import java.util.Set;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import com.howbuy.onlinecalc.utils.FundUtils;

@SuppressWarnings("serial")
public class SmjlRydmSplitFunc extends BaseFunction {
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Set<String> rydms = FundUtils.getSmjlRydm();
		for (String rydm : rydms) {
			collector.emit(new Values(rydm));
		}
	}
}
