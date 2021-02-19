package br.com.bwsystemssolutions.controlediabetes.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class  SoftKeyboard {

    public static void hide(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static View.OnTouchListener hideOnTouch(final Activity activity){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hide(activity);
                return false;
            }
        };
    }

    public static View.OnClickListener hideOnClick(final Activity activity){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(activity);
            }

        };
    }
}
