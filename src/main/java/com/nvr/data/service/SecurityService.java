package com.nvr.data.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import com.nvr.data.domain.Security;
import com.nvr.data.service.annotation.AppService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:12 AM
 * To change this template use File | Settings | File Templates.
 */

public interface SecurityService {
    public List<Security> getAllSecurities();
}
