package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import com.nvr.data.loader.*;
import com.nvr.data.repository.SecurityJpaDao;
import com.nvr.data.service.annotation.PostInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/17/13
 * Time: 7:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ServiceInitializer {
    TaskExecutorCompletionService completionService;
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    SecurityJpaDao securityJpaDao;
    @Autowired
    SecurityService securityService;

    final static Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);


    public ServiceInitializer() {
        completionService = new TaskExecutorCompletionService(threadPoolTaskExecutor);
    }

    @PostInitialize(order = 1)
    public void initServices() {
        LOGGER.info("Initialising..");
        List<Security> securities = securityJpaDao.findAll();
        if (securities.isEmpty()) {
            fullInitialise();
        }
        LOGGER.info("First step completed..");
    }

    @PostInitialize(order = 2)
    public void initServicesP2() {
        fullInitialise2();
        LOGGER.info("Initialising completed.");

    }

    private void fullInitialise() {
        LOGGER.info("Initialising Securities and Indices..");
        CountDownLatch firstLatch = new CountDownLatch(2);
        Future<List<Security>> listFutureSecurity = completionService.submit(getSecurityWorker(firstLatch));
        Future<List<Indice>> listFutureIndice = completionService.submit(getIndexWorker(firstLatch));
        try {
            firstLatch.await();
            LOGGER.info("Forming relations: Security-Indice");
            List<Security> securities = listFutureSecurity.get();
            List<Indice> indices = listFutureIndice.get();
            CountDownLatch secondLatch = new CountDownLatch(indices.size());
            for (Indice indice : indices) {
                completionService.submit(getSecurityIndexWorker(indice, securities, secondLatch));
            }
            secondLatch.await();
            for (Indice indice : indices) {
                for (Security security : indice.getSecurities()) {
                    security.addIndex(indice);
                }
            }
            LOGGER.info("Saving to Database..");
            for (Security security : securities) {
                securityJpaDao.save(security);
                System.out.println(security);
            }
            LOGGER.info("Security Indice Initiation Complete");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void fullInitialise2() {
        try {
            LOGGER.info("Initialising Security Prices..");
            List<Security> securities = securityJpaDao.findAll();

            Set<Security> securitySet = new HashSet<Security>();
            for (Security security : securities) {
                if (!security.getIndiceList().isEmpty() && !security.isPriced()) {
                    securitySet.add(security);
                }
            }


            CountDownLatch thirdLatch = new CountDownLatch(securitySet.size());
            for (Security security : securitySet) {
                completionService.submit(getPricedSecurityWorker(security, thirdLatch));
            }

            thirdLatch.await();
            LOGGER.info("Saving to Database.");
            for (Security security : securitySet) {
                securityJpaDao.save(security);
            }
            LOGGER.info("Security Price initiation complete..");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private ThreadedPriceLoader getPricedSecurityWorker(Security security, CountDownLatch thirdLatch) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("seedUrl", "http://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?");
        paramMap.put("symbol", security.getSymbol());
        paramMap.put("fromDate", fmt.format(security.getListing()));
        paramMap.put("toDate", fmt.format(new Date()));
        ThreadedPriceLoader threadedPriceLoader = new ThreadedPriceLoader(paramMap, security.getSymbol() + ".csv", thirdLatch, security);
        return threadedPriceLoader;
    }

    private ThreadedSecurityLoader getSecurityWorker(CountDownLatch latch) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("seedUrl", "http://nseindia.com/content/equities/EQUITY_L.csv");
        ThreadedSecurityLoader threadedSecurityLoader = new ThreadedSecurityLoader(paramMap, "securities.csv", latch);
        return threadedSecurityLoader;
    }


    private ThreadedIndexLoader getIndexWorker(CountDownLatch latch) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        ThreadedIndexLoader threadedIndexLoader = new ThreadedIndexLoader(paramMap, "indices.csv", latch);
        return threadedIndexLoader;

    }

    private ThreadedSecurityIndexLoader getSecurityIndexWorker(Indice indice, List<Security> securities, CountDownLatch latch) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("exchange", "nse");
        paramMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_");
        paramMap.put("indice", indice.getIndexName());
        paramMap.put("tailUrl", "list.csv");
        ThreadedSecurityIndexLoader threadedSecurityIndexLoader = new ThreadedSecurityIndexLoader(paramMap, indice.getIndexName() + ".csv", latch);
        threadedSecurityIndexLoader.setIndice(indice);
        threadedSecurityIndexLoader.setSecurities(securities);
        return threadedSecurityIndexLoader;
    }


}
