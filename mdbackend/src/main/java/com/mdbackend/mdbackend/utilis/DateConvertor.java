package com.mdbackend.mdbackend.utilis;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateConvertor {
    SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    SimpleDateFormat responseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat shortDateTime=new SimpleDateFormat("MMM dd hh:mm a");

    public Date convertToDate(String date) {

        try {
            Date dateTime = toDate.parse(date);
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertToString(Date date) {
        String recordedTime = responseDate.format(date);
        return recordedTime;
    }

    public long returnSecond(Date date) {
        try {
            long secondsSinceEpoch = date.getTime() / 1000;
            return secondsSinceEpoch;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
