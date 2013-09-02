package com.nvr.dataserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
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
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndicesLoader {
    Properties prop = new Properties();

    public IndicesLoader() throws IOException {
        prop.load(getClass().getResourceAsStream("/filepath.properties"));
        prop.load(getClass().getResourceAsStream("/url.properties"));
    }

    public List<Index> loadIndices() throws IOException, ParseException {
        URL url = new URL(prop.getProperty("NSE_INDEX_LIST"));
        String filePath = prop.getProperty("NSE_INDEX_LIST_PATH");
        Util.downloadCsvGivenUrl(filePath, url);
        List<Index> indices = readCsvAndLoadList(filePath);
        List<Security> securities = new SecuritiesLoader().loadSecurities();
        loadSecuritesForIndex(indices, securities);
        return indices;
    }

    private void loadSecuritesForIndex(List<Index> indices, List<Security> securities) throws IOException, ParseException {
        String dataPath = prop.getProperty("INDEX_STORE_LOC");
        for (Index index : indices) {
            URL url = generateUrlForIndexSecurityList(index.getName());
            String filePath = dataPath + index.getName() + ".csv";
            if (Util.downloadCsvGivenUrl(filePath, url))
                readCsvAndReloadList(index, filePath, securities);
        }

    }

    private void readCsvAndReloadList(Index index, String filePath, List<Security> securities) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String readLine;
        while ((readLine = br.readLine()) != null) {
            int i = securities.indexOf(parseSecurity(readLine));
            if (i >= 0) {
                index.addSecurity(securities.get(i));
            }
        }
    }

    private URL generateUrlForIndexSecurityList(String name) throws MalformedURLException {
        String[] arr = name.split(" ");
        String str = "";
        for (String s : arr) {
            str += s.toLowerCase();
        }
        String temp = prop.getProperty("NSE_INDEX_SECURITY_PATH") + str + "list.csv";
        return new URL(temp);
    }

    private List<Index> readCsvAndLoadList(String filePath) throws IOException {
        List<Index> indices = new ArrayList<Index>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.readLine();
        String readLine;
        while ((readLine = br.readLine()) != null) {
            indices.add(parseIndex(readLine));
        }
        return indices;
    }

    private Index parseIndex(String readLine) {
        String[] strArr = readLine.split(",");
        return new Index(strArr[0]);
    }

    private Security parseSecurity(String securityString) throws ParseException {
        String[] strArr = securityString.split(",");

        return new Security(strArr[2],strArr[3],strArr[4]);
    }

}
