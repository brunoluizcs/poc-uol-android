package com.uol.poc.brunosantos.pocuol.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by brunosantos on 26/01/2018.
 */

public class DateUtils {
    public static final String FORMAT_SHORT_DATE = "dd/MM";

    public static class DateTypeDeserializer implements JsonDeserializer<Date> {
        private final String[] DATE_FORMATS = new String[]{
                "yyyyMMddHHmmss",
                "dd/MM/yyyy HH:mm",
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd",
                "EEE MMM dd HH:mm:ss z yyyy",
                "HH:mm:ss",
                "MM/dd/yyyy HH:mm:ss aaa",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'",
                "MMM d',' yyyy H:mm:ss a"
        };

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.getDefault()).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            return null;
        }
    }


    public static String parseDate(Date date){
        return parseDate(FORMAT_SHORT_DATE, date);
    }

    public static String parseDate(String format,Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format,Locale.getDefault());
        return simpleDateFormat.format(date);
    }


}
