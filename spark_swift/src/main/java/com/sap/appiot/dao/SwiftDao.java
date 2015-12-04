package com.sap.appiot.dao;

public interface SwiftDao {
    void putFile(String inputPath, String outputPath);

    String getFile(String filePath);
}