package com.sap.i076326;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class kafka_consumer_group {
    private ConsumerConnector consumer;
    private String topic;
    private ExecutorService executor;

    private static ConsumerConfig createConsumerConfig(String zookeeper_loc, String groupId){
        Properties properties = new Properties();
        properties.put("zookeeper.connect", zookeeper_loc);
        properties.put("group.id", groupId);
        properties.put("zookeeper.session.timeout.ms","60000");
        properties.put("zookeeper.sync.time.ms", "200");
        properties.put("auto.commit.interval.ms", "1000");

        return new ConsumerConfig(properties);
    }

    public kafka_consumer_group(String zookeeper_loc, String groupId, String topic){
        this.topic = topic;
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper_loc, groupId));
    }

    public void run(int noOfThreads){
        Map<String, Integer> MapOfTopics = new HashMap<String, Integer>();
        MapOfTopics.put(this.topic, new Integer(noOfThreads));
        Map<String, List<KafkaStream<byte[],byte[]>>> topicStreams = consumer.createMessageStreams(MapOfTopics);
        List<KafkaStream<byte[],byte[]>> streams = topicStreams.get(this.topic);

        executor = Executors.newFixedThreadPool(noOfThreads);

        int threadNum = 0;
        for(KafkaStream stream : streams){
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while(it.hasNext()){
                System.out.println("Thread: " + threadNum + " --- Message: " + new String(it.next().message()));
            }
//            executor.submit(new kafka_consumer(stream, threadNum));
            threadNum++;
        }
    }

    public void shutdown() {
        if(consumer != null)
            consumer.shutdown();
        if(executor != null)
            executor.shutdown();

        try {
            if(!executor.awaitTermination(5000, TimeUnit.MILLISECONDS))
                System.out.println("Exit uncleanly");
        }catch(InterruptedException e){
            System.out.println("Exit uncleanly");
        }
    }
}