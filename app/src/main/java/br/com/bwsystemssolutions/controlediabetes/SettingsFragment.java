package br.com.bwsystemssolutions.controlediabetes;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat   {

    private EditTextPreference mglicemiaBaixaEditText;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);

        mglicemiaBaixaEditText = (EditTextPreference) findPreference("glicemia_Baixa");
        int i = mglicemiaBaixaEditText.getEditText().getInputType();
        Log.d("bwvm", "onCreatePreferences: "+ i);
    }

//    @Override
//    public void onCreatePreferences(Bundle bundle, String s) {
//        //addPreferencesFromResource(R.xml.pref_main);
//        setPreferencesFromResource(R.xml.pref_main, );
//
//        mglicemiaBaixaEditText = (EditTextPreference) findPreference("glicemia_Baixa");
//        int i = mglicemiaBaixaEditText.getEditText().getInputType();
//        Log.d("bwvm", "onCreatePreferences: "+ i);
//    }

}
