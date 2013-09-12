package com.nvr.data.loader;

import java.io.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/10/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLoader implements Loader{
    @Override
    public String downloadFileGivenUrl(URL url, String fileName) throws IOException {

        url.openConnection();
        InputStream reader = url.openStream();
        String path=System.getProperty("java.io.tmpdir")+fileName;
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
        System.out.println(path);
        writer.close();
        reader.close();

        return path;
    }
    protected int findHeaderGivenFile(File file) throws IOException {
        int headerLine=-1;
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line;

        while ((line=br.readLine())!=null){
            headerLine++;
            String[] strArr=line.split(",");
            if (strArr.length>1){
                break;
            }
        }
        return headerLine;
    }
}
