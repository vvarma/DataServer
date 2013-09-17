package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import com.nvr.data.loader.Loader;
import com.nvr.data.repository.IndexDao;
import com.nvr.data.service.annotation.AppService;
import com.nvr.data.service.annotation.PostInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
 * Date: 9/16/13
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
@AppService
public class IndexServiceImpl implements IndexService {
    @Autowired
    @Qualifier(value = "indexLoader")
    Loader loader;
    @Autowired
    @Qualifier(value = "securityIndexLoader")
    Loader loader2;
    @Autowired
    IndexDao indexDao;
    @Autowired
    SecurityService securityService;
    public static Logger logger= LoggerFactory.getLogger(IndexService.class);

    @Override
    public List<Indice> getAllIndice() {
        return indexDao.findAll();
    }

    @Override
    public List<Security> getAllSecurity(Indice indice) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
