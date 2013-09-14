package com.nvr.data.loader;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

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
public class SecurityIndexLoaderTest {
    List<Security> securities;
    SecurityLoader securityLoader;
    IndexLoader indexLoader;
    List<Indice> indices;
    @Before
    public void initSecurityListAndIndexList(){
        securityLoader=new SecurityLoader();
        indexLoader=new IndexLoader();
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
        SecurityIndexLoader loader=new SecurityIndexLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        for (Indice indice :indices){
            paramMap.put("exchange","nse");
            paramMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_cnx");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl","list.csv");
            try {
                URL url=loader.generateUrlGivenParamMap(paramMap);
                Assert.assertNotNull(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
    @Test
    public void shouldDownloadFileGivenUrl(){
        SecurityIndexLoader loader=new SecurityIndexLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        for (Indice indice :indices){
            paramMap.put("exchange","nse");
            paramMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_cnx");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl","list.csv");
            try {
                URL url=loader.generateUrlGivenParamMap(paramMap);
                Assert.assertNotNull(url);
                String fileName=loader.downloadFileGivenUrl(url, indice.getIndexName()+".csv");
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    @Test
    public void shouldParseFileAndGetListOfIndicesWithSecurityInfo(){
        SecurityIndexLoader loader=new SecurityIndexLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        for (Indice indice :indices){
            paramMap.put("exchange","nse");
            paramMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl","list.csv");
            try {
                URL url=loader.generateUrlGivenParamMap(paramMap);
                Assert.assertNotNull(url);
                String fileName= null;
                try {
                    fileName = loader.downloadFileGivenUrl(url, indice.getIndexName()+".csv");
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                List<Security> fakeSecurities=loader.parseFileAndReturnListOfEntity(fileName);
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
