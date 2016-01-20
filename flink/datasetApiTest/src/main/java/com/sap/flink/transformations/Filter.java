package com.sap.flink.transformations;

import org.apache.flink.api.common.functions.FilterFunction;

public class Filter implements FilterFunction<Integer> {
    public boolean filter(Integer integer) throws Exception {
        return integer > 1000;
    }
}