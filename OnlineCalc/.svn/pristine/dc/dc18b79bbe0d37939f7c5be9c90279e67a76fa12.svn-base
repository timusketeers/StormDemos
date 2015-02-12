package com.howbuy.onlinecalc.function;

import java.util.List;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

import com.alibaba.fastjson.JSON;
import com.howbuy.onlinecalc.utils.FundService;
import com.howbuy.onlinecalc.utils.FundUtils;
import com.howbuy.onlinecalc.utils.SmProfitLost;
import com.howbuy.onlinecalc.utils.Smjr;

@SuppressWarnings("serial")
public class SingleSmjlProfitLostFunc extends BaseFunction {
	public void execute(TridentTuple tuple, TridentCollector collector) {
		try {
			String rydm = tuple.getString(0);
			List<Smjr> smjrs = FundUtils.getSmjrList(rydm);
			double[] zdyls = new double[smjrs.size()];
			double[] zdhcs = new double[smjrs.size()];
			int index = 0;
			for (Smjr smjr : smjrs) {
				double[] result = FundService.caclRealSm(smjr.getJjdm(), smjr.getQsrq(), smjr.getJsrq());
				zdyls[index] = result[0];
				zdhcs[index] = result[1];
				index++;
			}
			double max = 0;
			double min = 0;
			for (int i = 0; i < zdyls.length; i++) {
				if (max < zdyls[i]) {
					max = zdyls[i];
				}
			}
			for (int i = 0; i < zdhcs.length; i++) {
				if (min > zdhcs[i]) {
					min = zdhcs[i];
				}
			}
			SmProfitLost vo = new SmProfitLost();
			vo.setRydm(rydm);
			vo.setZdyl(max);
			vo.setZdhc(min);
			collector.emit(new Values(JSON.toJSONString(vo)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
