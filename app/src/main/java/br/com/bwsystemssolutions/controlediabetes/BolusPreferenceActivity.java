package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.ListPreference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class BolusPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_preference); //layout contendo um framelayout vazio

        //Coloca o fragmento no lugar do frameLayout vazio.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_bolus_content_fragment, new BolusFragment())
                .commit();
    }

    public static class BolusFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
        Context mContext;
        ListPreference mGraduationListPreference;
        ListPreference mMethodListPreference;
        Preference mConfigureTablePreference;
        Preference mConfigureCalculatePreference;


        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_bolus, rootKey);

            mGraduationListPreference = (ListPreference) findPreference(getString(R.string.pref_bolus_select_graduation_key));
            mMethodListPreference = (ListPreference) findPreference(getString(R.string.pref_bolus_list_method_key));
            mConfigureCalculatePreference = findPreference(getString(R.string.pref_bolus_configure_calculate_key));
            mConfigureTablePreference = findPreference(getString(R.string.pref_bolus_configure_table_key));

            mConfigureTablePreference.setOnPreferenceClickListener(this);
            mConfigureCalculatePreference.setOnPreferenceClickListener(this);

            mMethodListPreference.setSummary(mMethodListPreference.getEntry());
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = context;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key == mConfigureTablePreference.getKey()){
                Intent intent = new Intent(mContext, BolusTableActivity.class);
                startActivity(intent);
            }

            if (key == mConfigureCalculatePreference.getKey()){
                Intent intent = new Intent(mContext, BolusCalculateConfig.class);
                startActivity(intent);
            }

            return true;
        }

        @Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Unregister the listener whenever a key changes
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference p = findPreference(key);
            if (p instanceof ListPreference) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry());
            }
        }
    }

}
