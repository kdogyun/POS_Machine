package com.example.dogyun.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateCalculate {

    public static int between(Calendar startCal, Calendar endCal){
        long diffSec = (endCal.getTimeInMillis() - startCal.getTimeInMillis())/1000;
        long diffDays = diffSec / (24*60*60);
        return (int) diffDays;
    }

    public static String dateAdd(Calendar startCal, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar(startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, (int)day);
        return sdf.format(calendar.getTime());
    }

    public static String date(Calendar startCal){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(startCal.getTime());
    }

    public static Date stringDate(String str){
        Date result = null;
        try {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = transFormat.parse(str);
        }catch ( ParseException e ){
            e.printStackTrace();
        }
        return result;
    }
}
