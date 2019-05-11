package br.com.bwsystemssolutions.controlediabetes.classe;

import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    public static final String DEFAULT_DATE_FORMAT = "dd/mm/yyyy";
    public static final String DEFAULT_TIME_FORMAT = "HH:MM";
    public static final String SQLITE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String convertDateToString(Date dtData, String format){
        return converter(dtData,format);
    }

    public static String convertTimeToString(Date dtData, String format){
        return converter(dtData,format);
    }

    public static String convertDateTimeToSQLiteFormat(String date, String time){


        SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATETIME_FORMAT);
        try {
            Date d  = sdf.parse(date + " " + time);
            return converter(d, SQLITE_DATETIME_FORMAT);

        } catch (ParseException ex) {
            Log.d("BWVM", "convertDateTimeToSQLiteFormat: " + ex.getLocalizedMessage());
            return null;
        }
    }








    private static String converter(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);

        return strDate;
    }


}
