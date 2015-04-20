//replace zookeeper location with correct one from the server
package com.sap.test;

import com.sap.i076326.kafka_consumer;

public class TestConsumer {
    public static void main(String [] args) {
        kafka_consumer consumer = new kafka_consumer("localhost:2182", "group4", "storeFiles");
        consumer.run();
    }
}