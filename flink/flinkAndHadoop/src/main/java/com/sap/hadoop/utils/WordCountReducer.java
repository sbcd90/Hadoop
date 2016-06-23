package com.sap.hadoop.utils;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * As input ( after shuffling ) the Reducer class takes ( Text, IntWritable ) (Key,Value) pairs like ("Hello", (1, 2)),("World", 1), ("sbcd90", 1)
 * As output the Reducer class produces ( Text, IntWritable ) (Key, Value) pairs like ("Hello", 2), ("World", 1), ("sbcd90", 1)
 */
public class WordCountReducer implements GroupReduceFunction<Tuple2<String, Integer>, Tuple2<Text, IntWritable> > {
    public void reduce(Iterable<Tuple2<String, Integer>> iterable, Collector<Tuple2<Text, IntWritable>> collector) throws Exception {
        Iterator<Tuple2<String, Integer>> it = iterable.iterator();

        while (it.hasNext()) {
            Tuple2<String, Integer> record = it.next();
            collector.collect(new Tuple2<Text, IntWritable>(new Text(record.f0), new IntWritable(record.f1)));
        }
    }
}