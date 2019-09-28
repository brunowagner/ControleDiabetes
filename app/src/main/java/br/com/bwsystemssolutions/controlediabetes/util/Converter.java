package br.com.bwsystemssolutions.controlediabetes.util;

import android.support.annotation.NonNull;

public class Converter {
    public static Double toDouble(@NonNull String s) throws NumberFormatException{
        if (s.length() == 0) s = "0.0";
        return Double.valueOf(s);
    }

    public static int toInt(@NonNull String s) throws NumberFormatException {
        if (s.length() == 0) s = "0";
        return Integer.parseInt(s);
    }
}
