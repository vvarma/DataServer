package com.nvr.data.loader;

import com.nvr.data.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/19/13
 * Time: 3:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Scope("prototype")
public abstract class  ThreadedLoader<T> extends AbstractLoader<T> implements Callable<List<T>> {
    Map<String,String> paramMap=new HashMap<String, String>();
    String fileName;
    CountDownLatch latch;
    final static Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    protected ThreadedLoader(Map<String, String> paramMap, String fileName, CountDownLatch latch) {
        this.paramMap = paramMap;
        this.fileName = fileName;
        this.latch = latch;
    }

    @Override
    public List<T> call() {
        LOGGER.debug("Running on thread" );
        List<T> tList=null;
        try {
            URL url =generateUrlGivenParamMap(paramMap);
            LOGGER.debug("Url generated "+url.toString());
            fileName=downloadFileGivenUrl(url,fileName);
            LOGGER.debug("File downloaded "+fileName);
            tList=parseFileAndReturnListOfEntity(fileName);
            LOGGER.debug("Completed");
        } catch (IOException e) {
            LOGGER.debug("Exception ",e);
        } catch (ParseException e) {
            LOGGER.debug("Exception ",e);
        }finally {
            latch.countDown();
        }

        return tList;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }


}
