package com.sap.i076326

import java.util
import java.util.Properties

import kafka.consumer.{ConsumerConfig, KafkaStream}
import org.apache.hadoop.hbase.util.Bytes

class kafka_consumer(zookeeper_loc: String, groupId: String, topicName: String) {
  val consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig())
  var tableName :String = null
  var rowName :String = null
  var columns :Array[String] = null
  var columnVals :Array[String] = null

  def createConsumerConfig(): ConsumerConfig = {
    val properties = new Properties()
    properties.put("zookeeper.connect", zookeeper_loc)
    properties.put("group.id", groupId)
    properties.put("zookeeper.session.timeout.ms", "60000")
    properties.put("zookeeper.sync.time.ms", "200")
    properties.put("auto.commit.interval.ms", "1000")

    return new ConsumerConfig(properties)
  }

  def run(): Unit = {
    val MapOfTopics = new util.HashMap[String, java.lang.Integer]()

    //hard-coding no. of threads to 1
    MapOfTopics.put(topicName, 1)
    val topicStreams = consumer.createMessageStreams(MapOfTopics)
    val streams: util.List[KafkaStream[Array[Byte], Array[Byte]]] = topicStreams.get(topicName)

    while(streams.iterator().hasNext()){
      val stream = streams.iterator().next()

      val iterator = stream.iterator()
      while(iterator.hasNext()){
        if(tableName == null)
          tableName = Bytes.toString(iterator.next().message())
        else if(rowName == null)
          rowName = Bytes.toString(iterator.next().message())
        else if(columns == null)
          columns = Bytes.toString(iterator.next().message()).split(";")
        else if(columnVals == null)
          columnVals = Bytes.toString(iterator.next().message()).split(";")

        if(tableName != null && rowName != null && columns != null && columnVals != null ){
          println(tableName)
          println(rowName)
          println(columns(0))
          println(columnVals(0))
          new hbase().insertData(tableName, rowName, columns, columnVals)

          tableName = null
          rowName = null
          columns = null
          columnVals = null
        }
      }
    }
  }

  def shutdown(): Unit = {
    consumer.shutdown()
  }
}