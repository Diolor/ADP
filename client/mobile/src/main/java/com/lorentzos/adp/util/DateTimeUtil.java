package com.lorentzos.adp.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dionysis_lorentzos on 11/3/15
 * for package com.lorentzos.adp.util
 * Use with caution dinosaurs might appear!
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {


    public static SimpleDateFormat isoWithMicro = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    public static SimpleDateFormat listViewFullDate = new SimpleDateFormat("MMM d yyyy, HH:mm:ss");


    /**
     * Formats the date from ISO to a more elegant format
     *
     * @param inputDate the string in a {@link #isoWithMicro} format
     * @return the formatted date or an empty string
     */
    public static String getDateForListView(String inputDate){
        try {
            Date date = DateTimeUtil.isoWithMicro.parse(inputDate);
            return  DateTimeUtil.listViewFullDate.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

}
