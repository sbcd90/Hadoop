package com.sap.flink.transformations;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

public class TransformationTest {
    private ExecutionEnvironment environment;

    public TransformationTest(ExecutionEnvironment environment) {
        this.environment = environment;
    }

    public void testMap() throws Exception {
        DataSet<String> sampleData = environment.fromElements("1");

        sampleData.map(new Map()).print();
    }

    public void testFlatMap() throws Exception {
        DataSet<String> sampleData = environment.fromElements(
                "Hello! This is a text element",
                "to represent Apache Flink Streaming",
                "Hello Hello Hello"
        );

        DataSet<Tuple2<String, Integer> > result = sampleData.flatMap(new FlatMap());

        result.print();
    }

    public void testMapPartition() throws Exception {
        DataSet<String> sampleData = environment.fromElements(
                "Hello! This is a text element",
                "to represent Apache Flink Streaming",
                "Hello Hello Hello"
        );

        sampleData.mapPartition(new MapPartition()).print();
    }
}