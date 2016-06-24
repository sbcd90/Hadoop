package org.apache.kafka;

import org.apache.kafka.avro.AvroSchema;
import org.apache.kafka.consumer.KafkaConsumer;
import org.apache.kafka.schema.SchemaRegistryClient;
import org.apache.kafka.schema.SerializationOption;

public class MainConsumerApplication {
  private static final String topic = "confluent_poc1";

  public static void main(String[] args) throws Exception {
    String avroSchema = new SchemaRegistryClient(1).getOrCreateSchemaRegistry(topic, SerializationOption.VALUE, AvroSchema.getSchema());
    KafkaConsumer consumer = new KafkaConsumer(topic, avroSchema);
    consumer.consume();
  }
}