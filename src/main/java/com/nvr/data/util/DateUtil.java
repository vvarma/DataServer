package com.nvr.data.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: vvarm1
 * Date: 9/20/13
 * Time: 6:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    public static boolean isWeekday(Date date){
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
            return true;
        else
            return false;
    }

    public static Date getLastWorkingDate(Date date) {
        while (isWeekday(date)){
            Calendar calendar=new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,-1);
            date=calendar.getTime();
        }
        return date;  //To change body of created methods use File | Settings | File Templates.
    }
}
