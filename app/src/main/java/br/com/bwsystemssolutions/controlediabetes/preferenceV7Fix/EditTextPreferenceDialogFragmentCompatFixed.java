package br.com.bwsystemssolutions.controlediabetes.preferenceV7Fix;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditTextPreferenceDialogFragmentCompatFixed extends android.support.v7.preference.EditTextPreferenceDialogFragmentCompat {

    private EditText mEditText;
    private int mInputType;

    private static final String INPUT_TYPE = "inputType";
    private static final String KEY = "key";

    public static EditTextPreferenceDialogFragmentCompatFixed newInstance (String key, int inputType){
        EditTextPreferenceDialogFragmentCompatFixed fragmentCompat = new EditTextPreferenceDialogFragmentCompatFixed();

        Bundle b = new Bundle(2);
        b.putString(KEY, key);
        b.putInt(INPUT_TYPE, inputType);
        fragmentCompat.setArguments(b);
        return fragmentCompat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputType = this.getArguments().getInt(INPUT_TYPE);
    }

    @Override
    protected void onBindDialogView(View view) {
        this.mEditText = view.findViewById(android.R.id.edit);
        mEditText.setInputType(mInputType);
        super.onBindDialogView(view);
    }
}
