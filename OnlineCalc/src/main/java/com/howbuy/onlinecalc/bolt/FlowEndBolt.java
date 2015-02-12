package com.howbuy.onlinecalc.bolt;

import java.util.Map;

import com.howbuy.onlinecalc.utils.KafkaMsgSender;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * 一个基金处理结束的bolt.
 * @author li.zhang
 *
 */
@SuppressWarnings("serial")
public class FlowEndBolt extends BaseRichBolt
{
    private OutputCollector collector;
    
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext ctx, OutputCollector collector)
    {
        this.collector = collector;
    }

    public void execute(Tuple tuple)
    {
        System.out.println("[FlowEndBolt:] enter execute()....");
        String fundcode = tuple.getString(0);
        double monthlyProfit = tuple.getDouble(1);
        double monthlyLost = tuple.getDouble(2);
        double triMonthProfit = tuple.getDouble(3);
        double triMonthLost = tuple.getDouble(4);
        double halfYearProfit = tuple.getDouble(5);
        double halfYearLost = tuple.getDouble(6);
        
        StringBuilder message = new StringBuilder();
        message.append(fundcode).append("\t")
               .append(monthlyProfit).append("\t")
               .append(monthlyLost).append("\t")
               .append(triMonthProfit).append("\t")
               .append(triMonthLost).append("\t")
               .append(halfYearProfit).append("\t")
               .append(halfYearLost);
        
        KafkaMsgSender.dispatch("calc_result_notify", message.toString());
        
        //走到这里则确认元组处理成功..
        collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        
    }
}
