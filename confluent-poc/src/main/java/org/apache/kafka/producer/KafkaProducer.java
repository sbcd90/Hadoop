package org.apache.kafka.producer;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducer {
  private String topic;
  private String avroSchema;
  private final String broker = "10.9.43.125:9092";
  private final int msgCount = 10;

  public KafkaProducer(String topic, String schema) {
    this.topic = topic;
    this.avroSchema = schema;
  }

  public void produce() throws InterruptedException {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", broker);
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

    Schema.Parser parser = new Schema.Parser();
    Schema schema = parser.parse(avroSchema);
    Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

    org.apache.kafka.clients.producer.KafkaProducer<String, byte[]> producer =
      new org.apache.kafka.clients.producer.KafkaProducer<String, byte[]>(properties);
    for (int i=0;i < msgCount;i++) {
      GenericData.Record avroRecord = new GenericData.Record(schema);
      avroRecord.put("key", i);
      avroRecord.put("value", "str - " + String.valueOf(i));

      byte[] bytes = recordInjection.apply(avroRecord);

      ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, bytes);
      producer.send(record);

      Thread.sleep(3000);
    }

    producer.close();
  }
}