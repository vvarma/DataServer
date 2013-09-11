package com.nvr.dataserver;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/4/13
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Loader {
    URL createUrlGivenInfo();
    String downloadFileGivenUrl(URL url);

}
