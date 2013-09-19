package com.nvr.data.loader;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/19/13
 * Time: 3:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Scope("prototype")
public abstract class  ThreadedLoader<T> extends AbstractLoader<T> implements Callable<List<T>> {
    Map<String,String> paramMap=new HashMap<String, String>();
    String fileName;
    @Override
    public List<T> call() {
        List<T> tList=null;
        try {
            URL url =generateUrlGivenParamMap(paramMap);
            fileName=downloadFileGivenUrl(url,fileName);
            tList=parseFileAndReturnListOfEntity(fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return tList;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
