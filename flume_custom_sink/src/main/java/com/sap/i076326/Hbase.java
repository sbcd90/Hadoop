package com.sap.i076326;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;

public class Hbase {

    private Configuration hc = HBaseConfiguration.create(new Configuration());

    public HBaseAdmin setupConnection() throws org.apache.hadoop.hbase.MasterNotRunningException, org.apache.hadoop.hbase.ZooKeeperConnectionException{
        hc.addResource(new Path("file:///usr/lib/hbase/hbase-0.90.2/conf/hbase-site.xml"));

        HBaseAdmin hba = new HBaseAdmin(hc);
        return hba;
    }

    public Configuration getConfiguration() {
        return hc;
    }

    public void createTable(HBaseAdmin admin, String tableName, String [] columnDescriptors) throws java.io.IOException{
        hc.set(TableInputFormat.INPUT_TABLE, tableName);
        HTableDescriptor ht = new HTableDescriptor(tableName);

        for(String columnDescriptor : columnDescriptors) {
            ht.addFamily(new HColumnDescriptor(columnDescriptor));
        }

        admin.createTable(ht);
    }

    public HTable getTable(String tableName) throws java.io.IOException{
        return new HTable(hc, tableName);
    }

    public void insertData(String tableName, String rowName, String [] columns, String [] columnVals) throws java.io.IOException{
        HTable table = new HTable(hc, tableName);

        Put put = new Put(Bytes.toBytes(rowName));

        int count = 0;
        for(String column : columns) {
            put.add(Bytes.toBytes(column.split(":")[0]), Bytes.toBytes(column.split(":")[1]), Bytes.toBytes(columnVals[count]));
            count++;
        }
        table.put(put);
    }
}

