package org.apache.kafka;

import org.apache.kafka.producer.KafkaProducer;
import org.apache.kafka.schema.SchemaRegistryClient;
import org.apache.kafka.schema.SerializationOption;

public class MainProducerApplication {
  private static final String topic = "confluent_poc";

  public static void main(String[] args) throws Exception {
    KafkaProducer producer = new KafkaProducer(topic, new SchemaRegistryClient().getSchemaRegistry(topic, SerializationOption.KEY));
    producer.produce();
  }
}