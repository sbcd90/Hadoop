package com.sap.i076326;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

public class kafka_consumer {
    private ConsumerConnector consumer;
    private String tableName;
    private String rowName;
    private String[] columns;
    private String[] columnVals;

    private String zookeeper_loc;
    private String groupId;
    private String topicName;

    public kafka_consumer(String zookeeper_loc, String groupId, String topicName) {
        this.zookeeper_loc = zookeeper_loc;
        this.groupId = groupId;
        this.topicName = topicName;

        this.tableName = null;
        this.rowName = null;
        this.columns = null;
        this.columnVals = null;

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
    }

    public ConsumerConfig createConsumerConfig() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", zookeeper_loc);
        properties.put("group.id", groupId);
        properties.put("zookeeper.session.timeout.ms", "60000");
        properties.put("zookeeper.sync.time.ms", "200");
        properties.put("auto.commit.interval.ms", "1000");

        return new ConsumerConfig(properties);
    }

    public void run() {
        HashMap<String, java.lang.Integer> MapOfTopics = new HashMap<String, java.lang.Integer> ();

        //hard-coding no. of threads to 1
        MapOfTopics.put(topicName, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> topicStreams = consumer.createMessageStreams(MapOfTopics);

        List<KafkaStream<byte[], byte[]>> streams = topicStreams.get(topicName);
        while(streams.iterator().hasNext()) {
            KafkaStream<byte[], byte[]> stream = streams.iterator().next();

            ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

            while(iterator.hasNext()) {
                if (tableName == null)
                    tableName = Bytes.toString(iterator.next().message());
                else if (rowName == null)
                    rowName = Bytes.toString(iterator.next().message());
                else if (columns == null)
                    columns = Bytes.toString(iterator.next().message()).split(";");
                else if (columnVals == null)
                    columnVals = Bytes.toString(iterator.next().message()).split(";");

                if (tableName != null && rowName != null && columns != null && columnVals != null) {
                    new hbase().insertData(tableName, rowName, columns, columnVals);

                    tableName = null;
                    rowName = null;
                    columns = null;
                    columnVals = null;
                }
            }
        }
    }

    public void shutdown() {
        consumer.shutdown();
    }
}