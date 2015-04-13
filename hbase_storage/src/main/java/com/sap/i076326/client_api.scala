package com.sap.i076326

import java.io.File

class client_api {
  var topicName: String = null
  var tableName: String = null
  var rowName: String = null
  var rowCount: Int = 1
  var columns: String = null
  var location: String = null
  var groupName: String = null

  var producer: com.sap.i076326.kafka_producer = null

  def setTopicName(pTopicName: String): Unit = {
    topicName = pTopicName
  }

  def setZookeeperLocation(pLocation: String): Unit = {
    location = pLocation
  }

  def setConsumerGroup(pGroupName: String): Unit = {
    groupName = pGroupName
  }

  def setTableName(pTableName: String): Unit = {
    tableName = pTableName
  }

  def setRowName(pRowName: String, pRowCount: Int): Unit = {
    rowName = pRowName
    rowCount = pRowCount
  }

  def setColumn(pColumns: String): Unit = {
    columns = pColumns
  }

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  def fetchFiles(directory: String): Unit = {
    val listOfFiles = recursiveListFiles(new File(directory))

    var count = rowCount + 1
    for(fileloc <- listOfFiles){
      val lines = scala.io.Source.fromFile(fileloc.getAbsolutePath()).mkString
      val data = fileloc.toString + ";" + lines

      callKafkaProducer(data, rowName + count)
      count = count + 1
    }
  }

  def initializeProducer(): Unit = {
    producer = new kafka_producer(1, topicName)
  }

  def initializeConsumer(): Unit = {
    val consumer = new kafka_consumer(location, groupName, topicName)
    consumer.run()
  }

  def callKafkaProducer(data: String, row: String): Unit = {
    producer.prepareData(tableName, row, columns, data)
    producer.sendData(producer.createProducerConfig())
  }

  def closeProducer(): Unit = {
    producer.closeProducer()
  }
}