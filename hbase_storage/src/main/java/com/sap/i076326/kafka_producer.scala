package com.sap.i076326

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}

import scala.util.Random

class kafka_producer(var events: Int, topicName: String) {
  var producer :Producer[String,String] = null
  var tableName :String = null
  var rowName :String = null
  var columns :String = null
  var columnVals :String = null

  def kafka_producer(): Random = {
    return new Random()
  }

  def createProducerConfig(): ProducerConfig = {
    val properties = new Properties()
    properties.put("metadata.broker.list", "localhost:9093,localhost:9094")
    properties.put("serializer.class", "kafka.serializer.StringEncoder")
    properties.put("partitioner.class", "com.sap.i076326.SimplePartitioner")
    properties.put("request.required.acks", "1")

    return new ProducerConfig(properties)
  }

  def prepareData(pTableName:String, pRowName:String, pColumns: String, pColumnVals: String): Unit = {
    tableName = pTableName
    rowName = pRowName
    columns = pColumns
    columnVals = pColumnVals
  }

  def sendData(config: ProducerConfig): Unit = {
    producer = new Producer[String,String](config)

    //hardcoding events to 4 for sending hbase data
    events = 4

    for(nEvents <- 1 to events){
      val ip = "192.168.2." + kafka_producer().nextInt(255)

      var data :KeyedMessage[String,String] = null
      if(nEvents == 1){
        data = KeyedMessage[String,String](topicName, ip, null, tableName)
      }else if(nEvents == 2){
        data = KeyedMessage[String,String](topicName, ip, null, rowName)
      }else if(nEvents == 3){
        data = KeyedMessage[String,String](topicName, ip, null, columns)
      }else if(nEvents ==4){
        data = KeyedMessage[String,String](topicName, ip, null, columnVals)
      }

      producer.send(data)
    }
  }

  def closeProducer(): Unit = {
    producer.close
  }

}