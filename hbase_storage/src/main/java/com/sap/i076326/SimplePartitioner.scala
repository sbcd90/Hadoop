package com.sap.i076326

import kafka.producer.Partitioner
import kafka.utils.VerifiableProperties

class SimplePartitioner(verifiableProperties: VerifiableProperties) extends Partitioner{

  def partition(key: Any, a_numPartitions: Int): Int = {
    var partition: Int = 0
    val stringKey: String = key.asInstanceOf[String]
    val offset: Int = stringKey.lastIndexOf(".")
    if (offset > 0) {
      partition = stringKey.substring(offset + 1).toInt % a_numPartitions
    }
    return partition
  }
}