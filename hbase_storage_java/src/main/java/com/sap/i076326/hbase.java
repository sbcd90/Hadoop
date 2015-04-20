package com.sap.i076326;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.util.Bytes;

public class hbase {
    private Configuration hc;
    private HBaseAdmin hba;

    public hbase() {
        hc = HBaseConfiguration.create(new Configuration());
        hc.addResource(new Path("file:///usr/lib/hbase/hbase-0.90.2/conf/hbase-site.xml"));

        try {
            hba = new HBaseAdmin(hc);
        }catch(MasterNotRunningException e) {

        }catch(ZooKeeperConnectionException e) {

        }
    }

    public Configuration getConfiguration() {
        return hc;
    }

    public  void createTable(String tableName, String[] columnDescriptors) {
        hc.set(TableInputFormat.INPUT_TABLE, tableName);

        HTableDescriptor ht = new HTableDescriptor(tableName);
        for(String columnDescriptor : columnDescriptors) {
            ht.addFamily(new HColumnDescriptor(columnDescriptor));
        }
        try{
            hba.createTable(ht);
        }catch (java.io.IOException e) {

        }
    }

    public void insertData(String tableName, String rowName, String[] columns, String[] columnVals) {
        HTable table = null;
        try{
            table = new HTable(hc, tableName);
        }catch(java.io.IOException e) {

        }

        Put put = new Put(Bytes.toBytes(rowName));

        int count = 0;
        for(String column : columns) {
            put.add(Bytes.toBytes(column.split(":")[0]), Bytes.toBytes(column.split(":")[1]), Bytes.toBytes(columnVals[count]));
            count++;
        }

        try{
            table.put(put);
        }catch (java.io.IOException e) {

        }
    }
}