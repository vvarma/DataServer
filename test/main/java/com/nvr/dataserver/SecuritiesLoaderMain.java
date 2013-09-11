package com.nvr.dataserver;

import com.nvr.dataserver.util.SecuritiesLoader;
import com.nvr.dataserver.util.Security;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecuritiesLoaderMain {
    public static void main(String[] args){
        try {
            SecuritiesLoader loader=new SecuritiesLoader();
            for (Security security:loader.loadSecurities()){
                System.out.println(security);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
