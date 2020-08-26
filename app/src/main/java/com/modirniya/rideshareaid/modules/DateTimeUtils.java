package com.modirniya.rideshareaid.modules;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;

public class DateTimeUtils {

    public static boolean checkDifference(Date startDate, Date endDate, Context context) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        if (elapsedDays != 0)
            return true;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        if (elapsedHours != 0)
            return true;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        if (elapsedMinutes > 29 || elapsedMinutes < 0)
            return true;
        else
            Toast.makeText(context, "You sent a feedback recently", Toast.LENGTH_SHORT).show();
        return false;
        //long elapsedSeconds = different / secondsInMilli;

    }
}
