package com.nvr.data.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJpaDAO<T extends Serializable> {
    private Class<T> clazz;
    @PersistenceContext
    EntityManager entityManager;

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public T findOne(Long id) {
        return this.entityManager.find(this.clazz, id);
    }

    public List<T> findAll() {
        return this.entityManager.createQuery("from " + this.clazz.getName())
                .getResultList();
    }

    public void save(T entity) {
        this.entityManager.persist(entity);
    }

    public void update(T entity) {
        this.entityManager.merge(entity);
    }

    public void delete(T entity) {
        this.entityManager.remove(entity);
    }

    public void deleteById(Long entityId) {
        T entity = this.findOne(entityId);
        this.delete(entity);
    }
}