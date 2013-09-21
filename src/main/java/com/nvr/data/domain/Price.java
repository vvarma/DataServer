package com.nvr.data.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/18/13
 * Time: 2:01 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id=0;
    @ManyToOne
    Security security;
    Double closePrice,openPrice,lowPrice,highPrice, lastTradedPrice;
    Integer quantity;
    Date priceDate;
    public Price(Double closePrice, Double openPrice, Double lowPrice, Double highPrice, Double lastTradedPrice, int quantity, Date priceDate) {
        this.closePrice = closePrice;
        this.openPrice = openPrice;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
        this.lastTradedPrice = lastTradedPrice;
        this.quantity = quantity;
        this.priceDate = priceDate;
    }
    public Price() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(Double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    @Override
    public String toString() {
        return "Price{" +
                "priceDate=" + priceDate +
                ", closePrice=" + closePrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;

        Price price = (Price) o;

        if (id != price.id) return false;
        if (!security.equals(price.security)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + security.hashCode();
        return result;
    }
}
