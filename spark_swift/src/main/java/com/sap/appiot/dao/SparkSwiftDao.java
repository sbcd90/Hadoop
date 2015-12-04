package com.sap.appiot.dao;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class SparkSwiftDao implements SwiftDao {
    private SparkConf conf;
    private JavaSparkContext sc;
    private String provider;
    private String container;

    public SparkSwiftDao(String appName, String master, String provider, String container) {
        this.conf = new SparkConf().setAppName(appName);
        this.sc = new JavaSparkContext(conf);
        this.provider = provider;
        this.container = container;
    }

    public void putFile(String inputPath, String outputPath) {

        JavaRDD<String> data = sc.textFile(inputPath);
        data.saveAsTextFile("swift://" + container + "." + provider + "/" + outputPath);
    }

    public String getFile(String filePath) {
        JavaRDD<String> data = sc.textFile("swift://" + container + "." + provider + "/" + filePath);
        return data.first();
    }
}