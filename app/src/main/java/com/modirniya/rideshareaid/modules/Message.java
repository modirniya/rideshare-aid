package com.modirniya.rideshareaid.modules;


import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class Message {

    private String uid;
    private String text;
    private String name;
    private Long time = Long.parseLong("0");

    public Message(String text, String name, Long timestamp) {
        this.text = text;
        this.name = name;
        this.time = timestamp;
    }

    public Message() {

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }


    public Long getTime() {
        return time;
    }

    public String getStringTime() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM hh:mm a", cal).toString();
    }

    public void setTime(Long time) {
        this.time = time;
    }
}