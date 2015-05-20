package com.sap.test;

import com.sap.i076326.HdfsApi;

import java.nio.charset.Charset;

public class TestHdfsApi {
    private static HdfsApi fs;
    private static byte[] fileContent;

    public void testGetLength() {
        try {
            System.out.println(fs.getLength("/root/HelloWorld1.txt"));
        }catch(java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testReadFile() {
        try {
            fileContent = fs.readFile("/root/HelloWorld1.txt", 0, 25);
            System.out.println(new String(fileContent));
        }catch(java.io.IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void testWriteFile() {
        try{
            byte[] content = "Hello World Hadoop Check!".getBytes(Charset.forName("UTF-8"));
            fs.writeFile("/root/HelloWorld1.txt", content);
        }catch(java.io.IOException e){

        }
    }

    public void testDeleteFile() {
        try {
            fs.deleteFile("/root/HelloWorld1.txt");
        }catch(java.io.IOException e) {

        }
    }

    public void testUpdateFile() {
        try {
            byte[] content = "Hello World Hadoop Check!".getBytes(Charset.forName("UTF-8"));
            fs.updateFile("/root/HelloWorld1.txt", content);
        }catch(java.io.IOException e) {

        }
    }

    public void testAppendFile() {
        try {
            byte[] content = "Hello World Hadoop Append!".getBytes(Charset.forName("UTF-8"));
            fs.appendFile("/root/HelloWorld1.txt", content);
        }catch(java.io.IOException e) {

        }
    }

    public void testGetDefaultBlockSize() {
        System.out.println(fs.getDefaultBlockSize("/root/HelloWorld1.txt"));
    }

    public static void main(String[] args) {
        TestHdfsApi test = new TestHdfsApi();
        try{
            fs = new HdfsApi("/usr/local/hadoop/hadoop-2.6.0/conf");
        }catch(java.io.IOException e) {
            System.out.println(e.getMessage());
        }catch(java.lang.InterruptedException e) {
            System.out.println(e.getMessage());
        }finally{
            if(fs != null) {
                test.testDeleteFile();
                test.testWriteFile();
                test.testReadFile();
                test.testUpdateFile();
//                test.testAppendFile();
                test.testGetLength();
                test.testGetDefaultBlockSize();

                try{
                    fs.closeFileSystem();
                }catch (java.io.IOException e){

                }
            }
        }
    }
}