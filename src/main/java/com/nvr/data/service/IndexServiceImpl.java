package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import com.nvr.data.repository.IndexDao;
import com.nvr.data.service.annotation.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
    IndexDao indexDao;

    public static Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Override
    public List<Indice> getAllIndice() {
        return indexDao.findAll();
    }

    @Override
    public List<Security> getAllSecurity(String indiceName) {
        Indice indice = indexDao.findOne(indiceName);
        List<Security> securities = indice.getSecurities();
        for (Security s : securities) ;
        return securities;
    }


}
