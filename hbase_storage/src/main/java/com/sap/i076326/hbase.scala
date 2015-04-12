package com.sap.i076326

import java.io.IOException

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{Get, HBaseAdmin, HTable, Put, Scan}
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor}

class hbase {

  var hc = HBaseConfiguration.create(new Configuration())

  @throws(classOf[IOException])
  def setupConnection(): HBaseAdmin = {
    hc.addResource(new Path("file:///usr/lib/hbase/hbase-0.90.2/conf/hbase-site.xml"))

    val hba = new HBaseAdmin(hc)
    return hba
  }

  def getConfiguration(): Configuration = {
    return hc
  }

  @throws(classOf[IOException])
  def createTable(connection: HBaseAdmin, tableName: String, columnDescriptors: Array[String]): Unit = {
    hc.set(TableInputFormat.INPUT_TABLE, tableName)
    val ht = new HTableDescriptor(tableName)

    for(columnDescriptor <- columnDescriptors) {
      ht.addFamily(new HColumnDescriptor(columnDescriptor))
    }
    connection.createTable(ht)
  }

  @throws(classOf[IOException])
  def listTable(connection: HBaseAdmin): String = {
    val tableDescriptors = connection.listTables()

    var tabNames = ""
    for(tableDescriptor <- tableDescriptors) {
      tabNames += tableDescriptor.getNameAsString() + "\n"
    }

    return tabNames
  }

  def getTable(tableName: String): HTable = {
    return new HTable(hc, tableName)
  }

  @throws(classOf[IOException])
  def enableTable(connection: HBaseAdmin, tableName: String): Unit = {
    if(connection.isTableDisabled(tableName))
      connection.enableTable(tableName)
  }

  @throws(classOf[IOException])
  def disableTable(connection: HBaseAdmin, tableName: String): Unit = {
    if(!connection.isTableDisabled(tableName))
      connection.disableTable(tableName)
  }

  @throws(classOf[IOException])
  def dropTable(connection: HBaseAdmin, tableName: String): Unit = {
    if(connection.isTableDisabled(tableName))
      connection.deleteTable(tableName)
  }

  def createColumnFamily(cfName: String): HColumnDescriptor = {
    return new HColumnDescriptor(cfName)
  }

  @throws(classOf[IOException])
  def insertData(tableName: String, rowName: String, columns: Array[String], columnVals: Array[String]): Unit = {
    val table = new HTable(hc, tableName)

    val put = new Put(Bytes.toBytes(rowName))

    var count = 0
    for(column <- columns) {
      put.add(Bytes.toBytes(column.split(":")(0)), Bytes.toBytes(column.split(":")(1)), Bytes.toBytes(columnVals(count)))
      count = count + 1
    }
    table.put(put)
  }

  @throws(classOf[IOException])
  def getByRowName(tableName: String, rowName: String, columns: Array[String]): scala.collection.mutable.Map[String,String] = {
    val table = new HTable(hc, tableName)

    val get = new Get(Bytes.toBytes(rowName))
    val result = table.get(get)

    val returnMap = scala.collection.mutable.Map[String,String]()
    for(column <- columns) {
      val value = Bytes.toString(result.getValue(Bytes.toBytes(column.split(":")(0)), Bytes.toBytes(column.split(":")(1))))
      returnMap.put(column, value)
    }
    return returnMap
  }

  @throws(classOf[IOException])
  def scanRows(tableName: String, columns: Array[String]): scala.collection.mutable.ArrayBuffer[scala.collection.mutable.Map[String,String]] = {
    val table = new HTable(hc, tableName)

    val scan = new Scan()

    for(column <- columns) {
      scan.addColumn(Bytes.toBytes(column.split(":")(0)), Bytes.toBytes(column.split(":")(1)))
    }

    val rs = table.getScanner(scan)
    var returnArr = new scala.collection.mutable.ArrayBuffer[scala.collection.mutable.Map[String,String]]()

    while (true) {

      val data = rs.next()
      if(data == null){
        rs.close()
        return returnArr
      }
      val returnMap = scala.collection.mutable.Map[String,String]()
      for(column <- columns) {
        val value = Bytes.toString(data.getValue(Bytes.toBytes(column.split(":")(0)), Bytes.toBytes(column.split(":")(1))))
        returnMap.put(column, value)
      }
      returnArr += returnMap
    }
    return null
  }
}