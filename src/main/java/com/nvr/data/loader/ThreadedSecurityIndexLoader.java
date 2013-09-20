package com.nvr.data.loader;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.PricedSecurity;
import com.nvr.data.domain.Security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/19/13
 * Time: 5:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadedSecurityIndexLoader extends ThreadedLoader<Security> {
    public ThreadedSecurityIndexLoader(Map<String, String> paramMap, String fileName, CountDownLatch latch) {
        super(paramMap, fileName, latch);
    }

    Indice indice;
    private List<PricedSecurity> securities;

    public void setIndice(Indice indice){
        this.indice=indice;
    }
    @Override
    public URL generateUrlGivenParamMap(Map<String, String> paramMap) throws MalformedURLException {
        URL url=null;
        String seedUrl=paramMap.get("seedUrl");
        String indexName=paramMap.get("indice");
        indexName=indexName.replace(" ", "");
        String urlRest=paramMap.get("tailUrl");
        url=new URL(seedUrl+indexName.toLowerCase()+urlRest);
        return url;
    }

    @Override
    public List<Security> parseFileAndReturnListOfEntity(String fileName) throws IOException, ParseException {
        List<Security> securities =new ArrayList<Security>();
        File file=new File(fileName);
        int headerLine=findHeaderGivenFile(file);
        System.out.println(headerLine+"header found"+fileName);
        BufferedReader br=new BufferedReader(new FileReader(file));
        for (;headerLine>0;headerLine--)
            br.readLine();
        br.readLine();//header related stuff here
        String line;
        while ((line=br.readLine())!=null){
            String[] lineArr=line.split(",");
            securities.add(new Security(lineArr[2],lineArr[3],lineArr[4]));
        }
        int index;
        for (Security s:securities){
            if ((index=this.securities.indexOf(s))>=0){
                indice.addSecurity(this.securities.get(index));
            }
        }
        return securities;
    }

    public void setSecurities(List<PricedSecurity> securities) {
        this.securities = securities;
    }
}
