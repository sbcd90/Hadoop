package org.apache.kafka.consumer;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KafkaConsumer {
  private String topic;
  private String avroSchema;
  private final String broker = "10.9.43.125:9092";

  public KafkaConsumer(String topic, String schema) {
    this.topic = topic;
    this.avroSchema = schema;
  }

  public void consume() throws InterruptedException {
    SparkConf conf = new SparkConf().setAppName("Kafka-Consumer").setMaster("local[*]");
    JavaSparkContext sparkContext = new JavaSparkContext(conf);
    sparkContext.setLogLevel("WARN");
    JavaStreamingContext ssc = new JavaStreamingContext(sparkContext, new Duration(2000));

    Set<String> topics = Collections.singleton(topic);
    Map<String, String> kafkaParams = new HashMap<String, String>();
    kafkaParams.put("metadata.broker.list", broker);

    JavaPairInputDStream<String, byte[]> directKafkaStream = KafkaUtils.createDirectStream(ssc, String.class, byte[].class,
      StringDecoder.class, DefaultDecoder.class, kafkaParams, topics);

    directKafkaStream.foreachRDD(rdd -> {
      Map<String, byte[]> result = rdd.collectAsMap();
      for (Map.Entry<String, byte[]> avroRecord: result.entrySet()) {
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(avroSchema);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
        GenericRecord record = recordInjection.invert(avroRecord.getValue()).get();

        System.out.println("key = " + record.get("key") + ", value = " + record.get("value"));
      }
    });

    ssc.start();
    ssc.awaitTermination();
  }
}