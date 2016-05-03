package com.example.khome.smartsms;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by khome on 4/5/16.
 */
public class MillisToDate {

    public static Calendar milliToDate(String date)
    {
        long dat=Long.parseLong(date);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dat);
        return cal;



    }

}
