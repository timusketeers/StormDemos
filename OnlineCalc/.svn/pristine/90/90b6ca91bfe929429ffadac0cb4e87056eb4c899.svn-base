package com.howbuy.onlinecalc.spout;

import java.util.List;
import java.util.Map;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.howbuy.onlinecalc.utils.FundUtils;

/**
 * 如果外部有消息发送过来要求我们计算所有基金的最大盈亏的时候，我们才开始计算，我们的拓扑才往下走.
 * @author li.zhang
 *
 */
@SuppressWarnings("serial")
public class TriggerCalcSpout extends BaseRichSpout
{
    private Map<?, ?> conf;
    
    private TopologyContext ctx;
    
    private SpoutOutputCollector collector;
    
    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext ctx, SpoutOutputCollector collector)
    {
        this.ctx = ctx;
        this.collector = collector;
        this.conf = conf;
    }

    public void nextTuple()
    {
        while (!receiveCalcCmd())
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //按照每个不同的基金分发到下一个拓扑计算节点.
        List<String> fundCodes = FundUtils.getAllFundCode();
        for (int i = 0; i < fundCodes.size(); i++)
        {
            Values tuple = new Values(fundCodes.get(i));
            collector.emit(tuple);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("fundcode"));
    }
    
    /**
     * 判断是否接收到计算基金盈亏的请求..
     * @return
     */
    private boolean receiveCalcCmd()
    {
        boolean received = false;
        
        return received;
    }

    public Map<?, ?> getConf()
    {
        return conf;
    }

    public TopologyContext getCtx()
    {
        return ctx;
    }

    public SpoutOutputCollector getCollector()
    {
        return collector;
    }
}
