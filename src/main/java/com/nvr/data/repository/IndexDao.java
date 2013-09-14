package com.nvr.data.repository;

import com.nvr.data.domain.Indice;
import com.nvr.data.repository.annotation.AppRepository;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 3:50 AM
 * To change this template use File | Settings | File Templates.
 */
@AppRepository
public class IndexDao extends AbstractJpaDAO<Indice> {
    public IndexDao() {
        setClazz(Indice.class);
    }
}
