package br.com.bwsystemssolutions.controlediabetes;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bwsystemssolutions.controlediabetes.androidFileAndDirectoryPicker.PickerByDialog;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BackupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_backup_content_fragment, new BackupFragment())
                .commit();
    }

    public static class BackupFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

        Context mContext;
        Preference mCriarBackupPreference;
        Preference mRestoreFromBackup;
        SwitchPreference mAutoBackupSwitchPreference;
        ListPreference mPeriodoListPreference;

        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_backup, rootKey);

            mCriarBackupPreference = findPreference(getString(R.string.pref_backup_create_key));
            mRestoreFromBackup = findPreference(getString(R.string.pref_backup_restore_key));
            mAutoBackupSwitchPreference = (SwitchPreference) findPreference(getString(R.string.pref_backup_autobackup_key));
            mPeriodoListPreference = (ListPreference) findPreference(getString(R.string.pref_backup_list_peri_key));

            mCriarBackupPreference.setOnPreferenceClickListener(this);
            mRestoreFromBackup.setOnPreferenceClickListener(this);
            mAutoBackupSwitchPreference.setOnPreferenceChangeListener(this);

            mPeriodoListPreference.setEnabled(mAutoBackupSwitchPreference.isChecked());
            mPeriodoListPreference.setSummary(mPeriodoListPreference.getEntry());
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = context;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key == mCriarBackupPreference.getKey()){
                createBackup();
            }

            if (key == mRestoreFromBackup.getKey()){
                restoreBackup();
            }
            return true;
        }

        public void restoreBackup() {
//            String path = getExternalFilesDir(null).getAbsolutePath();
//            String path = getFilesDir().getParent();
//            String path = "//data//data//br.com.bwsystemssolutions.controlediabetes";

            if (hasExternalStorageReadPermission()){
                //continue
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }

            String path = Environment.getExternalStorageDirectory().toString();


            PickerByDialog picker = new PickerByDialog(mContext, path);
            picker.setItemBackgroundColor(getResources().getColor(R.color.colorDefaultBackgroudListView),getResources().getColor(R.color.colorSelectedBackgroundItem));
            picker.setSelectType(PickerByDialog.SELECT_TYPE_FILE);
            picker.setOnResponseListener(new PickerByDialog.OnResponseListener() {
                @Override
                public void onResponse(boolean canceled, String response) {
                    if (canceled){
                        Log.d("bwvm", "onResponse: Canceled!");
                    } else {
                        Log.d("bwvm", "onResponse: Selected: " + response);

                        try {
                            Log.d("bwvm", "onCreate: Tentou recuperar backup!");
                            // Configurando o banco de dados
                            CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(mContext);
                            dbHelper.importDB(response);
                            //CalculoDeBolusDBHelper.exportDataBase(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            //        "//ControleDeDiabetes//Backup//BackupDB_" + dataFormatada + ".db");
                            Toast.makeText(mContext,"Backup Realizado com sucesso.", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Log.d("bwvm", "onCreate: excessão ao criar backup.");
                            e.printStackTrace();
                            Toast.makeText(mContext,"Falha ao criar o backup.", Toast.LENGTH_LONG).show();

                        }

                    }
                }
            });
            picker.show();
        }

        public void createBackup(){
            Date date = new Date();
            SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd_HHmm");
            String dataFormatada = formataData.format(date);

            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//ControleDeDiabetes//Backup");
            if (!f.exists()){ f.mkdirs(); }

            try {
                Log.d("bwvm", "onCreate: Tentou criar backup!");
                // Configurando o banco de dados
                CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(mContext);
                dbHelper.exportDB(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "//ControleDeDiabetes//Backup//BackupDB_" + dataFormatada + ".db");
                //CalculoDeBolusDBHelper.exportDataBase(Environment.getExternalStorageDirectory().getAbsolutePath() +
                //        "//ControleDeDiabetes//Backup//BackupDB_" + dataFormatada + ".db");
                Toast.makeText(mContext,"Backup Realizado com sucesso.", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                Log.d("bwvm", "onCreate: excessão ao criar backup.");
                e.printStackTrace();
                Toast.makeText(mContext,"Falha ao criar o backup.", Toast.LENGTH_LONG).show();

            }
        }

        private boolean hasExternalStorageWritePermission(){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        }

        private boolean hasExternalStorageReadPermission(){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
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
        public boolean onPreferenceChange(Preference preference, Object o) {

            mPeriodoListPreference.setEnabled((boolean) o);

            return true;
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
