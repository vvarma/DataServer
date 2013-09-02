package com.nvr.dataserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Index {
    String name;
    List<Security> securities;

    public Index(String name) {
        this.name = name;
        securities=new ArrayList<Security>();
    }

    public void addSecurity(Security security){
        securities.add(security);
    }
    public String getName() {

        return name;
    }

    public List<Security> getSecurities() {
        return securities;
    }

    @Override
    public String toString() {
        return "Index{" +
                "name='" + name + '\'' +
                ", securities=" + securities +
                '}';
    }
}
