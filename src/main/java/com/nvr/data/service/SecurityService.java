package com.nvr.data.service;

import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 5:12 AM
 * To change this template use File | Settings | File Templates.
 */

public interface SecurityService {
    public List<Security> getAllSecurities();
    public List<Price> getPriceForSecurity(String symbol,Date fromDate, Date toDate);
    public List<Price> getPriceForSecurity(String symbol,String series,Date fromDate, Date toDate);
    public List<Security> getAllPricedSecurities();
    public Security getSecurity(String symbol);
    public Security getSecurity(String symbol,String series);

}
