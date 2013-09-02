package com.nvr.dataserver.pipeline;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/2/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSVReder {
    public void readCSV(){
        File file=new File("C:/Users/vvarm1/Documents/dataServer/CNX Nifty Junior.csv");
        CSVReader reader=new CSVReader(file);
        System.out.println(reader.getFieldSeparator());
        System.out.println(reader.getDescription());
        System.out.println(reader.getStartingRow());
        System.out.println(reader.getFieldNames());

    }
    public static void main(String[] args){
        CSVReder reder=new CSVReder();
        reder.readCSV();

    }
}
