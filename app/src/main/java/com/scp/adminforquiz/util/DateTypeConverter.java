package com.scp.adminforquiz.util;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTypeConverter {

    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat DATE_SHOW = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public static String formatDate(Date date) {
        return DATE_SHOW.format(date);
    }

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public String dateToString(Date date) {
        return DATE_FORMAT.format(String.valueOf(date));
    }

    @TypeConverter
    public Date fromTimestamp(String value) {
        try {
            return DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
