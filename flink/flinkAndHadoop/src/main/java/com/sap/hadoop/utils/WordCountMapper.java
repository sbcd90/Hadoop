package com.sap.hadoop.utils;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.util.StringTokenizer;

/**
 * As input the Mapper class takes ( LongWritable, Text ) (Key, Value) pairs like (1, "Hello World")
 * As output the Mapper class produces ( Text, Intwritable ) (Key, Value) pairs like ("Hello", 1), ("World", 1)
 */
public class WordCountMapper implements FlatMapFunction<Tuple2<LongWritable, Text>, Tuple2<String, Integer> > {
    public void flatMap(Tuple2<LongWritable, Text> longWritableTextTuple2, Collector<Tuple2<String, Integer>> collector) throws Exception {
        StringTokenizer tokenizer = new StringTokenizer(longWritableTextTuple2.toString());

        while (tokenizer.hasMoreTokens()) {
            collector.collect(new Tuple2<String, Integer>(tokenizer.nextToken(), 1));
        }
    }
}