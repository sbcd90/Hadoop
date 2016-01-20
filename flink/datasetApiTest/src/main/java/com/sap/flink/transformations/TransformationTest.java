package com.sap.flink.transformations;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

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

    public void testFilter() throws Exception {
        DataSet<Integer> value = environment.fromElements(20);

        value.filter(new Filter()).print();
    }

    public void testReduce() throws Exception {
        DataSet<Integer> sampleData = environment.fromElements(20, 50);

        sampleData.reduce(new Reduce()).print();
    }

    public void testReduceGroup() throws Exception {
        DataSet<Integer> sampleData = environment.fromElements(1, 2, 3, 4, 5);

        sampleData.reduceGroup(new ReduceGroup()).print();
    }

    public void testAggregate() throws Exception {
        DataSet<Tuple3<Integer, String, Double> > sampleData = environment.fromElements(new Tuple3<Integer, String, Double>(5, "Hello", 1.0),
                                                                                        new Tuple3<Integer, String, Double>(5, "World", 4.0));

        sampleData.min(1).print();
    }

    public void testJoin() throws Exception {
        DataSet<Tuple2<String, String> > sampleData1 = environment.fromElements(new Tuple2<String, String>("Hello", "World1"));
        DataSet<Tuple2<String, String> > sampleData2 = environment.fromElements(new Tuple2<String, String>("Hello1", "World2"));

        sampleData1.join(sampleData2).where(0).equalTo(0).print();
    }

    public void testCross() throws Exception {
        DataSet<Tuple2<String, String> > sampleData1 = environment.fromElements(new Tuple2<String, String>("Hello", "World1"));
        DataSet<Tuple2<String, String> > sampleData2 = environment.fromElements(new Tuple2<String, String>("Hello1", "World2"));

        sampleData1.cross(sampleData2).print();
    }

    public void testUnion() throws Exception {
        DataSet<Tuple2<String, String> > sampleData1 = environment.fromElements(new Tuple2<String, String>("Hello", "World1"));
        DataSet<Tuple2<String, String> > sampleData2 = environment.fromElements(new Tuple2<String, String>("Hello1", "Test2"));

        sampleData1.union(sampleData2).print();
    }
}