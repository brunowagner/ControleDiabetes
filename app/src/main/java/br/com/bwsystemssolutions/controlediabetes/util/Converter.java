package br.com.bwsystemssolutions.controlediabetes.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;

public class Converter {
    public static Double toDouble(@NonNull String s) throws NumberFormatException{
        if (s.length() == 0) s = "0.0";
        return Double.valueOf(s);
    }

    public static int toInt(@NonNull String s) throws NumberFormatException {
        if (s.length() == 0) s = "0";
        return Integer.parseInt(s);
    }

    public static int toPx(@NonNull Context context, @NonNull int valueInDP){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDP, context.getResources().getDisplayMetrics());
    }
}
