package com.sap.bigdata;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountApplication extends Configured implements Tool {
    public int run(String[] strings) throws Exception {
        if(strings.length != 2) {
            System.out.println("Error with <input> <output>" + getClass().getSimpleName());
            return -1;
        }

        Job job = new Job();
        job.setJarByClass(WordCountApplication.class);
        job.setJobName("WordCountApplication");

        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        Integer returnValue = job.waitForCompletion(true) ? 0:1;
        System.out.println(job.isSuccessful());
        return returnValue;
    }

    /**
     *
     * @param args
     * @throws Exception
     * @apiNote see how to setup single node cluster https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html
     * @apiNote the sample inp1.txt which is used as input is kept in resources.
     */
    public static void main(String[] args) throws Exception {
        String[] strings = new String[2];
        strings[0] = "hdfs://localhost:9000/tmp/inp1";
        strings[1] = "hdfs://localhost:9000/tmp/out1";

        int exitCode = ToolRunner.run(new WordCountApplication(), strings);
        System.out.println(exitCode);
    }
}