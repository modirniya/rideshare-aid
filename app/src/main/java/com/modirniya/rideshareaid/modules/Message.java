package com.modirniya.rideshareaid.modules;


import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class Message {
    private String uid = null;
    private String text = null;
    private String name = null;
    private Long time;

    public Message() {
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

    public String getStringTime() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(getTime());
        return DateFormat.format("dd-MM hh:mm a", cal).toString();
    }

    public Long getTime() {
        return time;
    }

}