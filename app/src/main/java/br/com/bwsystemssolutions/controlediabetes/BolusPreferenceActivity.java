package br.com.bwsystemssolutions.controlediabetes;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class BolusPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_preference);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_bolus_content_fragment, new BolusFragment())
                .commit();
    }

    public static class BolusFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {

        }
    }

}
