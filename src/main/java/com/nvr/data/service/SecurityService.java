package com.nvr.data.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import com.nvr.data.domain.Security;
import com.nvr.data.service.annotation.AppService;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:12 AM
 * To change this template use File | Settings | File Templates.
 */

public interface SecurityService {
    public void loadSecurities() ;
    public List<Security> getAllSecurities();
}
