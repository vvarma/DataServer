package com.nvr.data.repository;

import com.nvr.data.repository.annotation.AppRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 4:41 AM
 * To change this template use File | Settings | File Templates.
 */

public interface JpaDao<T> {
    void setClazz(Class<T> clazzToSet) ;

    T findOne(String id);

    List<T> findAll() ;

    void save(T entity) ;

    void update(T entity) ;

    void delete(T entity) ;

    void deleteById(String entityId) ;
}
