package com.sap.appiot.service;

import com.sap.appiot.dao.SparkSwiftDao;
import com.sap.appiot.dao.SwiftDao;

public class TextFileService {
    private SwiftDao swiftDao;

    public TextFileService(String appName, String master, String provider, String container) {
        swiftDao = new SparkSwiftDao(appName, master, provider, container);
    }

    public String getTextFile(String path) {
        return swiftDao.getFile(path);
    }

    public void putTextFile(String inputPath, String outputPath) {
        swiftDao.putFile(inputPath, outputPath);
    }
}