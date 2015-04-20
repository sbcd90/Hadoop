package com.sap.i076326;

import java.io.File;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class client_api {
    private String topicName;
    private String tableName;
    private String rowName;
    private int rowCount;
    private String columns;

    kafka_producer producer;

    public client_api() {
        this.rowCount = 1;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setRowName(String rowName, int rowCount) {
        this.rowName = rowName;
        this.rowCount = rowCount;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public File[] recursiveListFiles(File f) {
        File[] files = f.listFiles();
        return files;
    }

    public void fetchFiles(String directory) {
        File[] listOfFiles = recursiveListFiles(new File(directory));

        int count = rowCount + 1;
        int counter = 0;
        while (counter < listOfFiles.length) {
            String content = "";
            try {
                content = Files.toString(new File(listOfFiles[counter].getAbsolutePath()), Charsets.UTF_8);
            } catch (java.io.IOException e) {

            }
            String data = listOfFiles[counter].getAbsolutePath() + ";" + content;

            callKafkaProducer(data, rowName + count);
            count++;
            counter++;
        }
    }

    public void initializeProducer() {
        producer = new kafka_producer(1, topicName);
    }

    public void callKafkaProducer(String data, String row) {
        producer.prepareData(tableName, row, columns, data);
        producer.sendData(producer.createProducerConfig());
    }

    public void closeProducer() {
        producer.closeProducer();
    }
}