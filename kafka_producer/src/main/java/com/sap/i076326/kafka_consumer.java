package com.sap.i076326;

import kafka.consumer.KafkaStream;
import kafka.consumer.ConsumerIterator;

public class kafka_consumer implements Runnable {
    private KafkaStream stream;
    private int threadNumber;

    public kafka_consumer(KafkaStream aStream, int threadNo){
        stream = aStream;
        threadNumber = threadNo;
    }

    public void run() {
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while(it.hasNext()){
            System.out.println("Thread: " + threadNumber + " --- Message: " + new String(it.next().message()));
        }
        System.out.println("Shutting down thread");
    }
}