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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
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
    ThreadPoolTaskExecutor threadPoolTaskExecutor;



    @Test
    public void shouldConcurrentlyProcessSecurityAndIndex(){
        CountDownLatch latch=new CountDownLatch(2);
        Map<String,String> indexParamMap=new HashMap<String, String>();
        Map<String,String> securityParamMap=new HashMap<String, String>();
        indexParamMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        securityParamMap.put("seedUrl","http://nseindia.com/content/equities/EQUITY_L.csv");
        securityParamMap.put("index","nifty");
        ThreadedIndexLoader threadedIndexLoader=new ThreadedIndexLoader(indexParamMap,"indices.csv",latch);
        ThreadedSecurityLoader threadedSecurityLoader=new ThreadedSecurityLoader(securityParamMap,"securities.csv",latch);
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
        CountDownLatch latch=new CountDownLatch(2);
        Map<String,String> indexParamMap=new HashMap<String, String>();
        indexParamMap.put("seedUrl","http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        ThreadedIndexLoader threadedIndexLoader=new ThreadedIndexLoader(indexParamMap,"indices.csv",latch);
        Future<List<Indice>>  futureIndices=threadPoolTaskExecutor.submit(threadedIndexLoader);
        List<Future<List<Security>>> futuresSecurityList=new ArrayList<Future<List<Security>>>();
        try {
            List<Indice> indices=futureIndices.get();
            TaskExecutorCompletionService completionService=new TaskExecutorCompletionService(threadPoolTaskExecutor);
            CountDownLatch latch2=new CountDownLatch(indices.size());
            for (Indice indice:indices){

                Map<String,String> securityIndexParamMap=new HashMap<String, String>();
                securityIndexParamMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_");
                securityIndexParamMap.put("indice", indice.getIndexName());
                securityIndexParamMap.put("tailUrl", "list.csv");
                String fileName=indice.getIndexName()+".csv";
                ThreadedSecurityIndexLoader threadedSecurityIndexLoader=new ThreadedSecurityIndexLoader(securityIndexParamMap,fileName,latch2);
                futuresSecurityList.add(completionService.submit(threadedSecurityIndexLoader));
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
    @Test
    public void shouldConcurrentlyDownloadPriceDataForSecuritiesInIndex(){
        Map<String,String> indexParamMap=new HashMap<String, String>();
        indexParamMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        ThreadedIndexLoader threadedIndexLoader=new ThreadedIndexLoader(indexParamMap,"indices.csv",new CountDownLatch(1));
        Future<List<Indice>>  futureIndices=threadPoolTaskExecutor.submit(threadedIndexLoader);
        List<Future<List<Security>>> futuresSecurityList=new ArrayList<Future<List<Security>>>();
        SimpleDateFormat fmt=new SimpleDateFormat("dd-MMM-yyyy");
        try {
            List<Indice> indices=futureIndices.get();
            TaskExecutorCompletionService completionService=new TaskExecutorCompletionService(threadPoolTaskExecutor);
            CountDownLatch latch=new CountDownLatch(indices.size());
            for (Indice indice:indices){

                Map<String,String> securityIndexParamMap=new HashMap<String, String>();
                securityIndexParamMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_");
                securityIndexParamMap.put("indice", indice.getIndexName());
                securityIndexParamMap.put("tailUrl", "list.csv");
                String fileName=indice.getIndexName()+".csv";
                ThreadedSecurityIndexLoader securityIndexLoader=new ThreadedSecurityIndexLoader(securityIndexParamMap,fileName,latch);
                securityIndexLoader.setIndice(indice);
                futuresSecurityList.add(completionService.submit(securityIndexLoader));
            }
            Future<List<Security>>  listFutureSecurities;
            while(completionService.size()!=0){
                listFutureSecurities=completionService.take();
                System.out.println(listFutureSecurities.get());
            }
            Set<Security> securitySet=new HashSet<Security>();
            for (Indice indice:indices){
                securitySet.addAll(indice.getSecurities());
            }
            System.out.println("1234" +securitySet);
            CountDownLatch latch1=new CountDownLatch(securitySet.size());
            for (Security security:securitySet){
                Map<String,String> pricedParamMap=new HashMap<String, String>();
                pricedParamMap.put("seedUrl", "http://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?");
                pricedParamMap.put("symbol", security.getSymbol());
                pricedParamMap.put("fromDate", fmt.format(security.getListing()));
                pricedParamMap.put("toDate", fmt.format(new Date()));
                ThreadedPriceLoader threadedPriceLoader=new ThreadedPriceLoader(pricedParamMap,security.getSymbol()+".csv",latch1, security);
                completionService.submit(threadedPriceLoader);
            }
            while (completionService.size()!=0){
                System.out.println(completionService.take().get());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
