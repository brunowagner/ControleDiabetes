package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.classe.Utilidades;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusTableDataDAO;
import br.com.bwsystemssolutions.controlediabetes.util.Converter;
import br.com.bwsystemssolutions.controlediabetes.util.Filters;

public class BolusDetailActivity extends AppCompatActivity {

    private BolusTableData mBolusTableData;
    SQLiteDatabase mDb;

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

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(BolusTableData.BUNDLE_STRING_KEY)){
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            mBolusTableData = (BolusTableData) bundle.getSerializable(BolusTableData.BUNDLE_STRING_KEY);
        }

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
                // TODO criar ação para salvar o registro
                boolean saved = save();
                if (saved){
                    Toast.makeText(this,"Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this,"Não foi possível salvar!", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean save(){
        BolusTableData bolusTableData = new BolusTableData();
        bolusTableData.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusTableData.setInsulin1(Converter.toDouble(mBreakFastEditText.getText().toString()));
        bolusTableData.setInsulin2(Converter.toDouble(mBrunchEditText.getText().toString()));
        bolusTableData.setInsulin3(Converter.toDouble(mLunchEditText.getText().toString()));
        bolusTableData.setInsulin4(Converter.toDouble(mTeaEditText.getText().toString()));
        bolusTableData.setInsulin5(Converter.toDouble(mDinnerEditText.getText().toString()));
        bolusTableData.setInsulin6(Converter.toDouble(mSupperEditText.getText().toString()));
        bolusTableData.setInsulin7(Converter.toDouble(mDawnEditText.getText().toString()));

        BolusTableDataDAO bolusTableDataDAO= new BolusTableDataDAO(this);
        return bolusTableDataDAO.add(bolusTableData);
    }

    // Data

//    private boolean saveData(){
//        isSaved = false;
//        boolean executed = false;
//
//        if (mBolusTableData == null || mBolusTableData.getId() == 0){
//            if (!validateFields(true)) { return false;}
//
//            addRecord();
//
//        } else {
//            if (!validateFields(false)) { return false;}
//
//            updateRecord();
//        }
//
//        if (isSaved){ executed = true; }
//
//        return executed;
//    }
//
//    private boolean validateFields(boolean isToSave){
//        boolean validate = false;
//        String message = "";
//
//        // se algum campo não for preenchido
//        if (mDataEditText.getText().length() == 0 || mHoraEditText.getText().length() == 0 ) {
//            //mEventoEditText.getText().length() == 0) {
//
//
//            message = "A data, a hora ou o evento não foi preenchido!";
//
//        } else if (mCarboidratoEditText.getText().length() == 0 && mGlicemiaEditText.getText().length() == 0 &&
//                mInsulinaBasalEditText.getText().length() == 0 && mInsulinaRapidaEditText.getText().length() == 0) {
//
//            message = "Preencha pelo menos um dos campos abaixo:\n" +
//                    "- Carbohidrato\n" +
//                    "- Glicemia\n" +
//                    "- Insulina Rápida\n" +
//                    "- Insulina Basal";
//        } else if (isToSave && mDataEditText.getText().toString().length() > 0 && mDataEditText.getText().toString().length() > 0 && existsRegister(mDataEditText.getText().toString(), mHoraEditText.getText().toString()   )){
//            message = "Data e hora ja' existem.\nO registro não pôde ser salvo.";
//
//        } else {
//            validate = true;
//        }
//
//        if (!validate) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(message)
//                    .setTitle("Atenção!")
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //do nothing
//                        }
//                    });
//
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//
//        return validate;
//    }
//
//    private boolean existsRegister(String date, String time){
//        String selection = CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME + "=?";
//        String[] args = new String[] { Utilidades.convertDateTimeToSQLiteFormat(date, time) };
//
//        Cursor cursor = mDb.query(CalculoDeBolusContract.RecordEntry.TABLE_NAME, new String[]{CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME}, selection, args, null, null, null);
//
//        Log.d("bwvm", "existsRegister: Tamanho do cursor: " + cursor.getCount());
//        return cursor.getCount() > 0 ? true : false;
//    }
//
//    private void addRecord(){
//
//        ContentValues cv = new ContentValues();
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME, Utilidades.convertDateTimeToSQLiteFormat(mDataEditText.getText().toString(),  mHoraEditText.getText().toString())  );
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME, mCarboidratoEditText.getText().toString());
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME, mGlicemiaEditText.getText().toString());
//        //cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, mEventoEditText.getText().toString());
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, String.valueOf(mEventSpinner.getSelectedItem()));
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_NAME, String.valueOf(mMealSpinner.getSelectedItem()));
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_TIME_NAME, String.valueOf(mMealTimeSpinner.getSelectedItem()));
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME, mInsulinaRapidaEditText.getText().toString());
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME, mInsulinaBasalEditText.getText().toString());
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME, mDoenteCheckBox.getText().toString());
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME, mMedicamentoCheckBox.getText().toString());
//        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME, mObservacaoEditText.getText().toString());
//
//        long saveds =  mDb.insert(CalculoDeBolusContract.RecordEntry.TABLE_NAME, null, cv);
//
//        if (saveds > 0) {
//            isSaved = true;
//            Toast.makeText(getApplicationContext(), "Salvo!", Toast.LENGTH_SHORT).show();
//            finish();
//        } else {
//            isSaved = false;
//            Toast.makeText(getApplicationContext(), "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void updateRecord(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Deseja realmente alterar o registro?")
//                .setTitle("Atenção!")
//                .setNegativeButton("Cancelar", null)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("bwvm", "updateRecord: Iniciou");
//                        ContentValues cv = new ContentValues();
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME, Utilidades.convertDateTimeToSQLiteFormat(mDataEditText.getText().toString(),  mHoraEditText.getText().toString())  );
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME, mCarboidratoEditText.getText().toString());
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME, mGlicemiaEditText.getText().toString());
//                        //cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, mEventoEditText.getText().toString());
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, String.valueOf(mEventSpinner.getSelectedItem()));
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_NAME, String.valueOf(mMealSpinner.getSelectedItem()));
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_TIME_NAME, String.valueOf(mMealTimeSpinner.getSelectedItem()));
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME, mInsulinaRapidaEditText.getText().toString());
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME, mInsulinaBasalEditText.getText().toString());
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME, mDoenteCheckBox.getText().toString());
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME, mMedicamentoCheckBox.getText().toString());
//                        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME, mObservacaoEditText.getText().toString());
//
//                        String where = CalculoDeBolusContract.RecordEntry._ID + "=" + mRecord.getId();
//
//                        Log.d("bwvm", "onClick: fara o update!");
//                        int saveds = mDb.update(CalculoDeBolusContract.RecordEntry.TABLE_NAME, cv, where, null);
//
//                        if (saveds > 0) {
//                            isSaved = true;
//                            Toast.makeText(getApplicationContext(), "Salvo!", Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            isSaved = false;
//                            Toast.makeText(getApplicationContext(), "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
}
