import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/10/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecurityLoaderTest {
    @Test
    public void shouldGenerateUrlGivenExchangeAndIndexName(){
        SecurityLoader loader=new SecurityLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        paramMap.put("index","nifty");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);
            if (url!=null)
            url.openConnection();


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldDownloadFileGivenUrl(){
        SecurityLoader loader=new SecurityLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);
            if (url!=null)
                loader.downloadFileGivenUrl(url,"securities.csv");


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    @Test
    public void shouldParseFileAndLoadSecurities(){
        SecurityLoader loader=new SecurityLoader();
        Map<String,String> paramMap=new HashMap<String, String>();
        paramMap.put("exchange","nse");
        String fileName;
        try {
            URL url=loader.generateUrlGivenParamMap(paramMap);
            Assert.assertNotNull(url);
            if (url!=null){
                fileName=loader.downloadFileGivenUrl(url,"securities.csv");
                loader.parseFileAndReturnListOfEntity(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
