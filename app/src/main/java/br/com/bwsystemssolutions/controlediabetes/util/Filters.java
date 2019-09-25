package br.com.bwsystemssolutions.controlediabetes.util;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filters {

    private static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        private DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }

    public static InputFilter DecimalDigits(int digitsBeforeZero,int digitsAfterZero){
        return new DecimalDigitsInputFilter(digitsBeforeZero, digitsAfterZero);
    }
}
