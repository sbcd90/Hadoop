package com.sap.bigdata;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * As input ( after shuffling ) the Reducer class takes ( Text, IntWritable ) (Key,Value) pairs like ("Hello", (1, 2)),("World", 1), ("sbcd90", 1)
 * As output the Reducer class produces ( Text, IntWritable ) (Key, Value) pairs like ("Hello", 2), ("World", 1), ("sbcd90", 1)
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        Integer sum = 0;
        Iterator<IntWritable> it = values.iterator();

        while (it.hasNext()) {
            sum = sum + it.next().get();
        }
        context.write(key, new IntWritable(sum));
    }
}