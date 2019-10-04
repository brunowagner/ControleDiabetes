package br.com.bwsystemssolutions.controlediabetes.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {

    public static AlertDialog okCancelNeutral (Context context, String title, String message,
                        String positiveButtonLable, DialogInterface.OnClickListener positiveClick,
                        String negativeButtonLable, DialogInterface.OnClickListener negativeClick,
                        String neutralButtonLabel, DialogInterface.OnClickListener neutralClick){

        final AlertDialog.Builder builder = getDefaultBuilder(context,title,message)
                .setPositiveButton(positiveButtonLable, positiveClick)
                .setNegativeButton(negativeButtonLable, negativeClick)
                .setNeutralButton(neutralButtonLabel,neutralClick);

        return builder.create();
    }

    public static AlertDialog okCancel (Context context, String title, String message,
                                   String positiveButtonLable, DialogInterface.OnClickListener positiveClick,
                                   String negativeButtonLable, DialogInterface.OnClickListener negativeClick){

        final AlertDialog.Builder builder = getDefaultBuilder(context,title,message)
                .setPositiveButton(positiveButtonLable, positiveClick)
                .setNegativeButton(negativeButtonLable, negativeClick);

        return builder.create();
    }

    public static AlertDialog ok (Context context, String title, String message,
                                 String positiveButtonLable, DialogInterface.OnClickListener positiveClick){

        final AlertDialog.Builder builder = getDefaultBuilder(context,title,message)
                .setPositiveButton(positiveButtonLable, positiveClick);

        return builder.create();
    }


    private static AlertDialog.Builder getDefaultBuilder(Context context, String title, String message){
        return new AlertDialog.Builder(context).setTitle(title).setMessage(message);
    }
}
