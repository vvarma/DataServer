package com.nvr.dataserver.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/2/13
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParseCSV {
    Properties properties=new Properties();

    public ParseCSV() throws IOException {
        properties.load(getClass().getResourceAsStream("/filepath.properties"));
        properties.load(getClass().getResourceAsStream("/url.properties"));
    }

    public<T> List<T> getListOfEntity(String fileName){
        return null;
    }
    public Class getEntityType(String fileName){
        return null;
    }
    public int parseHeaderAndReturnLineNumber(String fileName){
        return 0;
    }
    public String downloadFileGivenName(String fileName){
        properties.getProperty("");
        return null;
    }
}
