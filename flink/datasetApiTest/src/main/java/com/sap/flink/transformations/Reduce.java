package com.sap.flink.transformations;

import org.apache.flink.api.common.functions.ReduceFunction;

public class Reduce implements ReduceFunction<Integer> {
    public Integer reduce(Integer integer, Integer t1) throws Exception {
        return  integer + t1;
    }
}