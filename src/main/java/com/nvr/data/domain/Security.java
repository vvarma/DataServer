package com.nvr.data.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

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
@IdClass(SecurityId.class)
public class Security implements Serializable {
    @Id
    String symbol;
    String company;
    @Id
    String series;
    Date listing;
    String isinNumber;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JsonIgnore
    List<Indice> indiceList;

    @OneToMany (cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    @JsonIgnore
    List<Price> prices;
    Date lastPricedOn;
    boolean priced=false;

    public Security() {
        indiceList=new ArrayList<Indice>();
        prices =new ArrayList<Price>();
    }

    public Security(String symbol, String company, String series, Date listing, String isinNumber) {
        this.symbol = symbol;
        this.company = company;
        this.series = series;
        this.listing = listing;
        this.isinNumber = isinNumber;
        indiceList =new ArrayList<Indice>();
        prices =new ArrayList<Price>();
    }

    public void setLastPricedOn(Date lastPricedOn) {
        this.lastPricedOn = lastPricedOn;
    }

    public Date getLastPricedOn() {
        return lastPricedOn;
    }

    public void addIndex(Indice indice){
        indiceList.add(indice);
    }

    public void addPrice(Price price){
        if (price!=null){
            List<Price> priceList=new ArrayList<Price>(prices.size()+1);
            priceList.add(price);
            priceList.addAll(prices);
            prices=priceList;
            priced=true;
            lastPricedOn=price.getPriceDate();
        }

    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        if (!prices.isEmpty()){
            this.prices = prices;
            lastPricedOn=prices.get(0) .getPriceDate();
            priced=true;
        }
    }

    public boolean isPriced() {
        return priced;
    }

    public void setPriced(boolean priced) {
        this.priced = priced;
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
                "symbol='" + symbol  +
                '}';
    }

    public Security(String symbol, String series, String isinNumber) {
        this(symbol,"",series,new Date(),isinNumber);
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
