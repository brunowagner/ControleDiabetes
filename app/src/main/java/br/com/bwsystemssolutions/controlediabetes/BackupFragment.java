package br.com.bwsystemssolutions.controlediabetes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class BackupFragment extends PreferenceFragmentCompat {

    private ListPreference mPeriodoListPreference;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_backup, rootKey);

        mPeriodoListPreference = (ListPreference) findPreference(getString(R.string.pref_backup_list_peri_key));
    }
}
