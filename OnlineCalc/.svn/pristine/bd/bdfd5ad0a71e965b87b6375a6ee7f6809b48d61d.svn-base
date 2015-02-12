package com.howbuy.onlinecalc.bolt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.howbuy.onlinecalc.utils.Dimension;

@SuppressWarnings("serial")
public class CalcJoinBolt extends BaseRichBolt
{
    /** 表示1个月，3个月，6个月共三个维度的join.**/
    private static final int JOIN_DIMENSION_NUM = 3;
    
    private List<Tuple> anchors;
    
    private OutputCollector collector;
    
    /** key为fundcode, value为map, value中map key为dimension, value为最大盈利和最大回撤. **/
    private Map<String, Map<String, double[]>> fundProfits;
    
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext ctx, OutputCollector collector)
    {
        this.collector = collector;
        this.fundProfits = new HashMap<String, Map<String,double[]>>();
        this.anchors = new ArrayList<Tuple>();
    }

    @SuppressWarnings("unchecked")
    public void execute(Tuple tuple)
    {
        System.out.println("[CalcJoinBolt:] enter execute()...." + tuple.getSourceComponent() + ":" + tuple.getSourceStreamId());
        
        //多重锚定..
        this.anchors.add(tuple);
        
        String fundcode = tuple.getString(0);
        Map<String, double[]> tupleMap = (Map<String, double[]>)tuple.getValue(1);
        Map.Entry<String, double[]> entry = tupleMap.entrySet().iterator().next();
        
        String dimension = entry.getKey();
        double[] profitlost = entry.getValue();
        
        Map<String, double[]> map = fundProfits.get(fundcode);
        if (null == map)
        {
            map = new HashMap<String, double[]>();
            map.putAll(tupleMap);
            fundProfits.put(fundcode, map);
        }
        
        if (null == map.get(dimension) || 0 == map.get(dimension).length)
        {
            map.put(dimension, profitlost);
        }
        
        StringBuilder message = new StringBuilder();
        
        //当一个基金的三个维度都算完后，才开始向下个bolt分发消息.
        if (JOIN_DIMENSION_NUM == map.keySet().size())
        {
            double[][] profitlosts = new double[3][2];
            profitlosts[0] = map.get(Dimension.MONTH1.name());
            profitlosts[1] = map.get(Dimension.MONTH3.name());
            profitlosts[2] = map.get(Dimension.MONTH6.name());
            
            message.append("[").append(fundcode).append("]").append(":")
                   .append("[")
                   .append(profitlosts[0][0]).append(",")
                   .append(profitlosts[0][1]).append(",")
                   .append(profitlosts[1][0]).append(",")
                   .append(profitlosts[1][1]).append(",")
                   .append(profitlosts[2][0]).append(",")
                   .append(profitlosts[2][1]).append(",")
                   .append("]");
            
            System.out.println("[CalcJoinBolt]:" + message);
            
            collector.emit(this.anchors, new Values(fundcode, 
                    profitlosts[0][0], profitlosts[0][1],
                    profitlosts[1][0], profitlosts[1][1],
                    profitlosts[2][0], profitlosts[2][1]));
            
            for (int i = 0; i < this.anchors.size(); i++)
            {
                collector.ack(this.anchors.get(i));
            }
            
            /**
             * 这句需要注意, 这句很重要。因为提交jar到storm集群上后，当第一次发送消息告知需要计算482002这个
             * 基金的时候，我们整个topology上各个节点上的任务都会走一遍，导致CalcJoinBolt中的fundProfits这个
             * 成员变量中有482002这个基金三个维度上的计算结果...如果此时再次发消息告知需要计算482002这个基金的
             * 时候，走到CalcJoinBolt这个工作节点时，因为它的fundProfits成员变量中在第一次的时候已经缓存了482002
             * 这个基金三个粒度的计算结果。所以我们此时在CalcJoinBolt中没有起到阻塞等待的效果，导致三个维度过来的消息
             * 都会通过if (JOIN_DIMENSION_NUM == map.keySet().size())这个条件，从而导致下个bolt执行三次。所以我们会
             * 看到482002三个维度的结果会重复返回三条记录.慎之慎之!
             */
            fundProfits.remove(fundcode);
            
            anchors.clear();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("fundcode", 
                "monthly-profit", "monthly-lost",
                "tri-month-profit", "tri-month-lost",
                "half-year-profit", "half-year-lost"));
    }
}
