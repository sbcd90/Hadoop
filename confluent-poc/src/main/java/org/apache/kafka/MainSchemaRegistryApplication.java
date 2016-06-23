package org.apache.kafka;

import org.apache.kafka.avro.AvroSchema;
import org.apache.kafka.schema.SchemaRegistryClient;
import org.apache.kafka.schema.SerializationOption;

public class MainSchemaRegistryApplication {
  private static final String topic = "confluent_poc";

  public static void main(String[] args) {
    SchemaRegistryClient client = new SchemaRegistryClient();

    // check schema registries
    client.getSchemaRegistries();

    // post a new schema registry
    client.doSchemaRegistry(topic, SerializationOption.KEY, AvroSchema.getAvroSchema());
  }
}