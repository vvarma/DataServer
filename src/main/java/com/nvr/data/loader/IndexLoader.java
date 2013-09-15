package com.nvr.data.loader;

import com.nvr.data.domain.Indice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/10/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */

@Qualifier(value = "IndexLoader")
public class IndexLoader extends AbstractLoader implements Loader {
    @Override
    public URL generateUrlGivenParamMap(Map<String, String> paramMap) throws MalformedURLException {
        URL url=null;
        String exchange=paramMap.get("exchange");
        if (exchange!=null){
            url=new URL("http://www.nseindia.com/content/indices/ind_close_all_19082013.csv");
        }
        return url;
    }

    @Override
    public  List<Indice> parseFileAndReturnListOfEntity(String fileName) throws IOException {
        List<Indice> indices=new ArrayList<Indice>();
        File file=new File(fileName);
        int headerLine=findHeaderGivenFile(file);
        BufferedReader br=new BufferedReader(new FileReader(file));
        for (;headerLine>0;headerLine--)
            br.readLine();
        br.readLine();//do header related stuff here
        String line;
        while ((line=br.readLine())!=null){
            String[] lineArr=line.split(",");
            indices.add(new Indice(lineArr[0]));
        }
        return indices;
    }
}
