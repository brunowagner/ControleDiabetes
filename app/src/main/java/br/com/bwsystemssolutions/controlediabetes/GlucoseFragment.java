package br.com.bwsystemssolutions.controlediabetes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class GlucoseFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private EditTextPreference mglicemiaBaixaEditText;
    private EditTextPreference mglicemiaAltaEditText;
    private EditTextPreference mglicemiaNormalEditText;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_glicemia, rootKey);

        mglicemiaBaixaEditText = (EditTextPreference) findPreference("glicemia_Baixa");
        mglicemiaAltaEditText = (EditTextPreference) findPreference("glicemia_Alta");
        mglicemiaNormalEditText = (EditTextPreference) findPreference("glicemia_Normal");


        int i = mglicemiaBaixaEditText.getEditText().getInputType();
        Log.d("bwvm", "onCreatePreferences: "+ i);

        mglicemiaBaixaEditText.setOnPreferenceChangeListener(this);
        mglicemiaAltaEditText.setOnPreferenceChangeListener(this);
        mglicemiaNormalEditText.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        boolean modificacao = true;
        String key = preference.getKey();

        if (key.equals("glicemia_Baixa")){
            int newValue = Integer.parseInt(o.toString());
            int normalValue = Integer.parseInt(mglicemiaNormalEditText.getText());

            if (newValue >= normalValue) {
                Toast.makeText(this.getContext(),"Não alterado: Glicemia baixa deve ser abaixo da Normal.", Toast.LENGTH_LONG).show();
                modificacao = false;
            }
        }

        if (key.equals("glicemia_Alta")){
            int newValue = Integer.parseInt(o.toString());
            int normalValue = Integer.parseInt(mglicemiaNormalEditText.getText());

            if (newValue <= normalValue) {
                Toast.makeText(this.getContext(), "Não alterado: Glicemia alta deve ser acima da Normal.", Toast.LENGTH_LONG).show();
                modificacao = false;
            }
        }

        if (key.equals("glicemia_Normal")){
            int newValue = Integer.parseInt(o.toString());
            int highValue = Integer.parseInt(mglicemiaAltaEditText.getText());
            int lowValue = Integer.parseInt(mglicemiaBaixaEditText.getText());

            if (newValue <= lowValue || newValue >= highValue){
                Toast.makeText(this.getContext(),"Não alterado: Glicemia Normal deve ser entre a baixa e a alta.", Toast.LENGTH_LONG).show();
                modificacao = false;
            }
        }

        return modificacao;
    }
}
