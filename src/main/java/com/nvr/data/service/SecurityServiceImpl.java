package com.nvr.data.service;

import com.nvr.data.domain.Security;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class SecurityServiceImpl implements SecurityService {
    @Autowired

    @Override
    public void loadSecurities() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Security> getAllSecurities() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
