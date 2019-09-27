package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import br.com.bwsystemssolutions.controlediabetes.util.Filters;

public class BolusDetailActivity extends AppCompatActivity {

    BolusTableData mBolusTableData;
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

        initComponents();
        setFilters();
    }

    private void setFilters() {
        final InputFilter[] inputFilter = {Filters.DecimalDigits(2, 2)};
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
