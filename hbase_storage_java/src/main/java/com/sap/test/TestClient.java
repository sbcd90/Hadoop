//see the two shell scripts in resources folder.
// change in the line 'count=$((count+<no. of files to be inserted>))' with total no. of files to be inserted, say for 2 files, it'll be 'count=$((count+2))'
package com.sap.test;

import com.sap.i076326.client_api;

public class TestClient {
public static void main(String[] args) {
client_api client = new client_api();

client.setTopicName("storeFiles");

client.setTableName("hbase_logfiles_store");
    client.setRowName("row", 20 );
    client.setColumns("fileKey:fileName;fileContent:content");

client.initializeProducer();
    client.fetchFiles("/home/sbcd90/Documents/programs/Hadoop/catalina_logs");
client.closeProducer();
}
}
