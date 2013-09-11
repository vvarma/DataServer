import com.nvr.dataserver.util.Security;

import java.io.File;
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
 * Time: 9:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecurityLoader extends AbstractLoader implements Loader {
    @Override
    public URL generateUrlGivenParamMap(Map<String, String> paramMap) throws MalformedURLException {
        URL url=null;
        String exchange=paramMap.get("exchange");
        if (exchange!=null){
            url=new URL("http://nseindia.com/content/equities/EQUITY_L.csv");
        }

        return url;
    }

    @Override
    public List<Security> parseFileAndReturnListOfEntity(String fileName) throws IOException {
        List<Security> securities=new ArrayList<Security>();
        File file=new File(fileName);
        int headerLine=findHeaderGivenFile(file);
        System.out.println(headerLine);
        return securities;
    }
}
