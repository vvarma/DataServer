package com.nvr.data.util;


import org.junit.Test;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/24/13
 * Time: 5:50 AM
 * To change this template use File | Settings | File Templates.
 */


public class TestDateUtil {
    @Test
    public void shouldTestWeekdaysAndNextDay(){
        Date date=new Date();
        System.out.println(date);
        Date date1=DateUtil.getNextDate(date);
        System.out.println(date);
        System.out.println(date1);
        System.out.println(date.equals(date1));
        System.out.println(DateUtil.compareDates(date1,date));
        for (;!DateUtil.isWeekend(date);date=DateUtil.getNextDate(date)){
            System.out.println(date);
        }
    }
}
