package com.nvr.data.loader;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
 * Date: 9/12/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class SecurityIndexLoaderTest {
    List<Security> securities;
    @Autowired
    @Qualifier(value = "securityLoader")
    Loader securityLoader;
    @Autowired
    @Qualifier(value = "indexLoader")
    Loader indexLoader;
    @Autowired
    @Qualifier(value = "securityIndexLoader")
    Loader securityIndexLoader;
    List<Indice> indices;
    @Before
    public void initSecurityListAndIndexList(){
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
            URL url=securityLoader.generateUrlGivenParamMap(paramMap);
            String fileName=securityLoader.downloadFileGivenUrl(url,"securities.csv");
            securities=securityLoader.parseFileAndReturnListOfEntity(fileName);
            url=indexLoader.generateUrlGivenParamMap(paramMap);
            fileName=indexLoader.downloadFileGivenUrl(url,"indices.csv");
            indices=indexLoader.parseFileAndReturnListOfEntity(fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void shouldGenerateUrlGivenIndexName(){
        Map<String,String> paramMap=new HashMap<String, String>();
        for (Indice indice :indices){
            paramMap.put("exchange","nse");
            paramMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_cnx");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl","list.csv");
            try {
                URL url=securityIndexLoader.generateUrlGivenParamMap(paramMap);
                Assert.assertNotNull(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
    @Test
    public void shouldDownloadFileGivenUrl(){
        Map<String,String> paramMap=new HashMap<String, String>();
        for (Indice indice :indices){
            paramMap.put("exchange","nse");
            paramMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_cnx");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl","list.csv");
            try {
                URL url=securityIndexLoader.generateUrlGivenParamMap(paramMap);
                Assert.assertNotNull(url);
                String fileName=securityIndexLoader.downloadFileGivenUrl(url, indice.getIndexName()+".csv");
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    @Test
    public void shouldParseFileAndGetListOfIndicesWithSecurityInfo(){
        Map<String,String> paramMap=new HashMap<String, String>();
        for (Indice indice :indices){
            paramMap.put("exchange","nse");
            paramMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl","list.csv");
            try {
                URL url=securityIndexLoader.generateUrlGivenParamMap(paramMap);
                Assert.assertNotNull(url);
                String fileName= null;
                try {
                    fileName = securityIndexLoader.downloadFileGivenUrl(url, indice.getIndexName()+".csv");
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<Security> fakeSecurities=securityIndexLoader.parseFileAndReturnListOfEntity(fileName);
                for (Security s:fakeSecurities){
                    if (securities.contains(s)){
                        int i=securities.indexOf(s);
                        indice.addSecurity(securities.get(i));
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        for (Indice indice :indices){
            System.out.println("Indice " + indice.getIndexName()+ " no of securities "+ indice.getSecurities().size());
        }
    }
}
