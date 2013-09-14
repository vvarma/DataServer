package com.nvr.data.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Security implements Serializable {
    @Id
    final String symbol;
    final String company;
    final String series;
    final Date listing;
    final String isinNumber;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "indexSecurity",joinColumns = {@JoinColumn(name = "indice")},inverseJoinColumns ={@JoinColumn(name = "security")})
    final List<Indice> indiceList;

    public Security(String symbol, String company, String series, Date listing, String isinNumber) {
        this.symbol = symbol;
        this.company = company;
        this.series = series;
        this.listing = listing;
        this.isinNumber = isinNumber;
        indiceList =new ArrayList<Indice>();
    }

    public void addIndex(Indice indice){
        indiceList.add(indice);
    }
    public List<Indice> getIndiceList() {
        return indiceList;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
    }

    public String getSeries() {
        return series;
    }

    public Date getListing() {
        return (Date) listing.clone();
    }

    public String getIsinNumber() {
        return isinNumber;
    }

    @Override
    public String toString() {
        return "Security{" +
                "symbol='" + symbol + '\'' +
                ", company='" + company + '\'' +
                ", series='" + series + '\'' +
                ", listing=" + listing +
                ", isinNumber='" + isinNumber + '\'' +
                '}';
    }

    public Security(String symbol, String series, String isinNumber) {
        this(symbol,null,series,null,isinNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Security)) return false;

        Security security = (Security) o;

        if (isinNumber != null ? !isinNumber.equals(security.isinNumber) : security.isinNumber != null) return false;
        if (series != null ? !series.equals(security.series) : security.series != null) return false;
        if (symbol != null ? !symbol.equals(security.symbol) : security.symbol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (isinNumber != null ? isinNumber.hashCode() : 0);
        return result;
    }
}
