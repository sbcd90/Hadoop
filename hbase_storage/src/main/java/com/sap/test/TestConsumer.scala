package com.sap.test

import com.sap.i076326.kafka_consumer

object TestConsumer{
  def main(args: Array[String]): Unit = {
    val consumer = new kafka_consumer("localhost:2182", "group2", "kafkaScala")
    consumer.run()

//    Thread.sleep(60000)
//    consumer.shutdown()
  }
}