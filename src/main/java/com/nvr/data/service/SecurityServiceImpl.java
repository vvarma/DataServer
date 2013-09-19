package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Price;
import com.nvr.data.domain.PricedSecurity;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:14 AM
 * To change this template use File | Settings | File Templates.
 */
@AppService
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    @Qualifier("securityLoader")
    Loader loader;
    @Autowired
    SecurityJpaDao securityJpaDao;
    final static Logger LOGGER= LoggerFactory.getLogger(SecurityService.class);


    @Override
    public List<Security> getAllSecurities() {
        List<PricedSecurity> pricedSecurities=securityJpaDao.findAll();
        List<Security> securities=new ArrayList<Security>();
        for (PricedSecurity pricedSecurity:pricedSecurities){
            securities.add(pricedSecurity);
        }
        return  securities;
    }

}
