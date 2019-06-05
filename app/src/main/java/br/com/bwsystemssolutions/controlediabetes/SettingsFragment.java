package br.com.bwsystemssolutions.controlediabetes;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import br.com.bwsystemssolutions.controlediabetes.preferenceV7Fix.EditTextPreferenceDialogFragmentCompatFixed;

public class SettingsFragment extends PreferenceFragmentCompat implements PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback  {

    private EditTextPreference mglicemiaBaixaEditText;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_main);

        mglicemiaBaixaEditText = (EditTextPreference) findPreference("glicemia_Baixa");
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {

        String key = preference.getKey();
        Log.d("bwvm", "onDisplayPreferenceDialog: key" + key);
        if (preference instanceof EditTextPreference){
            EditTextPreferenceDialogFragmentCompatFixed f;
            f = EditTextPreferenceDialogFragmentCompatFixed.newInstance(key,InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(),"android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }


    @Override
    public boolean onPreferenceDisplayDialog(@NonNull PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        String key = preference.getKey();
        Log.d("bwvm", "onPreferenceDisplayDialog: key" + key);
        if (preference instanceof EditTextPreference){
            EditTextPreferenceDialogFragmentCompatFixed f;
            f = EditTextPreferenceDialogFragmentCompatFixed.newInstance(key,InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(),"android.support.v7.preference.PreferenceFragment.DIALOG");
            return true;
        }

        return false;
    }



}
