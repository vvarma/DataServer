package com.nvr.data.repository;


import com.nvr.data.domain.Price;
import com.nvr.data.domain.PricedSecurity;
import com.nvr.data.domain.Security;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 2:01 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/testContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TestSecurityDao {
    @Autowired
    SecurityJpaDao securityJpaDao;

    @Test
    public void shouldCreateSecurity(){

        Security security=new Security("SIPLA","EQ","ISI");
        PricedSecurity pricedSecurity=new PricedSecurity(security);
        securityJpaDao.save(pricedSecurity);
        Assert.assertTrue(securityJpaDao.findAll().size()>0);
    }
    @Test
    public void shouldCreatePricedSecurity(){
        Security security=new Security("FTS","EQ","ISIG");
        PricedSecurity pricedSecurity=new PricedSecurity(security);
        List<Price> prices=new ArrayList<Price>();
        prices.add(new Price(10.0,10.0,1.0,1.0,2.0,200,new Date()));
        pricedSecurity.setPrices(prices);
        securityJpaDao.save(pricedSecurity);
        Assert.assertTrue(securityJpaDao.findAll().size()>0);
        System.out.println(securityJpaDao.findAll());
    }

}
