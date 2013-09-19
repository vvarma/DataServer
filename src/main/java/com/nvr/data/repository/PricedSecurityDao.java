package com.nvr.data.repository;

import com.nvr.data.domain.PricedSecurity;
import com.nvr.data.domain.SecurityId;
import com.nvr.data.repository.annotation.AppRepository;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/18/13
 * Time: 11:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PricedSecurityDao extends AbstractJpaDAO<PricedSecurity> {
    public PricedSecurityDao() {
        setClazz(PricedSecurity.class);
    }

    public PricedSecurity findByEntity(PricedSecurity pricedSecurity){
        SecurityId securityId=new SecurityId();
        securityId.setSeries(pricedSecurity.getSeries());
        securityId.setSymbol(pricedSecurity.getSymbol());
        return entityManager.find(getClazz(),securityId);
    }
}
