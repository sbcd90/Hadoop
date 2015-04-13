package com.sap.i076326;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class kafka_producer {
    public void producer() {
        long events = 5;
        Random rnd = new Random();

        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9093,localhost:9094");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("partitioner.class","com.sap.i076326.SimplePartitioner");
        properties.put("request.required.acks","1");

        ProducerConfig config = new ProducerConfig(properties);

        Producer<String,String> producer = new Producer<String, String>(config);
        for(long nEvents=0;nEvents<events;nEvents++){
            long runTime = new Date().getTime();
            String ip = "192.168.2." + rnd.nextInt(255);
            String msg = runTime + ",www.example.com," + ip;
            KeyedMessage<String,String> data = new KeyedMessage<String, String>("testTopic", ip, msg);
            producer.send(data);
        }
        producer.close();
    }
}