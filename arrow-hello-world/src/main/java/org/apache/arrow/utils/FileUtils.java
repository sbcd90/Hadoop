package org.apache.arrow.utils;

import java.io.File;

public class FileUtils {

    public File validateFile(String fileName) {
        String path = getClass().getClassLoader().getResource("arrow").getPath();
        path = path.substring(0, path.lastIndexOf("/"));
        File file = new File(path + File.separator + fileName);
        return file;
    }
}