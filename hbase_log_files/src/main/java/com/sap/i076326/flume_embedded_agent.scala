//hbase sink not supported
package com.sap.i076326

import java.util.HashMap
import org.apache.flume.agent.embedded.EmbeddedAgent

class flume_embedded_agent {
  def create_embedded_agent() : Unit = {
    val properties = new HashMap[String, String]()
    properties.put("sources", "source1")
    properties.put("sinks", "sink1")
    properties.put("source1.type", "exec")
    properties.put("source1.command", "sh /home/sbcd90/Documents/programs/readFile.sh /home/sbcd90/Documents/programs/catalina_logs/catalina.2014-12-19.log")
    properties.put("source1.batchSize", "1")
    properties.put("sink1.type", "org.apache.flume.sink.hbase.HBaseSink")
    properties.put("sink1.table", "events_table1")
    properties.put("sink1.columnFamily",  "edata")
    properties.put("sink1.serializer", "org.apache.flume.sink.hbase.SimpleAsyncHbaseEventSerializer")
    properties.put("sink1.serializer.payloadColumn","payload")
    properties.put("channel.type", "memory")
    properties.put("channel.capacity", "1000")
    properties.put("channel.transactionCapacity", "100")

    println(properties)

    val agent = new EmbeddedAgent("myagent")

    agent.configure(properties)
    agent.start()
    agent.stop()
  }
}