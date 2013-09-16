package com.nvr.data.service;

import com.nvr.data.domain.Security;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/16/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TestSecurityService {
    @Autowired
    SecurityService securityService;
    @Test
    public void shouldLoadAllSecurity() throws IOException, ParseException {
        securityService.loadSecurities();
        List<Security> securityList=securityService.getAllSecurities();
        System.out.println(securityList.size());
        Assert.assertTrue(securityList.size()>0);
    }
}
