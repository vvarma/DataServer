package com.nvr.data.loader;

import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/18/13
 * Time: 2:55 AM
 * To change this template use File | Settings | File Templates.
 */

public class ThreadedPriceLoader extends ThreadedLoader<Price> {
    Security security;
    public ThreadedPriceLoader(Map<String, String> paramMap, String fileName, CountDownLatch latch, Security security) {
        super(paramMap, fileName, latch);
        this.security=security;
    }

    @Override
    public URL generateUrlGivenParamMap(Map<String, String> paramMap) throws MalformedURLException {
        String seedUrl=paramMap.get("seedUrl");
        String symbol=paramMap.get("symbol");
        String fromDate=paramMap.get("fromDate");
        String toDate=paramMap.get("toDate");
        URL url=null;
        if (seedUrl!=null&&symbol!=null&&fromDate!=null&&toDate!=null){
            url=new URL(seedUrl+"symbol="+symbol+"&fromDate="+fromDate+"&toDate="+toDate+"&hiddDwnld=true");
        }
        return url;
    }

    @Override
    public List<Price> parseFileAndReturnListOfEntity(String fileName) throws IOException, ParseException {
        File file=new File(fileName);
        int headerLine=findHeaderGivenFile(file);
        BufferedReader br=new BufferedReader(new FileReader(file));
        for (;headerLine>0;headerLine--)
            br.readLine();
        br.readLine();//read header information here
        String line;
        List<Price> prices=new ArrayList<Price>();
        while ((line=br.readLine())!=null){
            String[] lineArr=line.split(",");
            SimpleDateFormat fmt=new SimpleDateFormat("dd-MMM-yyyy");
            if (lineArr.length==8){
                Price price=new Price(Double.parseDouble(lineArr[5]),Double.parseDouble(lineArr[1]),Double.parseDouble(lineArr[3]),Double.parseDouble(lineArr[2]),Double.parseDouble(lineArr[4]),Integer.parseInt(lineArr[6].replace(" ","")),fmt.parse(lineArr[0]));
                prices.add(price);
            } else{
                break;
            }

        }
        security.setPrices(prices);
        return prices;
    }
}
