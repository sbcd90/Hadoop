package com.sap.test

import com.sap.i076326.client_api

object TestClient{
  def main (args: Array[String]) {
    val client = new client_api()

    client.setTopicName("kafkaScala")
    client.setZookeeperLocation("localhost:2182")
    client.setConsumerGroup("group2")

    client.setTableName("hbase_logfiles_store")
    client.setRowName("row", 8)
    client.setColumn("fileKey:fileName;fileContent:content")

//    client.initializeConsumer()
    client.initializeProducer()
    client.fetchFiles("/home/sbcd90/Documents/programs/Hadoop/catalina_logs")
    client.closeProducer()
  }
}