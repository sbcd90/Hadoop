package com.sap.flink.transformations;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class MapPartition implements MapPartitionFunction<String, List> {
    public void mapPartition(Iterable<String> iterable, Collector<List> collector) throws Exception {
        List<Integer> count = new ArrayList<Integer>();
        for(String s: iterable) {
            count.add(s.split(" ").length);
        }
        collector.collect(count);
    }
}