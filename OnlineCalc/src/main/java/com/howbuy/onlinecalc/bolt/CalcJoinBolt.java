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
    /** ��ʾ1���£�3���£�6���¹�����ά�ȵ�join.**/
    private static final int JOIN_DIMENSION_NUM = 3;
    
    private List<Tuple> anchors;
    
    private OutputCollector collector;
    
    /** keyΪfundcode, valueΪmap, value��map keyΪdimension, valueΪ���ӯ�������س�. **/
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
        
        //����ê��..
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
        
        //��һ�����������ά�ȶ�����󣬲ſ�ʼ���¸�bolt�ַ���Ϣ.
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
             * �����Ҫע��, ������Ҫ����Ϊ�ύjar��storm��Ⱥ�Ϻ󣬵���һ�η�����Ϣ��֪��Ҫ����482002���
             * �����ʱ����������topology�ϸ����ڵ��ϵ����񶼻���һ�飬����CalcJoinBolt�е�fundProfits���
             * ��Ա��������482002�����������ά���ϵļ�����...�����ʱ�ٴη���Ϣ��֪��Ҫ����482002��������
             * ʱ���ߵ�CalcJoinBolt��������ڵ�ʱ����Ϊ����fundProfits��Ա�������ڵ�һ�ε�ʱ���Ѿ�������482002
             * ��������������ȵļ��������������Ǵ�ʱ��CalcJoinBolt��û���������ȴ���Ч������������ά�ȹ�������Ϣ
             * ����ͨ��if (JOIN_DIMENSION_NUM == map.keySet().size())����������Ӷ������¸�boltִ�����Ρ��������ǻ�
             * ����482002����ά�ȵĽ�����ظ�����������¼.��֮��֮!
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
