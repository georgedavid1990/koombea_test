package com.koombea.testjorge.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    static String[] day_suffixes =
            {  "0th",  "1st",  "2nd",  "3rd",  "4th",  "5th",  "6th",  "7th",  "8th",  "9th",
                    "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th",
                    "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th",
                    "30th", "31st" };

    public static Date getDateFromString(String pattern, String dateText){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        Date date = null;
        try {
            date = format.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getOrdinalDateFormat(Date date){
        SimpleDateFormat formatYearAndMonth = new SimpleDateFormat("yy MMM ", Locale.US);
        SimpleDateFormat formatDayOfMonth  = new SimpleDateFormat("d", Locale.US);
        int day = Integer.parseInt(formatDayOfMonth.format(date));
        return formatYearAndMonth.format(date) + day_suffixes[day];
    }
}
