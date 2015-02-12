package com.howbuy.onlinecalc.function;

import java.util.ArrayList;
import java.util.List;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSONArray;
import com.howbuy.onlinecalc.utils.Dimension;
import com.howbuy.onlinecalc.utils.FundUtils;
import com.howbuy.onlinecalc.utils.GmProfitLost;

@SuppressWarnings("serial")
public class FundProfitLostFunc extends BaseFunction {
	public void execute(TridentTuple tuple, TridentCollector collector) {
		try {
			String fundCode = tuple.getString(0);
			String clrq = tuple.getString(1);
			String start = tuple.getString(2);
			String end = tuple.getString(3);
			List<GmProfitLost> list = new ArrayList<GmProfitLost>();
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.MONTH1));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.MONTH3));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.MONTH6));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.YEAR1));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.YEAR2));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.YEAR3));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.CURYEAR));
			list.addAll(FundUtils.getGmProfitLosts(fundCode, clrq, start, end, Dimension.ALL));
			JSONArray jsonarr = new JSONArray();
			jsonarr.addAll(list);
			String jsonstr = jsonarr.toJSONString();
			System.out.println("json string is : " + jsonstr);
			collector.emit(new Values(jsonstr));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
