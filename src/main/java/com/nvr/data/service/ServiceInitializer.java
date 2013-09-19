package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Price;
import com.nvr.data.domain.PricedSecurity;
import com.nvr.data.domain.Security;
import com.nvr.data.loader.Loader;
import com.nvr.data.repository.IndexDao;
import com.nvr.data.repository.SecurityJpaDao;
import com.nvr.data.service.annotation.PostInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/17/13
 * Time: 7:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ServiceInitializer {
    @Autowired
    SecurityJpaDao securityJpaDao;
    @Autowired
    IndexDao indexDao;
    @Autowired
    @Qualifier(value = "securityLoader")
    Loader securityLoader;
    @Autowired
    @Qualifier(value = "indexLoader")
    Loader indexLoader;
    @Autowired
    @Qualifier(value = "securityIndexLoader")
    Loader securityIndexLoader;
    final static Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    @Autowired
    @Qualifier(value = "priceLoader")
    Loader priceLoader;

    /*@PostInitialize*/
    public void initServices() {
        List<Security>securities=loadSecurity();
        List<Indice> indices = loadIndice();
        loadSecurityIndex();
        /*Set<PricedSecurity>securitySet=loadSecurityPrices();
        *//*for (Security s:securitySet){
            securityJpaDao.save(s);
        }*/

    }

    private List<Security> loadSecurity() {
        LOGGER.debug("Loading security");
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("seedUrl", "http://nseindia.com/content/equities/EQUITY_L.csv");
        String fileName;
        List<Security> securities = null;
        try {
            URL url = securityLoader.generateUrlGivenParamMap(paramMap);
            LOGGER.debug("Generated Url " + url.getPath());
            if (url != null) {
                fileName = securityLoader.downloadFileGivenUrl(url, "securities.csv");
                LOGGER.debug("filed downloaded @ " + fileName);
                securities = securityLoader.parseFileAndReturnListOfEntity(fileName);
                LOGGER.debug("securities found " + securities.size());


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        LOGGER.debug("Security Service done");
        for (Security s:securities){
            securityJpaDao.save(new PricedSecurity(s));
        }
        return securities;
    }

    public List<Indice> loadIndice() {
        LOGGER.info("Initializing Indices");
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        String fileName;
        List<Indice> indices = null;
        try {
            URL url = indexLoader.generateUrlGivenParamMap(paramMap);

            if (url != null) {
                fileName = indexLoader.downloadFileGivenUrl(url, "indices.csv");
                indices = indexLoader.parseFileAndReturnListOfEntity(fileName);

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for (Indice i:indices){
            indexDao.save(i);
        }
        return indices;
    }

    private void loadSecurityIndex() {
        List<PricedSecurity> securities=securityJpaDao.findAll();
        List<Indice> indices=indexDao.findAll();
        LOGGER.info("Initializing securities in indices");
        Map<String, String> paramMap = new HashMap<String, String>();
        if (securities==null||indices==null){
            LOGGER.error("Null Pointer");
            return;
        }
        for (Indice indice : indices) {
            paramMap.put("exchange", "nse");
            paramMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl", "list.csv");
            try {
                URL url = securityIndexLoader.generateUrlGivenParamMap(paramMap);
                String fileName = null;
                try {
                    fileName = securityIndexLoader.downloadFileGivenUrl(url, indice.getIndexName() + ".csv");
                } catch (IOException e) {

                    continue;
                }
                List<Security> fakeSecurities = securityIndexLoader.parseFileAndReturnListOfEntity(fileName);
                for (Security s : fakeSecurities) {
                    if (securities.contains(s)) {
                        int i = securities.indexOf(s);
                        indice.addSecurity(securities.get(i));
                        securities.get(i).addIndex(indice);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            indexDao.update(indice);
        }
    }

}
