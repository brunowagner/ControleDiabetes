package br.com.bwsystemssolutions.controlediabetes.classe;

import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm";
    public static final String SQLITE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String convertDateToString(Date dtData, String format){
        return converter(dtData,format);
    }

    public static String convertTimeToString(Date dtData, String format){
        return converter(dtData,format);
    }

    public static String convertDateTimeToSQLiteFormat(String date, String time){

        String dateTime = date + " " + time;
        SimpleDateFormat in = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data = null;
        try {
            data = in.parse(dateTime);
        } catch (ParseException e) {
            Log.d("BWVM", "convertDateTimeToSQLiteFormat: " + e.getLocalizedMessage());
            return null;
        }
        in.applyPattern(SQLITE_DATETIME_FORMAT);
        String convertedDateTime = in.format(data);

        Log.d("bwvm", "convertDateTimeToSQLiteFormat: Data Formatada  " + convertedDateTime);

        return convertedDateTime;
    }


    private static String converter(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);

        return strDate;
    }


}
