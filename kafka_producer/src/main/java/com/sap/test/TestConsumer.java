package com.sap.test;

import com.sap.i076326.kafka_consumer_group;

public class TestConsumer {
    public static void main(String [] args){
        kafka_consumer_group cg = new kafka_consumer_group("localhost:2181", "group1", "testTopic");
        cg.run(4);
        try{
            Thread.sleep(60000);
        }catch(InterruptedException e){

        }
        cg.shutdown();
    }
}