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
    public static boolean isWeekend(Date date){
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
            return true;
        else
            return false;
    }

    public static Date getLastWorkingDate(Date date) {
        while (isWeekend(date)){
            Calendar calendar=new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,-1);
            date=calendar.getTime();
        }
        return date;  //To change body of created methods use File | Settings | File Templates.
    }
    public static Date getNextDate(Date date){
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }
    public static int compareDates(Date date, Date date1){
        Calendar calendar=new GregorianCalendar();
        Calendar calendar1=new GregorianCalendar();
        calendar.setTime(date);
        calendar1.setTime(date1);
        if (calendar.get(Calendar.YEAR)<calendar1.get(Calendar.YEAR)||
                calendar.get(Calendar.MONTH)<calendar1.get(Calendar.MONTH)||
                calendar.get(Calendar.DATE)<calendar1.get(Calendar.DATE)){
            return -1;
        }else if(calendar.get(Calendar.YEAR)==calendar1.get(Calendar.YEAR)&&
                calendar.get(Calendar.MONTH)==calendar1.get(Calendar.MONTH)&&
                calendar.get(Calendar.DATE)==calendar1.get(Calendar.DATE))
            return 0;
        else
            return 1;
    }
}
