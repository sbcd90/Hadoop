package com.sap.i076326;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Random;

public class kafka_producer {
    private int events;
    private Random random = new Random();
    private String topicName;
    private String tableName;
    private String rowName;
    private String columns;
    private String columnVals;

    private Producer<String, String> producer = null;

    public kafka_producer(int events, String topicName) {
        this.events = events;
        this.topicName = topicName;
    }

    public int nextInt(int upperRange) {
        return random.nextInt(255);
    }

    public ProducerConfig createProducerConfig() {
        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9093,localhost:9094");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("partitioner.class", "com.sap.i076326.SimplePartitioner");
        properties.put("request.required.acks", "1");

        return new ProducerConfig(properties);
    }

    public void prepareData(String tableName, String rowName, String columns, String columnVals) {
        this.tableName = tableName;
        this.rowName = rowName;
        this.columns = columns;
        this.columnVals = columnVals;
    }

    public void sendData(ProducerConfig config) {
        producer = new Producer<String, String>(config);

        //hardcoding events to 4 for sending hbase data
        this.events = 4;

        for(int nEvents=1;nEvents<=this.events;nEvents++) {
            String ip = "192.168.2." + nextInt(255);

            KeyedMessage<String, String> data = null;
            if(nEvents == 1) {
                data = new KeyedMessage<String, String> (this.topicName, ip, null, tableName);
            }else if(nEvents == 2) {
                data = new KeyedMessage<String, String> (this.topicName, ip, null, rowName);
            }else if(nEvents == 3) {
                data = new KeyedMessage<String, String> (this.topicName, ip, null, columns);
            }else if(nEvents == 4) {
                data = new KeyedMessage<String, String> (this.topicName, ip, null, columnVals);
            }

            System.out.println(tableName);
            producer.send(data);
        }
    }

    public void closeProducer() {
        producer.close();
    }


}