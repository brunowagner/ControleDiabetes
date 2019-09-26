package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.zip.Inflater;

import br.com.bwsystemssolutions.controlediabetes.util.Filters;

public class BolusDetailActivity extends AppCompatActivity {

    private EditText mGlucoseEditText;
    private EditText mBreakFastEditText; //café da manhã
    private EditText mBrunchEditText;    // colação
    private EditText mLunchEditText;     //almoço
    private EditText mTeaEditText;       //lanche da tarde
    private EditText mDinnerEditText;    //jantar
    private EditText mSupperEditText;    //ceia
    private EditText mDawnEditText;      //madrugada

    private final String TAG = "bwvm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_detail);

        initComponents();
        setFilters();
    }

    private void setFilters() {
        final InputFilter[] inputFilter = {Filters.DecimalDigits(3, 2)};
        mGlucoseEditText.setFilters(inputFilter);
        mBreakFastEditText.setFilters(inputFilter);
        mBrunchEditText.setFilters(inputFilter);
        mLunchEditText.setFilters(inputFilter);
        mTeaEditText.setFilters(inputFilter);
        mDinnerEditText.setFilters(inputFilter);
        mSupperEditText.setFilters(inputFilter);
        mDawnEditText.setFilters(inputFilter);
    }

    private void initComponents() {
        mGlucoseEditText = findViewById(R.id.et_glicemia);
        mBreakFastEditText = findViewById(R.id.et_cafe_da_manha);
        mBrunchEditText = findViewById(R.id.et_colacao);
        mLunchEditText = findViewById(R.id.et_almoco);
        mTeaEditText = findViewById(R.id.et_lanche);
        mDinnerEditText = findViewById(R.id.et_jantar);
        mSupperEditText = findViewById(R.id.et_ceia);
        mDawnEditText = findViewById(R.id.et_madrugada);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_time_block_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_save:
                // TODO criar ação para salvar o registro
                break;
        }
        return true;
    }
}
