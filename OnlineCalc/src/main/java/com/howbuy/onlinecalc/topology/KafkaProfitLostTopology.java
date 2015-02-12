package com.howbuy.onlinecalc.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.howbuy.onlinecalc.bolt.CalcJoinBolt;
import com.howbuy.onlinecalc.bolt.DimensionSplitBolt;
import com.howbuy.onlinecalc.bolt.FlowEndBolt;
import com.howbuy.onlinecalc.bolt.HalfYearCalcBolt;
import com.howbuy.onlinecalc.bolt.MonthlyCalcBolt;
import com.howbuy.onlinecalc.bolt.TriMonthCalcBolt;
import com.howbuy.onlinecalc.spout.TriggerCalcSpout;

/**
 * 基金盈亏计算的拓扑.
 * @author li.zhang
 *
 */
public class KafkaProfitLostTopology
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        /**
         * storm的分组策略grouping只是决定：在哪个节点的哪个任务上执行这个bolt, 并不决定拓扑图中的这个bolt执行不执行;事实上，
         * 
         * 拓扑图上的每个bolt总是会被执行的.
         */
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("trigger-calc", new TriggerCalcSpout());
        builder.setBolt("dimension_split", new DimensionSplitBolt()).shuffleGrouping("trigger-calc");
        
        /**
         * shuffle-grouping是将dimension_split过来的tuple随机分发到MonthlyCalcBolt的50个task中的任意一个.
         * all-grouping是将dimension_split过来的tuple发送到MonthlyCalcBolt所有50个task中.
         */
        builder.setBolt("monthly_calc", new MonthlyCalcBolt(), 50).shuffleGrouping("dimension_split");    //一个月的基金最大盈亏计算.
        builder.setBolt("tri_month_calc", new TriMonthCalcBolt(), 50).shuffleGrouping("dimension_split"); //三个月的基金最大盈亏计算.
        builder.setBolt("half_year_calc", new HalfYearCalcBolt(), 50).shuffleGrouping("dimension_split"); //半年的基金最大盈亏计算.
        
        //三个粒度的执行结果join.
        builder.setBolt("calc_join", new CalcJoinBolt(), 50)
                    .fieldsGrouping("monthly_calc", new Fields("fundcode"))
                    .fieldsGrouping("tri_month_calc", new Fields("fundcode"))
                    .fieldsGrouping("half_year_calc", new Fields("fundcode"));
        
        
        //流程结束..
        builder.setBolt("flow-end", new FlowEndBolt()).shuffleGrouping("calc_join");
        
        Config conf = new Config();
        conf.setNumWorkers(2);
        conf.setDebug(true);
        
        try
        {
            if (null == args || 0 >= args.length)
            {
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("calc_fund_profit_lost", conf, builder.createTopology());
                Thread.sleep(1000);
                cluster.shutdown();
            }
            else
            {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
