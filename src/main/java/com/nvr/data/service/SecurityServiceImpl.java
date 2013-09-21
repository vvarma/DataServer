package com.nvr.data.service;

import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;
import com.nvr.data.domain.SecurityId;
import com.nvr.data.repository.SecurityJpaDao;
import com.nvr.data.service.annotation.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

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
    SecurityJpaDao securityJpaDao;
    final static Logger LOGGER= LoggerFactory.getLogger(SecurityService.class);


    @Override
    public List<Security> getAllSecurities() {
        List<Security> securities=securityJpaDao.findAll();
        return  securities;
    }

    @Override
    public List<Price> getPriceForSecurity(String symbol, Date fromDate, Date toDate) {
        return securityJpaDao.getSecurityPricesBetween(symbol,"EQ",fromDate,toDate);
    }

    @Override
    public List<Security> getAllPricedSecurities() {
        return securityJpaDao.findByPriced(true);
    }

    @Override
    public List<Price> getPriceForSecurity(String symbol, String series, Date fromDate, Date toDate) {
        return securityJpaDao.getSecurityPricesBetween(symbol,series,fromDate,toDate);
    }

    @Override
    public Security getSecurity(String symbol) {
        SecurityId securityId=new SecurityId();
        securityId.setSeries("EQ");
        securityId.setSymbol(symbol);
        return securityJpaDao.findOne(securityId);
    }

    @Override
    public Security getSecurity(String symbol, String series) {
        SecurityId securityId=new SecurityId();
        securityId.setSeries(series);
        securityId.setSymbol(symbol);
        return securityJpaDao.findOne(securityId);
    }
}
