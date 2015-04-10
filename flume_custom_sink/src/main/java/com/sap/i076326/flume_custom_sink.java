package com.sap.i076326;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;


public class flume_custom_sink extends AbstractSink implements Configurable{
    private String tableName;
    private String columnDescriptors;
    private String columns;
    private int batchSize;
    private HBaseAdmin hba;
    private Hbase hbase;
    private int count;

    @Override
    public void configure(Context context) {
        tableName = context.getString("tableName");
        columnDescriptors = context.getString("columnDescriptors");
        columns = context.getString("columns");
        batchSize = context.getInteger("batchSize");
    }

    @Override
    public void start() {
        hbase = new Hbase();
        try {
            hba = hbase.setupConnection();
            hbase.createTable(hba, tableName, columnDescriptors.split(";"));
            count = 0;
        }catch (org.apache.hadoop.hbase.MasterNotRunningException e){

        }catch (org.apache.hadoop.hbase.ZooKeeperConnectionException e){

        }catch (java.io.IOException e){

        }
    }

    @Override
    public void stop() {
        System.out.println("can stop now");
    }

    @Override
    public Status process() throws EventDeliveryException {
        Channel channel = getChannel();
        Transaction tx = null;

        tx = channel.getTransaction();
        try {
            Thread.sleep(60000);
        }catch(java.lang.InterruptedException e){

        }
        tx.begin();

        for (int i = 0; i < batchSize; i++) {
            Event event = channel.take();
            if (event == null) {
                tx.rollback();
                return Status.BACKOFF;
            } else {
                try{
                    String [] data = new String[columns.split(";").length];
                    for(int count=0;count<Bytes.toString(event.getBody()).split(";").length-1;count++){
                        data[count] = Bytes.toString(event.getBody()).split(";")[count+1];
                    }
                    hbase.insertData(tableName, Bytes.toString(event.getBody()).split(";")[0], columns.split(";"), data);
                }catch(java.io.IOException e){

                }
            }
        }
        tx.commit();
        tx.close();
        count++;

        return Status.READY;
    }
}