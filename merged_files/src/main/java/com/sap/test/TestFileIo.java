package com.sap.test;

import com.sap.i076326.FileIo;
import com.sap.i076326.HdfsApi;
import com.sap.i076326.HiveApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class TestFileIo {
    private static final Logger log = LoggerFactory.getLogger(TestFileIo.class);

    public static void main(String[] args) {
        try{
            //delete existing files & tables
            HdfsApi hdfsApi = new HdfsApi("/usr/local/hadoop/hadoop-2.6.0/conf");
            hdfsApi.deleteFile("/a/b");
            hdfsApi.deleteFile("/c/d");
            hdfsApi.deleteFile("/e/f");

            HiveApi hiveApi = new HiveApi();
            hiveApi.deleteTable("tatb");
            hiveApi.deleteTable("tctd");
            hiveApi.deleteTable("tetf");

            //trigger files to be write to HDFS
            FileIo fileIo = new FileIo();
            fileIo.writeFile("/a/b/c/d/e", "Hello World Hadoop, I'm alphabets\n".getBytes(Charset.forName("UTF-8")));
            fileIo.writeFile("/a/b/c/f/g", "Hello World Hadoop, I am alphabets\n".getBytes(Charset.forName("UTF-8")));
            fileIo.writeFile("/c/d/e/h/i", "Hello World Hadoop, I am again alphabets\n".getBytes(Charset.forName("UTF-8")));
            fileIo.writeFile("/c/d/e/j/k", "Hello World Hadoop, I am again again alphabets\n".getBytes(Charset.forName("UTF-8")));
            fileIo.writeFile("/e/f/g/h/i", "Hello World Hadoop, I am again again again alphabets\n".getBytes(Charset.forName("UTF-8")));
            fileIo.writeFile("/e/f/g/j/k", "Hello World Hadoop, I am again again again again alphabets\n".getBytes(Charset.forName("UTF-8")));

            //read a particular small file
            System.out.println(new String(fileIo.readFile("/e/f/g/h/i")));

        }catch(java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }catch(java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }catch(java.io.IOException e) {
            System.out.println(e.getMessage());
        }catch(java.lang.InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}