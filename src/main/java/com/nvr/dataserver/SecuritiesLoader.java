package com.nvr.dataserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecuritiesLoader {
    Properties prop = new Properties();

    //throws file not found, io
    public SecuritiesLoader() throws IOException {
        prop.load(getClass().getResourceAsStream("/filepath.properties"));
        prop.load(getClass().getResourceAsStream("/url.properties"));
    }

    //throws malformed url, io
    public List<Security> loadSecurities() throws IOException, ParseException {
        URL url = new URL(prop.getProperty("NSE_SECURITY_LIST"));
        String filePath = prop.getProperty("NSE_SECURITY_LIST_PATH");
        Util.downloadCsvGivenUrl(filePath, url);
        return readCsvAndLoadList(filePath);
    }

    private List<Security> readCsvAndLoadList(String filePath) throws ParseException, IOException {
        List<Security> securities = new ArrayList<Security>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String lineRead;
        br.readLine();
        while ((lineRead = br.readLine()) != null) {
            Security security = parseSecurity(lineRead);
            securities.add(security);
        }
        return securities;
    }

    private Security parseSecurity(String securityString) throws ParseException {
        String[] strArr = securityString.split(",");
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        return new Security(strArr[0], strArr[1], strArr[2], fmt.parse(strArr[3]), strArr[6]);
    }
}
