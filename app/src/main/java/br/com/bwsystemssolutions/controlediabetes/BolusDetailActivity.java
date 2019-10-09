package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusTableDataDAO;
import br.com.bwsystemssolutions.controlediabetes.util.Alert;
import br.com.bwsystemssolutions.controlediabetes.util.Converter;
import br.com.bwsystemssolutions.controlediabetes.util.Filters;

public class BolusDetailActivity extends AppCompatActivity {

    private BolusTableData mBolusTableData;
    private BolusTableDataDAO mBolusTableDataDAO;
    private boolean mEditAction = false;

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

        //Verifica se veio objeto e set a variavel que informa que trata-se de edição ou não.
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(BolusTableData.BUNDLE_STRING_KEY)){
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            mBolusTableData = (BolusTableData) bundle.getSerializable(BolusTableData.BUNDLE_STRING_KEY);
            mEditAction = true;
        }

        mBolusTableDataDAO = new BolusTableDataDAO(this);

        initComponents();
        setFilters();
        fillComponents();
    }

    private void setFilters() {
        final InputFilter[] inputFilter = {Filters.DecimalDigits(3, 2)};
        //mGlucoseEditText.setFilters(inputFilter);
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

    private void fillComponents() {
        if (mBolusTableData == null) return;

        mGlucoseEditText.setText(String.valueOf(mBolusTableData.getGlucose()));
        mBreakFastEditText.setText(String.valueOf(mBolusTableData.getInsulin1()));
        mBrunchEditText.setText(String.valueOf(mBolusTableData.getInsulin2()));
        mLunchEditText.setText(String.valueOf(mBolusTableData.getInsulin3()));
        mTeaEditText.setText(String.valueOf(mBolusTableData.getInsulin4()));
        mDinnerEditText.setText(String.valueOf(mBolusTableData.getInsulin5()));
        mSupperEditText.setText(String.valueOf(mBolusTableData.getInsulin6()));
        mDawnEditText.setText(String.valueOf(mBolusTableData.getInsulin7()));
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
                if (mEditAction) {
                    update();
                } else {
                    save();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DialogInterface.OnClickListener replaceClick(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fillObjectWithActivityData(mBolusTableData);
                final boolean updated = mBolusTableDataDAO.update(mBolusTableData);
                if (updated){
                    Toast.makeText(BolusDetailActivity.this,"Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BolusDetailActivity.this,"Alteração não realizada!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        };
    }

    private void save(){
        mBolusTableData = mBolusTableDataDAO.fetchByGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));

        //Se já existir a glicemia
        if (mBolusTableData != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Atenção!")
                    .setMessage("Glicemia informada já existe.\nDeseja substituir?")
                    .setPositiveButton("Substituir", replaceClick())//new DialogInterface.OnClickListener() {
                    .setNegativeButton("Cancelar", null)
                    .create()
                    .show();

        } else {
            mBolusTableData = new BolusTableData();
            fillObjectWithActivityData(mBolusTableData);
             mBolusTableDataDAO.add(mBolusTableData);
            Toast.makeText(BolusDetailActivity.this,"Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean update(){


        int glucose = Converter.toInt(mGlucoseEditText.getText().toString());

        if (glucose != mBolusTableData.getGlucose() && existsGlucose(glucose)) {
            Alert.ok(this,"Atenção!",
                    "Este valor de glicemia já está configurado.\nUtilize outro valor de glicemia que ainda não foi utilizado.", "Ok", null).show();
            return false;
        }

        fillObjectWithActivityData(mBolusTableData);
        boolean updated = mBolusTableDataDAO.update(mBolusTableData);
        if (updated) {
            Toast.makeText(BolusDetailActivity.this,"Editado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(BolusDetailActivity.this,"Não foi possível editar!", Toast.LENGTH_SHORT).show();
        }
        return updated;
    }

    private boolean existsGlucose(int glucose){
        return mBolusTableDataDAO.fetchByGlucose(glucose) != null;
    }

    private void fillObjectWithActivityData(BolusTableData bolusTableData) {
        bolusTableData.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusTableData.setInsulin1(Converter.toDouble(mBreakFastEditText.getText().toString()));
        bolusTableData.setInsulin2(Converter.toDouble(mBrunchEditText.getText().toString()));
        bolusTableData.setInsulin3(Converter.toDouble(mLunchEditText.getText().toString()));
        bolusTableData.setInsulin4(Converter.toDouble(mTeaEditText.getText().toString()));
        bolusTableData.setInsulin5(Converter.toDouble(mDinnerEditText.getText().toString()));
        bolusTableData.setInsulin6(Converter.toDouble(mSupperEditText.getText().toString()));
        bolusTableData.setInsulin7(Converter.toDouble(mDawnEditText.getText().toString()));
    }
}
