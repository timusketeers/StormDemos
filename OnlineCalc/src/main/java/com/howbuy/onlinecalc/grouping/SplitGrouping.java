package com.howbuy.onlinecalc.grouping;

import java.util.List;
import backtype.storm.generated.GlobalStreamId;
import backtype.storm.grouping.CustomStreamGrouping;
import backtype.storm.task.WorkerTopologyContext;

@SuppressWarnings("serial")
public class SplitGrouping implements CustomStreamGrouping
{
    public void prepare(WorkerTopologyContext ctx, GlobalStreamId stream, List<Integer> targetTasks)
    {
        
    }

    public List<Integer> chooseTasks(int taskId, List<Object> values)
    {
        return null;
    }
}
