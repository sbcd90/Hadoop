package com.sap.flink.transformations;

import org.apache.flink.api.common.functions.MapFunction;

public class Map implements MapFunction<String, Integer> {
    public Integer map(String s) throws Exception {
        return Integer.parseInt(s);
    }
}