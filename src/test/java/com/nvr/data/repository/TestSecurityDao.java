package com.nvr.data.repository;


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
 * Time: 2:01 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TestSecurityDao {
    @Autowired
    SecurityJpaDao securityJpaDao;

    @Test
    public void shouldCreateSecurity(){
        Security security=new Security("CIPLA","EQ","ISI");
        securityJpaDao.save(security);
        Assert.assertTrue(securityJpaDao.findAll().size()>0);
    }

}
