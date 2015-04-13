package com.sap.test;

import com.sap.i076326.kafka_producer;

public class TestProducer {
    public static void main(String [] args){
        kafka_producer prod = new kafka_producer();
        prod.producer();
    }
}