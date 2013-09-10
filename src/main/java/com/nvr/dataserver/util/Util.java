package com.nvr.dataserver.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static boolean downloadCsvGivenUrl(String path, URL url) throws IOException {
        url.openConnection();
        InputStream reader=null;
        try {
            reader = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FileOutputStream writer = new FileOutputStream(path);
        byte[] buffer = new byte[153600];
        int totalBytesRead = 0;
        int bytesRead = 0;
        while ((bytesRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, bytesRead);
            buffer = new byte[153600];
            totalBytesRead += bytesRead;
        }
        long endTime = System.currentTimeMillis();
        writer.close();
        reader.close();
        return true;
    }
    public static Class parseHeaderOfCsv(String fileName){
        return null;
    }
}
