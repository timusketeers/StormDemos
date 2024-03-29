package com.howbuy.onlinecalc.function;

import java.util.List;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

import com.howbuy.onlinecalc.utils.FundUtils;
import com.howbuy.onlinecalc.utils.FundVo;
import com.howbuy.onlinecalc.utils.StringUtils;

@SuppressWarnings("serial")
public class FundSplitFunc extends BaseFunction {
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String args = tuple.getStringByField("args");
		System.out.println("args is : " + args);
		String start = "";
		String end = "";
		if (StringUtils.isNotBlank(args)) {
			start = args.split(",")[0];
			end = args.split(",")[1];
		}
		List<FundVo> fundCodes = FundUtils.getFundInfo();
		for (int i = 0; i < fundCodes.size(); i++) {
			FundVo vo = fundCodes.get(i);
			collector.emit(new Values(vo.getJjdm(), vo.getClrq(), start, end,1));
			collector.emit(new Values(vo.getJjdm(), vo.getClrq(), start, end,2));
			collector.emit(new Values(vo.getJjdm(), vo.getClrq(), start, end,3));
		}
	}

	
}
