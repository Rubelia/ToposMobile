package com.example.laptrinhmobile.toposmobile.Object;

import android.os.Parcel;
import android.util.Log;

import java.io.Serializable;
import java.security.acl.LastOwnerException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by LapTrinhMobile on 10/16/2015.
 */
public class Timer implements  Serializable{

    private static final String LOCATION = "Timer";
    private Calendar c;
    private int day, month, year, hour, minutes, seconds, milliseconds;

    public Timer() {
        c = Calendar.getInstance(Locale.getDefault());
        day  = c.get(Calendar.DATE);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
//        TimeZone timeZone = c.getTimeZone();
//        Log.d("Timer ", timeZone.getDisplayName());
//
        if(month < 12)
        {
            month += 1;
        }
        else
        {
            month = 1;
            year += 1;
        }

        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        seconds = c.get(Calendar.SECOND);
        milliseconds = c.get(Calendar.MILLISECOND);
//
    }

    public Timer(Timer in) {
        day = in.getDay();
        month = in.getMonth();
        year = in.getYear();
        hour = in.getHour();
        minutes = in.getMinutes();
        seconds = in.getSeconds();
        milliseconds = in.getMilliseconds();
    }
    public void setDay(int in_day) { this.day = in_day; }
    public void setMonth(int in_month) { this.month = in_month; }
    public void setYear(int in_year) { this.year = in_year; }
    public void setHour(int in_hour) { this.hour = in_hour; }
    public void setMinutes(int in_minutes) { this.minutes = in_minutes; }
    public void setSeconds(int in_seconds) { this.seconds = in_seconds; }
    public void setMilliseconds(int in_milliseconds) { this.milliseconds = in_milliseconds; }
    public int getDay() { return  this.day; }
    public int getMonth() { return  this.month; }
    public String getStrMonth() {
        if(this.month > 9)
            return ""+this.month;
        else return  "0" + this.month;
    }
    public int getYear() { return  this.year; }
    public int getHour() { return  this.hour; }
    public int getMinutes() { return  this.minutes; }
    public int getSeconds() { return  this.seconds; }
    public int getMilliseconds() { return this.milliseconds; }
    public int getGio() {
        return this.getSeconds() + this.getMinutes()*60 + this.getHour() * 60 * 60;
    }

    public String getTimeForHD() {
        String year = new String(""+this.year);
        year.substring(2,3);
        return ""+this.day+this.month+year;
    }
    public String toString() {
        return this.year + "-" + this.month + "-" + this.day + " " + this.hour + ":" + this.minutes + ":" +this.seconds + "." + this.milliseconds;
    }
//    public Date getDate() {
//        Date result = new Date(this.year,this.month, this.day, this.hour, this.minutes, this.seconds);
//        Log.d(LOCATION,result.toString());
//        return  result;
//    }

}
