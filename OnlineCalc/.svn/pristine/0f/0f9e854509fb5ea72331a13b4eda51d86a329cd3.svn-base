package com.howbuy.onlinecalc.bolt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.howbuy.onlinecalc.utils.Dimension;
import com.howbuy.onlinecalc.utils.FundUtils;

@SuppressWarnings("serial")
public class TriMonthCalcBolt extends BaseRichBolt
{
    private OutputCollector collector;
    
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext ctx, OutputCollector collector)
    {
        this.collector = collector;
    }

    public void execute(Tuple tuple)
    {
        System.out.println(tuple.getString(0) + "[TriMonthCalcBolt]=================");
        String fundCode = tuple.getString(0);
        double[] result = FundUtils.calcProfitLost(new Date(),new Date(),fundCode, Dimension.MONTH3);
        double maxprofit = result[0];//最大赢利
        double maxlost = result[1];//最大回撤
        
        Map<String, double[]> map = new HashMap<String, double[]>();
        double[] profitlost = new double[2];
        profitlost[0] = maxprofit;
        profitlost[1] = maxlost;
        
        map.put(Dimension.MONTH3.name(), profitlost);

        recordLog(fundCode, result);
        
        collector.emit(tuple, new Values(fundCode, map));
        collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("fundcode", "dimension-profit-lost"));
    }

    private void recordLog(String fundCode, double[] profitlost)
    {
        StringBuilder profitMsg = new StringBuilder();
        StringBuilder lostMsg = new StringBuilder();
        profitMsg.append("[").append(fundCode).append("]:")
                 .append("[").append(Dimension.MONTH3.name()).append("]")
                 .append("最大赢利:").append(profitlost[0]);
        
        lostMsg.append("[").append(fundCode).append("]:")
                 .append("[").append(Dimension.MONTH3.name()).append("]")
                 .append("最大回撤:").append(profitlost[1]);
        
        System.out.println(profitMsg.toString());
        System.out.println(lostMsg.toString());
    }
}
