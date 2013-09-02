package com.nvr.dataserver;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 8/20/13
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndicesLoaderMain {
    public static void main(String[] args){
        try {
            IndicesLoader loader=new IndicesLoader();
            for (Index index:loader.loadIndices()){
                System.out.println(index);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
