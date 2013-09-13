package com.nvr.data.loader;

import com.nvr.data.domain.Index;
import com.nvr.data.loader.IndexLoader;
import com.nvr.data.loader.Loader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/10/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexLoaderTest {

    @Test
    public void shouldGenerateUrlAndTestConnection(){
        Loader loader=new IndexLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
        URL url=loader.generateUrlGivenParamMap(paramMap);
        Assert.assertNotNull(url);

            if (url!=null)
            url.openConnection();
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shoudlDownloadFileGivenUrl(){
        Loader loader=new IndexLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);

            if (url!=null)
                loader.downloadFileGivenUrl(url,"indices.csv");
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldParseFileAndGetIndices(){
        Loader loader=new IndexLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        String fileName;
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);

            if (url!=null){
                fileName=loader.downloadFileGivenUrl(url,"indices.csv");
                List<Index> indices= loader.parseFileAndReturnListOfEntity(fileName);
                if (indices.size()<=0){
                    Assert.assertTrue(false);
                }
            }
        } catch (IOException e) {
            Assert.assertTrue(false);
<<<<<<< HEAD:test/main/java/IndexLoaderTest.java
            e.printStackTrace();
=======
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
>>>>>>> 066316b862322b5590015c6bd86003581c6c5cac:test/main/java/com/nvr/data/loader/IndexLoaderTest.java
        }
    }
}
