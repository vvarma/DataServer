package com.nvr.data.loader;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/19/13
 * Time: 4:31 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/testContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ThreadedLoaderTest {
    @Autowired
    ThreadedSecurityLoader threadedSecurityLoader;
    @Autowired
    ThreadedIndexLoader threadedIndexLoader;
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Test
    public void shouldConcurrentlyProcessSecurityAndIndex(){
        Map<String,String> indexParamMap=new HashMap<String, String>();
        Map<String,String> securityParamMap=new HashMap<String, String>();
        indexParamMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        securityParamMap.put("seedUrl","http://nseindia.com/content/equities/EQUITY_L.csv");
        securityParamMap.put("index","nifty");
        threadedIndexLoader.setFileName("index.csv");
        threadedIndexLoader.setParamMap(indexParamMap);
        threadedSecurityLoader.setFileName("securities.csv");
        threadedSecurityLoader.setParamMap(securityParamMap);
        TaskExecutorCompletionService completionService=new TaskExecutorCompletionService(threadPoolTaskExecutor);
        Future<List<Security>> futureSecurities= completionService.submit (threadedSecurityLoader);
        Future<List<Indice>>  futureIndices=completionService.submit(threadedIndexLoader);
        try {
            List<Indice> indices=futureIndices.get();
            List<Security> securities=futureSecurities.get();

            System.out.println(securities);
            System.out.println(indices);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    @Test
    public void shouldConcurrentlyProcessIndexAndItsSecurtites(){
        Map<String,String> indexParamMap=new HashMap<String, String>();
        indexParamMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        threadedIndexLoader.setFileName("index.csv");
        threadedIndexLoader.setParamMap(indexParamMap);
        Future<List<Indice>>  futureIndices=threadPoolTaskExecutor.submit(threadedIndexLoader);
        List<Future<List<Security>>> futuresSecurityList=new ArrayList<Future<List<Security>>>();
        try {
            List<Indice> indices=futureIndices.get();
            TaskExecutorCompletionService completionService=new TaskExecutorCompletionService(threadPoolTaskExecutor);
            for (Indice indice:indices){
                ThreadedSecurityIndexLoader securityIndexLoader=new ThreadedSecurityIndexLoader();
                Map<String,String> securityIndexParamMap=new HashMap<String, String>();
                securityIndexParamMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_");
                securityIndexParamMap.put("indice", indice.getIndexName());
                securityIndexParamMap.put("tailUrl", "list.csv");
                String fileName=indice.getIndexName()+".csv";
                securityIndexLoader.setParamMap(securityIndexParamMap);
                securityIndexLoader.setFileName(fileName);
                futuresSecurityList.add(completionService.submit(securityIndexLoader));
            }
            Future<List<Security>>  listFutureSecurities;
            while(completionService.size()!=0){
                listFutureSecurities=completionService.take();
                System.out.println(listFutureSecurities.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
