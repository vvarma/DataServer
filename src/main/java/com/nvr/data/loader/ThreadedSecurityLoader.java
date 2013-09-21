package com.nvr.data.loader;

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
 * User: vvarma
 * Date: 9/10/13
 * Time: 9:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class ThreadedSecurityLoader extends ThreadedLoader<Security>  {
    public ThreadedSecurityLoader(Map<String, String> paramMap, String fileName, CountDownLatch latch) {
        super(paramMap, fileName, latch);
    }

    @Override
    public URL generateUrlGivenParamMap(Map<String, String> paramMap) throws MalformedURLException {
        URL url = null;
        String seedUrl = paramMap.get("seedUrl");
        if (seedUrl != null) {
            url = new URL(seedUrl);
        }
        return url;
    }

    @Override
    public List<Security> parseFileAndReturnListOfEntity(String fileName) throws IOException, ParseException {
        List<Security> securities = new ArrayList<Security>();
        File file = new File(fileName);
        int headerLine = findHeaderGivenFile(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (; headerLine > 0; headerLine--)
            br.readLine();
        br.readLine();//header related stuff here
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineArr = line.split(",");
            SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
            Security security = new Security(lineArr[0], lineArr[1], lineArr[2], fmt.parse(lineArr[3]), lineArr[6]);
            if (!securities.contains(security))
                securities.add(security);
        }
        return securities;
    }
}
