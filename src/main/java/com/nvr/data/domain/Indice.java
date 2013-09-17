package com.nvr.data.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class Indice implements Serializable {
    @Id
    String indexName;
    @ManyToMany(mappedBy = "indiceList",cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<Security> securities;

    public Indice() {
    }

    public Indice(String indexName) {
        this.indexName = indexName;
        securities=new ArrayList<Security>();
    }

    public Indice(String indexName, List<Security> securities) {
        this.indexName = indexName;
        this.securities = securities;
    }

    public void addSecurity(Security security){
        securities.add(security);
    }
    public String getIndexName() {

        return indexName;
    }

    public List<Security> getSecurities() {
        return securities;
    }

    @Override
    public String toString() {
        return "Indice{" +
                "indexName='" + indexName + '\'' +
                ", securities=" + securities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Indice indice = (Indice) o;

        if (!indexName.equals(indice.indexName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return indexName.hashCode();
    }
}
