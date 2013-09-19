package com.nvr.data.repository;

import com.nvr.data.domain.PricedSecurity;
import com.nvr.data.repository.annotation.AppRepository;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */

@AppRepository
public class SecurityJpaDao extends AbstractJpaDAO<PricedSecurity> {
    public SecurityJpaDao() {
        setClazz(PricedSecurity.class);
    }

}
