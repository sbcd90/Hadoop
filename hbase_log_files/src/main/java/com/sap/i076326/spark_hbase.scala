package com.sap.i076326

import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark._

class spark_hbase {

  var hbase :hbase = null

  def setUp(): SparkContext = {
    val conf = new SparkConf().setAppName("spark_hbase").setMaster("local")
    val sc = new SparkContext(conf)
    return sc
  }

  def getHbaseInstance(): hbase = {
    if(hbase == null)
      return new hbase()
    else
      return hbase
  }

  def createTable(tableName: String, columnDescriptors: Array[String]): Unit = {
    hbase = getHbaseInstance()
    val admin = hbase.setupConnection()
    if(!admin.isTableAvailable(tableName))
      hbase.createTable(admin, tableName, columnDescriptors)
    else{
      val colFamily = hbase.createColumnFamily(columnDescriptors(0))
      admin.disableTable(tableName)
      admin.addColumn(tableName, colFamily)
      admin.enableTable(tableName)
    }
  }

  def insertData(tableName: String, rowName: String, noOfColumns: Integer, columns: Array[String], columnVals: Array[String]): Unit = {
    hbase = getHbaseInstance()
    val table = hbase.getTable(tableName)

    for(count <- 0 to noOfColumns) {
      hbase.insertData(tableName, rowName + count, columns, columnVals)
    }
    table.flushCommits()
  }

  def createRDD(sc: SparkContext): Long = {
    hbase = getHbaseInstance()
    val hBaseRDD = sc.newAPIHadoopRDD(hbase.getConfiguration(), classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    return hBaseRDD.count()
  }
}