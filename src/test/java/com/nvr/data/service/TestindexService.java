package com.nvr.data.service;

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
 * Date: 9/16/13
 * Time: 2:02 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TestindexService {
    @Autowired
    IndexService indexService;
    @Test
    public void shouldGetAllIndice(){
        indexService.loadIndice();
        Assert.assertTrue(indexService.getAllIndice().size()>0);
    }
}
