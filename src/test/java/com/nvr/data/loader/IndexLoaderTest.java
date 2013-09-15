package com.nvr.data.loader;

import com.nvr.data.domain.Indice;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class IndexLoaderTest {
    @Autowired
    @Qualifier(value = "indexLoader")
    Loader loader;

    @Test
    public void shouldGenerateUrlAndTestConnection(){
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
        URL url= loader.generateUrlGivenParamMap(paramMap);
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
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
            URL url= loader.generateUrlGivenParamMap(paramMap);
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
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        String fileName;
        try {
            URL url= loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);

            if (url!=null){
                fileName= loader.downloadFileGivenUrl(url,"indices.csv");
                List<Indice> indices= loader.parseFileAndReturnListOfEntity(fileName);
                if (indices.size()<=0){
                    Assert.assertTrue(false);
                }
            }
        } catch (IOException e) {
            Assert.assertTrue(false);

            e.printStackTrace();

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
