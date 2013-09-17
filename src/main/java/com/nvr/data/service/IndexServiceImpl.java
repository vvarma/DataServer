package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import com.nvr.data.loader.Loader;
import com.nvr.data.repository.IndexDao;
import com.nvr.data.service.annotation.AppService;
import com.nvr.data.service.annotation.PostInitialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/16/13
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    @Qualifier(value = "indexLoader")
    Loader loader;
    @Autowired
    @Qualifier(value = "securityIndexLoader")
    Loader loader2;
    @Autowired
    IndexDao indexDao;
    @Autowired
    SecurityService securityService;

    @Override
    @PostInitialize(order = 1)
    public void loadIndice() {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("exchange", "nse");
        String fileName;
        try {
            URL url = loader.generateUrlGivenParamMap(paramMap);

            if (url != null) {
                fileName = loader.downloadFileGivenUrl(url, "indices.csv");
                List<Indice> indices = loader.parseFileAndReturnListOfEntity(fileName);
                for (Indice indice : indices)
                    indexDao.save(indice);

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ;
    }

    private void loadSecurityIndex(List<Indice> indices) {
        List<Security> securities = securityService.getAllSecurities();
        Map<String, String> paramMap = new HashMap<String, String>();
        for (Indice indice : indices) {
            paramMap.put("exchange", "nse");
            paramMap.put("seedUrl", "http://www.nseindia.com/content/indices/ind_");
            paramMap.put("indice", indice.getIndexName());
            paramMap.put("tailUrl", "list.csv");
            try {
                URL url = loader2.generateUrlGivenParamMap(paramMap);
                String fileName = null;
                try {
                    fileName = loader2.downloadFileGivenUrl(url, indice.getIndexName() + ".csv");
                } catch (IOException e) {

                    continue;
                }
                List<Security> fakeSecurities = loader2.parseFileAndReturnListOfEntity(fileName);
                for (Security s : fakeSecurities) {
                    if (securities.contains(s)) {
                        int i = securities.indexOf(s);
                        indice.addSecurity(securities.get(i));
                        securities.get(i).addIndex(indice);
                        indexDao.update(indice);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }


    @Override
    public List<Indice> getAllIndice() {
        return indexDao.findAll();
    }

    @Override
    public List<Security> getAllSecurity(Indice indice) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
