package com.nvr.data.loader;

import com.nvr.data.annotation.AppTest;
import com.nvr.data.domain.Price;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/18/13
 * Time: 4:21 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/testContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class PriceLoaderTest {
    @Autowired
    @Qualifier(value = "priceLoader")
    Loader loader;
    @Test
    public void shouldGenerateUrl(){
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("seedUrl","http://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?");
        paramMap.put("symbol","CIPLA");
        paramMap.put("fromDate","08-Feb-1995");
        paramMap.put("toDate","19-Aug-2013");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            System.out.println(url);
            Assert.assertNotNull(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldDownloadFileGivenUrl(){
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("seedUrl","http://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?");
        paramMap.put("symbol","CIPLA");
        paramMap.put("fromDate","08-Feb-1995");
        paramMap.put("toDate","19-Aug-2013");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            String fileName=loader.downloadFileGivenUrl(url,"CIPLA.csv");
            File file=new File(fileName);
            Assert.assertTrue(file.exists());
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldParseCsvGivenFilename(){
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("seedUrl","http://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?");
        paramMap.put("symbol","CIPLA");
        paramMap.put("fromDate","08-Feb-1995");
        paramMap.put("toDate","19-Aug-2013");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            String fileName=loader.downloadFileGivenUrl(url,"CIPLA.csv");
            List<Price> prices=loader.parseFileAndReturnListOfEntity(fileName);
            Assert.assertTrue(prices.size()>0);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
