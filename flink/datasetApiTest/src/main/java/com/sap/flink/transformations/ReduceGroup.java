package com.sap.flink.transformations;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.util.Collector;

public class ReduceGroup implements GroupReduceFunction<Integer, Integer> {
    public void reduce(Iterable<Integer> iterable, Collector<Integer> collector) throws Exception {
        Integer prefixSum = 0;
        for(Integer t: iterable) {
            prefixSum = prefixSum + t;
            collector.collect(prefixSum);
        }
    }
}