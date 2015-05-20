package com.sap.i076326;

import java.util.ArrayList;
import java.util.HashMap;

public class FileIo {
    private final HiveApi hiveApi;
    private final HdfsApi hdfsApi;

    public FileIo() throws java.lang.ClassNotFoundException, java.sql.SQLException, java.io.IOException, java.lang.InterruptedException {
        hiveApi = new HiveApi();
        hdfsApi = new HdfsApi("/usr/local/hadoop/hadoop-2.6.0/conf");
    }

    public void writeFile(final String logicalPath, final byte[] content) throws java.sql.SQLException, java.io.IOException {

        //Assuming path is of the format /a/b/c/d/e
        String[] partsOfPath = logicalPath.split("/");
        String firstPath = "/" + partsOfPath[1] + "/" + partsOfPath[2];
        String secondPath = "/" + partsOfPath[3] + "/" + partsOfPath[4] + "/" + partsOfPath[5];

        //check if a table exists or not
        if(!hiveApi.showTables(firstPath.replaceAll("/", "t"))) {

            //Creating new Hive Hash Table to fetch the small file in O(1) time. ( not considering time to select from hive )
            HashMap<String, String> columns = new HashMap<String, String>();
            columns.put("path", "string");
            columns.put("offset", "int");
            columns.put("length", "int");

            //replace "/" with "t" in java -- Temporary solution
            hiveApi.createTable(firstPath.replaceAll("/", "t"), columns);

            //Creating a new file in HDFS
            hdfsApi.writeFile(firstPath, content);

            //Creating a new record in Hive table
            ArrayList<String> values = new ArrayList<String>();
            values.add(Integer.toString(content.length - 1));
            values.add("\"" + secondPath + "\"");
            values.add("0");

            hiveApi.insertIntoTable(firstPath.replaceAll("/", "t"), values);
        }
        else {
            //safe casting to be enabled
            byte[] fileContent = hdfsApi.readFile(firstPath, 0, (int) hdfsApi.getLength(firstPath));
            byte[] resultContent = new byte[fileContent.length + content.length];

            System.arraycopy(fileContent, 0, resultContent, 0, fileContent.length);
            System.arraycopy(content, 0, resultContent, fileContent.length, content.length);

            //Creating a new file in HDFS
            hdfsApi.updateFile(firstPath, resultContent);

            //Creating a new record in Hive table
            ArrayList<String> values = new ArrayList<String>();
            values.add(Integer.toString(content.length - 1));
            values.add("\"" + secondPath + "\"");
            values.add(Integer.toString(fileContent.length));

            hiveApi.insertIntoTable(firstPath.replaceAll("/", "t"), values);
        }
    }

    public byte[] readFile(final String logicalPath) throws java.sql.SQLException, java.io.IOException {

        //Assuming path is of the format /a/b/c/d/e
        String[] partsOfPath = logicalPath.split("/");
        String firstPath = "/" + partsOfPath[1] + "/" + partsOfPath[2];
        String secondPath = "/" + partsOfPath[3] + "/" + partsOfPath[4] + "/" + partsOfPath[5];

        //check if a table exists or not
        if(!hiveApi.showTables(firstPath.replaceAll("/", "t"))) {
            System.out.println("File does not exist");
            return null;
        }
        else {
            ArrayList<Integer> values = hiveApi.readFromTable(firstPath.replaceAll("/", "t"), "path", "\"" + secondPath + "\"");
            return hdfsApi.readFile(firstPath, values.get(0), values.get(1));
        }

    }
}