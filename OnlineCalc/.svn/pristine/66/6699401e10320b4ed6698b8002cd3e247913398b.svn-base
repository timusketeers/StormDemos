package com.howbuy.onlinecalc.topology;

import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.tuple.Fields;

import com.howbuy.onlinecalc.aggregator.ProfitLostAggregator;
import com.howbuy.onlinecalc.function.FundProfitLostFunc;
import com.howbuy.onlinecalc.function.FundSplitFunc;

/**
 * 基金盈亏计算的拓扑.
 * 
 * @author li.zhang
 * 
 */
public class DrpcProfitLostTopology
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
        String functionName = "calc";
        TridentTopology builder = new TridentTopology();
        builder.newDRPCStream(functionName)
               //这里的new Fields("args")在trident drpc的时候必须这样写，因为builder.newDrpcStream()发出的元组声明是new Fields("args"),注意这一点.
               .each(new Fields("args"), new FundSplitFunc(), new Fields("fundcode"))
               .parallelismHint(50)
               /**
                * storm trident中的stream与storm spout/bolt 中的task是等价的概念(这个理解很重要).我们可以对bolt和steam来设置并行度.
                * 表示逻辑上我们给这个任务节点(bolt或者steam)分配parallelism_hint个逻辑上的任务空槽,反映在zookeeper的/tasks/{topology-id}下面会因为
                * 这里多出50个以task-id命名的文件来分别存储各个任务的状态信息,从而来协调任务.
                * 
                * 所以设置并行度，其实就是设置我们逻辑上分配多少个任务槽给这个bolt或者这个stream.所以并行度要在对应的stream执行
                * stream.parallelismHint(parallelism_hint).慎之慎之,这点理解很重要.已经证明了.
                */
               .shuffle()
               .each(new Fields("fundcode"), new FundProfitLostFunc(), new Fields("fundcode-profit-lost-result"))
               .aggregate(new Fields("fundcode-profit-lost-result"), new ProfitLostAggregator(), new Fields("joined-profit-lost-rslt"))
               .project(new Fields("joined-profit-lost-rslt"));//project这里表示DRPCClient.execute()方法返回的结果只是joined-profit-lost-rslt这个fields的tuple.

        Config conf = new Config();
        conf.setNumWorkers(2);
        conf.setDebug(true);

        try
        {
            if (null == args || 0 >= args.length)
            {
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("calc_fund_profit_lost", conf, builder.build());
                Thread.sleep(1000);
                cluster.shutdown();
            }
            else
            {
                StormSubmitter.submitTopology(args[0], conf, builder.build());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
