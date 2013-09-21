package com.nvr.data.repository;


import com.nvr.data.domain.*;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        securityJpaDao.save(security);
        Assert.assertTrue(securityJpaDao.findAll().size()>0);
    }
    @Test
    public void shouldCreatePricedSecurity(){
        Security security=new Security("FTS","EQ","ISIG");
        List<Price> prices=new ArrayList<Price>();
        prices.add(new Price(10.0,10.0,1.0,1.0,2.0,200,new Date()));
        security.setPrices(prices);
        securityJpaDao.save(security);
        Assert.assertTrue(securityJpaDao.findAll().size()>0);
        System.out.println(securityJpaDao.findAll());
    }
    @Test
    public void testFindByIndex(){
        Security security=new Security("FTS","EQ","ISIN");
        Security security1=new Security("Blah","MQ","KJSD");
        Security security2=new Security("Sad","EQ","Sleepy");
        Indice indice=new Indice("why");
        Indice indice2=new Indice("what");
        Indice indice3=new Indice("how");
        Indice indice4=new Indice("when");
        indice.addSecurity(security);
        security.addIndex(indice);
        indice.addSecurity(security1);
        security1.addIndex(indice);
        indice.addSecurity(security2);
        security2.addIndex(indice);
        indice2.addSecurity(security1);
        security1.addIndex(indice2);
        indice3.addSecurity(security);
        security.addIndex(indice3);
        indice3.addSecurity(security2);
        security2.addIndex(indice3);
        securityJpaDao.save(security);
        securityJpaDao.save(security1);
        securityJpaDao.save(security2);

        Assert.assertTrue(securityJpaDao.findByIndice(indice.getIndexName()).size()==3);
        Assert.assertTrue(securityJpaDao.findByIndice(indice2.getIndexName()).size()==1);
        Assert.assertTrue(securityJpaDao.findByIndice(indice3.getIndexName()).size()==2);
        Assert.assertTrue(securityJpaDao.findByIndice(indice4.getIndexName()).size()==0);

    }
    @Test
    public void findByPriced(){
        Security security=new Security("FTS","EQ","ISIG");
        Security security2=new Security("Sad","EQ","Sleepy");
        List<Price> prices=new ArrayList<Price>();
        prices.add(new Price(10.0,10.0,1.0,1.0,2.0,200,new Date()));
        security.setPrices(prices);
        securityJpaDao.save(security);
        securityJpaDao.save(security2);
        Assert.assertEquals(securityJpaDao.findByPriced(Boolean.TRUE).get(0), security);
        Assert.assertEquals(securityJpaDao.findByPriced(Boolean.FALSE).get(0),security2);
    }
    @Test
    public void findPriceByDate(){
        Calendar calendar=new GregorianCalendar();
        Calendar calendar1=new GregorianCalendar();
        Calendar calendar2=new GregorianCalendar();
        calendar1.set(2013,02,01);
        calendar2.set(2013,05,05);
        Security security=new Security("FTS","EQ","ISIG");
         List<Price> prices=new ArrayList<Price>();
        Date date=calendar2.getTime();
        Date date1=calendar1.getTime();
        Date date2=calendar.getTime();
        Price price=new Price(10.0,10.0,1.0,1.0,2.0,200,date);
        price.setSecurity(security);
        prices.add(price);
        security.setPrices(prices);
        securityJpaDao.save(security);
        System.out.println(securityJpaDao.getSecurityPricesBetween("FTS","EQ",date1,date2));
        Assert.assertTrue(securityJpaDao.getSecurityPricesBetween("FTS","EQ",date1,date2).size()>0);

    }
}
