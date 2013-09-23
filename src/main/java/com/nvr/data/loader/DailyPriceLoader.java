package com.nvr.data.loader;

import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/23/13
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DailyPriceLoader extends AbstractLoader<Price> {
    public void parseFileAndReturnUpdatedSecurities(List<Security> securities, String fileName) throws IOException, ParseException {
        File file = new File(fileName);
        int headerLine = findHeaderGivenFile(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (headerLine-- > 0)
            br.readLine();
        br.readLine();
        String line;
        List<Price> prices = new ArrayList<Price>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        while ((line = br.readLine()) != null) {
            String[] lineArr = line.split(",");
            int index;
            if ((index = securities.indexOf(new Security(lineArr[0], lineArr[1], lineArr[12]))) > 0) {
                Price price = new Price(Double.parseDouble(lineArr[5]), Double.parseDouble(lineArr[2]),
                        Double.parseDouble(lineArr[4]), Double.parseDouble(lineArr[3]),
                        Double.parseDouble(lineArr[6]), Integer.parseInt(lineArr[8]),
                        fmt.parse(lineArr[10]));
                price.setSecurity(securities.get(index));
                securities.get(index).addPrice(price);
            }
        }
    }

    @Override
    public URL generateUrlGivenParamMap(Map<String, String> paramMap) throws MalformedURLException {
        URL url = null;
        String seedUrl = paramMap.get("seedUrl");
        String date = paramMap.get("date");
        String dateArr[] = date.split("-");
        url = new URL(seedUrl + dateArr[2] + "/" + dateArr[1].toUpperCase() + "/cm" + date.replace("-", "").toUpperCase() + "bhav.csv.zip");
        return url;
    }

    @Override
    public List<Price> parseFileAndReturnListOfEntity(String fileName) throws IOException, ParseException {
        File file = new File(fileName);
        int headerLine = findHeaderGivenFile(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (headerLine-- > 0)
            br.readLine();
        br.readLine();
        String line;
        List<Price> prices = new ArrayList<Price>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        while ((line = br.readLine()) != null) {
            String[] lineArr = line.split(",");
            Price price = new Price(Double.parseDouble(lineArr[5]), Double.parseDouble(lineArr[2]),
                    Double.parseDouble(lineArr[4]), Double.parseDouble(lineArr[3]),
                    Double.parseDouble(lineArr[6]), Integer.parseInt(lineArr[8]),
                    fmt.parse(lineArr[10]));
            price.setSecurity(new Security(lineArr[0], lineArr[1], lineArr[12]));
            prices.add(price);
        }
        return prices;
    }

    @Override
    public String downloadFileGivenUrl(URL url, String fileName) throws IOException {
        String zipFileName = super.downloadFileGivenUrl(url, fileName + ".zip");
        return extractTemp(zipFileName);
    }

    private String extractTemp(String fName) throws IOException {
        String entryName = "";
        byte[] buf = new byte[1024];
        ZipInputStream zinstream = new ZipInputStream(
                new FileInputStream(fName));
        ZipEntry zentry = zinstream.getNextEntry();
        String path = System.getProperty("java.io.tmpdir");

        while (zentry != null) {
            entryName = zentry.getName();
            path = path + "/" + entryName;
            FileOutputStream outstream = new FileOutputStream(path);
            int n;

            while ((n = zinstream.read(buf, 0, 1024)) > -1) {
                outstream.write(buf, 0, n);
            }
            outstream.close();
            zinstream.closeEntry();
            zentry = zinstream.getNextEntry();
        }
        zinstream.close();
        return path;
    }
}
