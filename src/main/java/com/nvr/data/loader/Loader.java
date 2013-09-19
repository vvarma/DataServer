package com.nvr.data.loader;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/10/13
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Loader<T> {
    URL generateUrlGivenParamMap(Map<String,String> paramMap) throws MalformedURLException;
    String downloadFileGivenUrl(URL url, String fileName) throws IOException;
    List<T> parseFileAndReturnListOfEntity(String fileName) throws IOException, ParseException;
}
