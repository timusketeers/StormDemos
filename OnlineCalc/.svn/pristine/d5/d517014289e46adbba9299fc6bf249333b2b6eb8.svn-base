package com.howbuy.onlinecalc.bolt;

import java.util.Map;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * 这个bolt负责从不同的维度分发到下一个不同维度的计算节点.
 * 
 * 例如，从这个节点会同时将一个fundCode元组分发到各个维度的计算单元
 * 
 * (包括一个月盈亏的计算单元、 三个月盈亏的计算单元、 6个月盈亏的计算单元.)
 * @author li.zhang
 *
 */
@SuppressWarnings("serial")
public class DimensionSplitBolt extends BaseRichBolt
{

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext ctx, OutputCollector collector)
    {
        
    }

    public void execute(Tuple input)
    {
        
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        
    }
}
