package com.sap.i076326;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.PrintWriter;
import java.util.Arrays;


public class HdfsApi {
    private final FileSystem fs;

    public HdfsApi(final String configDir) throws java.io.IOException, java.lang.InterruptedException {
        Configuration config = new Configuration();
        config.set("fs.default.name", "hdfs://localhost:54310");
        config.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        config.set("dfs.support.append", "true");
        config.addResource(configDir + "/core-site.xml");
        config.addResource(configDir + "/hdfs-site.xml");
        config.addResource(configDir + "/mapred-site.xml");

        fs = FileSystem.get(config);
    }

    public FileSystem getFileSystem() {
        return fs;
    }

    public long getDefaultBlockSize(String filePath) {
        Path path = new Path(filePath);
        return fs.getDefaultBlockSize(path);
    }

    public long getLength(final String filePath) throws java.io.IOException {
        Path path = new Path(filePath);
        return fs.getFileStatus(path).getLen();
    }

    public byte[] readFile(final String filePath, int offset, int length) throws java.io.IOException {
        Path path = new Path(filePath);

        if(!fs.exists(path)){
            System.out.println("File does not exist");
            return null;
        }

        FSDataInputStream inputStream = fs.open(path);
        byte[] buffer = new byte[inputStream.available()];

        IOUtils.readFully(inputStream, buffer, 0, inputStream.available());
        return Arrays.copyOfRange(buffer, offset, offset + length);
    }

    public void writeFile(final String filePath, final byte[] content) throws java.io.IOException {
        Path path = new Path(filePath);

        if(fs.exists(path)) {
            System.out.println("File already exists");
            return;
        }

        FSDataOutputStream outputStream = fs.create(path);
        outputStream.write(content);
        outputStream.close();
    }

    public void deleteFile(final String filePath) throws java.io.IOException {
        Path path = new Path(filePath);

        if(!fs.exists(path)){
            System.out.println("File does not exist");
            return;
        }

        fs.delete(path, true);
    }

    public void updateFile(final String filePath, final byte[] content) throws java.io.IOException {
        deleteFile(filePath);
        writeFile(filePath, content);
    }

    //append is dangerous & not working as of Hadoop-Hdfs version 2.6.0
    public void appendFile(final String filePath, final byte[] content) throws java.io.IOException {
        Path path = new Path(filePath);

        //code to be changed if append is used 'String' is going to be passed
        String tempContent = new String(content);

        FSDataOutputStream outputStream = fs.append(path);
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.append(tempContent);
        printWriter.close();

    }

    public void closeFileSystem() throws java.io.IOException {
        fs.close();
    }
}