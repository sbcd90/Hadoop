package org.apache.kafka.schema;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import org.apache.avro.Schema;

public class SchemaRegistryClient {
  private static final String schemaRegistryUrl = "http://10.9.43.125:8081";

  private final String KEY_SERIALIZATION_FLAG = "key";
  private final String VALUE_SERIALIZATION_FLAG = "value";

  private final io.confluent.kafka.schemaregistry.client.SchemaRegistryClient schemaRegistryClient;

  // identityMapCapacity - no. of schemas per topic.
  public SchemaRegistryClient(int identityMapCapacity) {
    schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, identityMapCapacity);
  }

  public boolean doSchemaRegistry(String topic, SerializationOption flag, Schema schema) throws Exception {
    String subject = null;
    if (flag == SerializationOption.KEY) {
      subject = topic + "-" + KEY_SERIALIZATION_FLAG;
    } else {
      subject = topic + "-" + VALUE_SERIALIZATION_FLAG;
    }
    schemaRegistryClient.register(subject, schema);
    return true;
  }

  public String getSchema(String topic, SerializationOption flag) throws Exception {
    String subject = null;
    if (flag == SerializationOption.KEY) {
      subject = topic + "-" + KEY_SERIALIZATION_FLAG;
    } else {
      subject = topic + "-" + VALUE_SERIALIZATION_FLAG;
    }
    SchemaMetadata metadata = schemaRegistryClient.getLatestSchemaMetadata(subject);
    return metadata.getSchema();
  }

  public String getOrCreateSchemaRegistry(String topic, SerializationOption flag, Schema... schema) {
    try {
      return getSchema(topic, flag);
    } catch (Exception e) {
      try {
        doSchemaRegistry(topic, flag, schema[0]);
        return getSchema(topic, flag);
      } catch (Exception e1) {
        throw new RuntimeException(e1);
      }
    }
  }
}