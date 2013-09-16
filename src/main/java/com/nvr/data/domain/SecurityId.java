package com.nvr.data.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/16/13
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class SecurityId implements Serializable{
    private String symbol;
    private String series;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecurityId that = (SecurityId) o;

        if (!series.equals(that.series)) return false;
        if (!symbol.equals(that.symbol)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = symbol.hashCode();
        result = 31 * result + series.hashCode();
        return result;
    }
}
