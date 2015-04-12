package com.sap.test

import com.sap.i076326.kafka_producer

object TestProducer {
  def main (args: Array[String]): Unit = {
    val producer = new kafka_producer(1, "kafkaScala")
    producer.prepareData("hbase_logfiles_store", "row4", "fileKey:fileName;fileContent:content", "catalina_log_1.log;Hello World")
    producer.sendData(producer.createProducerConfig())
    producer.closeProducer()
  }
}