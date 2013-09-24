package com.nvr.data.service;

import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;
import com.nvr.data.loader.DailyPriceLoader;
import com.nvr.data.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/23/13
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AdvSecService {
    @Autowired
    DailyPriceLoader dailyPriceLoader;
    @Autowired
    SecurityService securityService;


    public void updatePricedSecurities() {
        List<Security> securities=securityService.getAllPricedSecurities();
        Date date=securities.get(0).getLastPricedOn();
        System.out.println("yoyo++"+date);
        SimpleDateFormat fmt=new SimpleDateFormat("dd-MMM-yyyy");
        for (Date d= DateUtil.getNextDate(date);DateUtil.compareDates(d,new Date())<=0;d=DateUtil.getNextDate(d)){
            if (!DateUtil.isWeekend(d)){
                Map<String,String> paramMap=new HashMap<String, String>();
                paramMap.put("seedUrl","http://www.nseindia.com/content/historical/EQUITIES/");
                paramMap.put("date",fmt.format(d));
                try {
                    URL url=dailyPriceLoader.generateUrlGivenParamMap(paramMap);
                    String file=dailyPriceLoader.downloadFileGivenUrl(url,fmt.format(d)+".csv");
                    List<Price> priceList=dailyPriceLoader.parseFileAndReturnListOfEntity(file);
                    for (Security security:securities){
                        for (Price price:priceList){
                            if (price.getSecurity().equals(security)){
                                securityService.updateSecurity(price,security.getSymbol(),security.getSeries());
                            }
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

    }
}
