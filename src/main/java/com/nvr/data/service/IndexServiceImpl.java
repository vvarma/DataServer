package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Price;
import com.nvr.data.domain.PricedSecurity;
import com.nvr.data.domain.Security;
import com.nvr.data.loader.Loader;
import com.nvr.data.repository.IndexDao;
import com.nvr.data.repository.PricedSecurityDao;
import com.nvr.data.repository.SecurityJpaDao;
import com.nvr.data.service.annotation.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/16/13
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
@AppService
public class IndexServiceImpl implements IndexService {
    @Autowired
    @Qualifier(value = "priceLoader")
    Loader priceLoader;
    @Autowired
    IndexDao indexDao;
    @Autowired
    SecurityJpaDao securityJpaDao;


    public static Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Override
    public List<Indice> getAllIndice() {
        loadSecurityPrices();
        return indexDao.findAll();
    }

    @Override
    public List<Security> getAllSecurity(String indiceName) {
        Indice indice = indexDao.findOne(indiceName);
        List<Security> securities = indice.getSecurities();
        for (Security s : securities) ;
        return securities;
    }

    private void loadSecurityPrices() {
        List<Indice> indices = indexDao.findAll();
        List<PricedSecurity> securities = securityJpaDao.findAll();


        for (PricedSecurity security : securities) {
            for (Indice indice : indices) {
                if (indice.getSecurities().contains(security)) {
                    String symbolName = security.getSymbol();
                    SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
                    String fromDate = fmt.format(security.getListing());
                    String toDate = fmt.format(new Date());
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("seedUrl", "http://www.nseindia.com/live_market/dynaContent/live_watch/get_quote/getHistoricalData.jsp?");
                    paramMap.put("symbol", symbolName);
                    paramMap.put("fromDate", fromDate);
                    paramMap.put("toDate", toDate);

                    try {
                        URL url = priceLoader.generateUrlGivenParamMap(paramMap);
                        String fileName = priceLoader.downloadFileGivenUrl(url, symbolName + ".csv");
                        List<Price> prices = priceLoader.parseFileAndReturnListOfEntity(fileName);

                        security.setPrices(prices);
                        securityJpaDao.update(security);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    break;
                }
            }

        }

    }
}
