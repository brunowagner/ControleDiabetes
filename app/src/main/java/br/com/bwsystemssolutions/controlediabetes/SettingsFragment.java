package br.com.bwsystemssolutions.controlediabetes;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat  {

    private EditTextPreference mglicemiaBaixaEditText;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_main);

        mglicemiaBaixaEditText = (EditTextPreference) findPreference("glicemia_Baixa");

    }

}
