package com.sap.appiot.main;

import com.sap.appiot.service.TextFileService;

public class TextFileApplication {

    public static void main(String[] args) {
        TextFileService textFileService = new TextFileService("spark_swift_integration_test", "local[4]", "SparkTest", "c1");

        textFileService.putTextFile("src/main/resources/TestFile.txt", "TestFile.txt");
        System.out.println(textFileService.getTextFile("TestFile.txt"));
    }
}