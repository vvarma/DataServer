package com.nvr.data.service;

import java.util.List;

import com.nvr.data.domain.Security;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:12 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SecurityService {
    public void loadSecurities();
    public List<Security> getAllSecurities();
}
