package com.sap.i076326

import java.io.File

class store_logs_without_messaging {
  var hbase :hbase = null

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

  def writeToHbase(tableName: String, rowName: String, key: String, columns: Array[String], data: Array[String]): Unit = {
    hbase = getHbaseInstance()

    val table = hbase.getTable(tableName)
    hbase.insertData(tableName, rowName, columns, data)
    table.flushCommits()
  }

  def getFilesList(directory: String): List[String] = {
    return new File(directory).listFiles().map(_.getName).toList
  }

  def readFiles(directory: String, filesList: List[String]): scala.collection.mutable.ArrayBuffer[String] = {
    var fileData = new scala.collection.mutable.ArrayBuffer[String]

    for(count <- 0 to filesList.length-1) {
      val line = scala.io.Source.fromFile(directory + "/" + filesList(count)).mkString
      fileData += line
    }
    return fileData
  }

  def putFilesIntoHbase(tableName: String, rowName: String, columnDescriptors: Array[String], columns: Array[String], directory: String, filesList: List[String]): Unit = {
    createTable(tableName, columnDescriptors)

    val fileData = readFiles(directory, filesList)
    for(count <- 0 to fileData.length-1) {
      writeToHbase(tableName, rowName + count, filesList(count), columns, Array(filesList(count), fileData(count)))
    }
  }

  def readFileFromHBase(tableName: String, rowName: String, columns: Array[String]): scala.collection.mutable.Map[String,String] = {
    return getHbaseInstance().getByRowName(tableName, rowName, columns)
  }

  def readFilesFromHBase(tableName: String, columns: Array[String]): scala.collection.mutable.ArrayBuffer[scala.collection.mutable.Map[String,String]] = {
    return getHbaseInstance().scanRows(tableName, columns)
  }
}