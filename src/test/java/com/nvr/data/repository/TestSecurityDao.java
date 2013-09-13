package com.nvr.data.repository;


import com.nvr.data.domain.Security;
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
@ContextConfiguration(locations = {"classpath:META-INF/spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TestSecurityDao {
    @Autowired
    SecurityDao securityDao;

    @Test
    public void shouldCreateSecurity(){
        Security security=new Security("CIPLA","EQ","ISI");
        securityDao.save(security);
    }

}
