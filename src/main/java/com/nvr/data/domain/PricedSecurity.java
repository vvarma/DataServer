package com.nvr.data.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/18/13
 * Time: 2:00 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class PricedSecurity extends Security {
    @ElementCollection(targetClass = Price.class)
    List<Price> prices;

    public PricedSecurity(Security security) {
        super(security.symbol,security.company,security.series,security.listing,security.isinNumber);
        getIndiceList().addAll(security.getIndiceList());
        prices=new ArrayList<Price>();
    }

    public PricedSecurity() {
        prices=new ArrayList<Price>();
    }

    public void addPrice(Price price){
        prices.add(price);
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }
}
