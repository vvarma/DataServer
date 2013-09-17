package com.nvr.data.repository;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 3:48 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/testContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TestIndexDao {
    @Autowired
    IndexDao indexDao;
    @Test
    public void shouldCreateIndex(){
        Indice indice =new Indice("nifty");
        indexDao.save(indice);
        //Assert.assertTrue(indexDao.findAll().size()>0);
        Assert.assertNotNull(indexDao.findOne("nifty"));

    }
    @Test
    public  void shouldCreateIndexSecurities(){
        Security security1=new Security("CIPLA","EQ","ISIN");
        Security security2=new Security("AMTEK","EQ","ISO");
        Indice indice =new Indice("sifty");
        indice.addSecurity(security1);
        security1.addIndex(indice);
        indice.addSecurity(security2);
        security2.addIndex(indice);
        indexDao.save(indice);
        Indice indice1=indexDao.findOne("sifty");
        Indice indice2=indexDao.findOne("nifty");
        Assert.assertEquals(indice1,indice);
        Assert.assertNull(indice2);
        Assert.assertTrue(indice1.getSecurities().size()>0);
    }
}
