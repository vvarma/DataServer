package com.nvr.data.service;

import com.nvr.data.domain.Security;
import com.nvr.data.loader.Loader;
import com.nvr.data.repository.SecurityJpaDao;
import com.nvr.data.service.annotation.AppService;
import com.nvr.data.service.annotation.PostInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:14 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    @Qualifier("securityLoader")
    Loader loader;
    @Autowired
    SecurityJpaDao securityJpaDao;
    final static Logger LOGGER= LoggerFactory.getLogger(SecurityService.class);

    @Override
    @PostInitialize(order = 1)
     public void loadSecurities(){
        LOGGER.debug("Loading security");
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("exchange", "nse");
        String fileName;
        try{
            URL url = loader.generateUrlGivenParamMap(paramMap);
            LOGGER.debug("Generated Url " + url.getPath());
            if (url != null) {
                fileName = loader.downloadFileGivenUrl(url, "securities.csv");
                LOGGER.debug("filed downloaded @ "+fileName);
                List<Security> securities = loader.parseFileAndReturnListOfEntity(fileName);
                LOGGER.debug("securities found " + securities.size());
                for (Security security:securities){
                    securityJpaDao.save(security);
                }

        }


        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        LOGGER.debug("Security Service done");
    }

    @Override
    public List<Security> getAllSecurities() {
        return securityJpaDao.findAll();
    }
}
