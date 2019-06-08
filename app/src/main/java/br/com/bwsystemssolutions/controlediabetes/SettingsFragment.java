package br.com.bwsystemssolutions.controlediabetes;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, View.OnClickListener {

    private EditTextPreference mglicemiaBaixaEditText;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);

        mglicemiaBaixaEditText = (EditTextPreference) findPreference("glicemia_Baixa");
        int i = mglicemiaBaixaEditText.getEditText().getInputType();
        Log.d("bwvm", "onCreatePreferences: "+ i);

        mglicemiaBaixaEditText.setOnPreferenceChangeListener(this);
        mglicemiaBaixaEditText.setOnPreferenceClickListener(this);

        mglicemiaBaixaEditText.getEditText().setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("bwvm", "onResume: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("bwvm", "onStart: ");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Log.d("bwvm", "onPreferenceChange: " + preference.getKey() + " alterado" );
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.d("bwvm", "onPreferenceClick: clicado" );
        return false;
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        super.onDisplayPreferenceDialog(preference);

    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button){
            Button b = (Button) v;
            b.getText().toString();
            Log.d("bwvm", "onClick: on button " + b.getText().toString());
        }
        Log.d("bwvm", "onClick: clicado");
    }
}
