package org.apache.kafka;

import org.apache.kafka.consumer.KafkaConsumer;
import org.apache.kafka.schema.SchemaRegistryClient;
import org.apache.kafka.schema.SerializationOption;

public class MainConsumerApplication {
  private static final String topic = "confluent_poc";

  public static void main(String[] args) throws Exception {
    String avroSchema = new SchemaRegistryClient().getSchemaRegistry(topic, SerializationOption.KEY);
    KafkaConsumer consumer = new KafkaConsumer(topic, avroSchema);
    consumer.consume();
  }
}