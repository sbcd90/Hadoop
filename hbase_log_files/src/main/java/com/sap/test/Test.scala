package com.sap.test

import com.sap.i076326.spark_hbase

object Test {
  def main(args: Array[String]) : Unit = {

/*    val hbase_wrapper = new hbase()

    val hba = hbase_wrapper.setupConnection()

    hbase_wrapper.disableTable(hba, "emp_client")
    hbase_wrapper.dropTable(hba, "emp_client")

    val columnDescriptors = Array("personal data", "professional data")

    hbase_wrapper.createTable(hba, "emp_client", columnDescriptors)

    println(hbase_wrapper.listTable(hba))

    hbase_wrapper.insertData("emp_client", "row1", Array("personal data:PersonalId", "professional data:ProfessionalId"), Array("J6818101","i076326"))

    var result = hbase_wrapper.getByRowName("emp_client", "row1", Array("personal data:PersonalId", "professional data:ProfessionalId"))
    println(result("professional data:ProfessionalId"))

    val resultArr = hbase_wrapper.scanRows("emp_client", Array("personal data:PersonalId", "professional data:ProfessionalId"))
    result = resultArr(0)
    println(result("personal data:PersonalId"))*/

    val spark = new spark_hbase()
      val sc = spark.setUp()
//      spark.createTable("emp_spark_client", Array("personal data", "professional data"))
//      spark.insertData("emp_spark_client", "r", 8, Array("personal data:PersonalId", "professional data:ProfessionalId"), Array("J6818101","i076326"))
      println(spark.createRDD(sc, "log_files_store"))

/*      val logs_type1 = new store_logs_without_messaging()
    val filesList = logs_type1.getFilesList("/home/sbcd90/Documents/programs/catalina_logs")
    logs_type1.putFilesIntoHbase("catalina_log1", "rcatalina_log1", Array("fileId", "fileData"), Array("fileId:normalId", "fileData:logData"), "/home/sbcd90/Documents/programs/catalina_logs", filesList)
    var result = logs_type1.readFileFromHBase("catalina_log1", "rcatalina_log10", Array("fileId:normalId", "fileData:logData"))
    println(result("fileId:normalId"))
    val resultArr = logs_type1.readFilesFromHBase("catalina_log1", Array("fileId:normalId", "fileData:logData"))
    result = resultArr(1)
    println(result("fileId:normalId"))*/

 /*   val flume_source = new flume_embedded_agent()
    flume_source.create_embedded_agent()*/
  }
}