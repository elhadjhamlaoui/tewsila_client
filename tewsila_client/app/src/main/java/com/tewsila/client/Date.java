package com.tewsila.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by elhadj on 31/07/2018.
 */

public class Date {
    static String date(){
        java.util.Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }
}
