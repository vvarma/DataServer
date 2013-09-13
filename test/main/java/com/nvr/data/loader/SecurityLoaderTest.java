package com.nvr.data.loader;

import com.nvr.data.domain.Security;
import com.nvr.data.loader.SecurityLoader;
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
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecurityLoaderTest {
    @Test
    public void shouldGenerateUrlGivenExchangeAndIndexName(){
        SecurityLoader loader=new SecurityLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        paramMap.put("index","nifty");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);
            if (url!=null)
            url.openConnection();


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldDownloadFileGivenUrl(){
        SecurityLoader loader=new SecurityLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);
            if (url!=null)
                loader.downloadFileGivenUrl(url,"securities.csv");


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldParseFileAndLoadSecurities(){
        SecurityLoader loader=new SecurityLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        String fileName;
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);
            if (url!=null){
                fileName=loader.downloadFileGivenUrl(url,"securities.csv");
                List<Security> securities=loader.parseFileAndReturnListOfEntity(fileName);
                if (securities.size()<=0){
                    Assert.assertTrue(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
