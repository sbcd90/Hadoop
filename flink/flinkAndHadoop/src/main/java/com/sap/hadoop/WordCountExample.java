package com.sap.hadoop;

import com.sap.hadoop.utils.WordCountMapper;
import com.sap.hadoop.utils.WordCountReducer;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.hadoop.mapreduce.HadoopInputFormat;
import org.apache.flink.api.java.hadoop.mapreduce.HadoopOutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCountExample {
    public static void main(String[] args) throws Exception {
        String inputPath = "hdfs://localhost:9000/tmp/inp1";
        String outputPath = "hdfs://localhost:9000/tmp/out1";

        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

        Job job = Job.getInstance();
        HadoopInputFormat<LongWritable, Text> hadoopInputFormat = new HadoopInputFormat<LongWritable, Text>(new TextInputFormat(), LongWritable.class, Text.class, job);

        TextInputFormat.addInputPath(job, new Path(inputPath));

        DataSet<Tuple2<LongWritable, Text> > text = environment.createInput(hadoopInputFormat);

        DataSet<Tuple2<Text, IntWritable> > result = text.flatMap(new WordCountMapper()).groupBy(0).reduceGroup(new WordCountReducer());

        HadoopOutputFormat<Text, IntWritable> hadoopOutputFormat = new HadoopOutputFormat<Text, IntWritable>(new TextOutputFormat<Text, IntWritable>(), job);

        hadoopOutputFormat.getConfiguration().set("mapreduce.output.textoutputformat.separator", " ");
        TextOutputFormat.setOutputPath(job, new Path(outputPath));

        result.output(hadoopOutputFormat);

        environment.execute("Hadoop WordCount");
    }
}