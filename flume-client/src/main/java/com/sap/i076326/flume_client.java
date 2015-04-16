package com.sap.i076326;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.io.File;

public class flume_client {
    protected RpcClient client;
    protected String hostname;
    protected int port;

    public static void main(String [] args) {
        flume_client avroClient = new flume_client();
        avroClient.callAvroSource();
    }

    public void callAvroSource() {

        this.hostname = "0.0.0.0";
        this.port = 41414;
        this.client = RpcClientFactory.getDefaultInstance(this.hostname, this.port);

        String content = "";
        try {
            content = Files.toString(new File("/home/sbcd90/Documents/programs/Hadoop/catalina_logs/catalina.2014-12-19.log"), Charsets.UTF_8);
        }catch(java.io.IOException e){

        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("row4;catalina.2014-12-19.log;");
        stringBuilder.append(content);
        Event event = EventBuilder.withBody(stringBuilder.toString().getBytes());
        try{
            client.append(event);
        }catch(EventDeliveryException e){
            client.close();
            client = null;
            client = RpcClientFactory.getDefaultInstance(this.hostname, this.port);
        }
        client.close();
    }
}