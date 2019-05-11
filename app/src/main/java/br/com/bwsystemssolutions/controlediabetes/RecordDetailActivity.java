package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.classe.Utilidades;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class RecordDetailActivity extends AppCompatActivity {

    public EditText mDataEditText;
    public  EditText mHoraEditText;
    public  EditText mGlicemiaEditText;
    public  EditText mEventoEditText;
    public  EditText mCarboidratoEditText;
    public  EditText mInsulinaRapidaEditText;
    public  EditText mInsulinaBasalEditText;
    public  CheckBox mDoenteCheckBox;
    public  CheckBox mMedicamentoCheckBox;
    public  EditText mObservacaoEditText;

    public  ImageButton mTimePickerImageButton;
    public  ImageButton mDatePickerImageButton;
    TimePickerDialog mTimePickerDialog;
    DatePickerDialog mDatePickerDialog;

    Record mRecord;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        mDataEditText = (EditText) findViewById(R.id.et_data_record);
        mTimePickerImageButton = (ImageButton) findViewById(R.id.ibtn_time_picker_record);
        mHoraEditText = (EditText) findViewById(R.id.et_hora_record);
        mDatePickerImageButton = (ImageButton) findViewById(R.id.ibtn_date_picker_record);
        mGlicemiaEditText = (EditText) findViewById(R.id.et_glicemia_record);
        mEventoEditText = (EditText) findViewById(R.id.et_evento_record);
        mCarboidratoEditText = (EditText) findViewById(R.id.et_carboidrato_record);
        mInsulinaRapidaEditText = (EditText) findViewById(R.id.et_insulina_rapida_record);
        mInsulinaBasalEditText = (EditText) findViewById(R.id.et_insulina_basal_record);
        mDoenteCheckBox = (CheckBox) findViewById(R.id.cb_doente_record);
        mMedicamentoCheckBox = (CheckBox) findViewById(R.id.et_medicamento_record);
        mObservacaoEditText = (EditText) findViewById(R.id.et_observacao_record);

        mTimePickerImageButton.setOnClickListener(new ClickHandler());

        mDatePickerImageButton.setOnClickListener(new ClickHandler());

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(Record.BUNDLE_STRING_KEY)){
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            mRecord = (Record) bundle.getSerializable(Record.BUNDLE_STRING_KEY);
        }

        loadData();

        configureDb();
    }

    private void configureDb(){
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void loadData(){
        if (mRecord == null){ return; }

        mDataEditText.setText(Utilidades.convertDateToString(mRecord.getDate(), Utilidades.DEFAULT_DATE_FORMAT));
        mHoraEditText.setText(Utilidades.convertTimeToString(mRecord.getDate(), Utilidades.DEFAULT_TIME_FORMAT));
        mGlicemiaEditText.setText(mRecord.getGlucose());
        mEventoEditText.setText(mRecord.getEvent());
        mCarboidratoEditText.setText(mRecord.getCarbohydrate());
        mInsulinaRapidaEditText.setText(String.valueOf(mRecord.getFastInsulin()));
        mInsulinaBasalEditText.setText(String.valueOf(mRecord.getBasalInsulin()));
        mDoenteCheckBox.setChecked(mRecord.isSick());
        mMedicamentoCheckBox.setChecked(mRecord.isMedicament());
        mObservacaoEditText.setText(mRecord.getNote());
    }

    private boolean saveData(){
        if (!validateData()) { return false;}

        boolean executed = false;

        if (mRecord == null){

            long add = addTimeBlock();
            if (add > 0){ executed = true; }
        } else {
            int update = updateTimeBlock();
            if (update > 0){ executed = true; }
        }
        return executed;
    }

    private boolean validateData(){
        boolean validate = false;
        String message = "";

        // se algum campo não for preenchido
        if (mDataEditText.getText().length() == 0 || mHoraEditText.getText().length() == 0 ||
                mEventoEditText.getText().length() == 0) {

            message = "A data, a hora ou o evento não foi preenchido!";

            //se o campo inicio foi preenchido e já existir o bloco de horas
        } else if (mDataEditText.getText().toString().length() > 0 && mDataEditText.getText().toString().length() > 0 && existsRegister(mDataEditText.getText().toString(), mHoraEditText.getText().toString()   )){
            message = "Data e hora ja' existem.\nO registro não pôde ser salvo.";
        } else {
            validate = true;
        }

        if (!validate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setTitle("Atenção!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }

        return validate;
    }

    private boolean existsRegister(String date, String time){
        String selection = CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME + "=?";
        String[] args = new String[] { Utilidades.convertDateTimeToSQLiteFormat(date, time) };

        Cursor cursor = mDb.query(CalculoDeBolusContract.RecordEntry.TABLE_NAME, new String[]{CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME}, selection, args, null, null, null);

        Log.d("bwvm", "existsRegister: Tamanho do cursor: " + cursor.getCount());
        return cursor.getCount() > 0 ? true : false;
    }

    private long addTimeBlock(){
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME, Utilidades.convertDateTimeToSQLiteFormat(mDataEditText.getText().toString(),  mHoraEditText.getText().toString())  );
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME, mCarboidratoEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME, mGlicemiaEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, mEventoEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME, mInsulinaRapidaEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME, mInsulinaBasalEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME, mDoenteCheckBox.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME, mMedicamentoCheckBox.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME, mObservacaoEditText.getText().toString());

        return mDb.insert(CalculoDeBolusContract.RecordEntry.TABLE_NAME, null, cv);
    }

    private int updateTimeBlock(){
        Log.d("bwvm", "updateTimeBlock: Iniciou");
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME, Utilidades.convertDateTimeToSQLiteFormat(mDataEditText.getText().toString(),  mHoraEditText.getText().toString())  );
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME, mCarboidratoEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME, mGlicemiaEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, mEventoEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME, mInsulinaRapidaEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME, mInsulinaBasalEditText.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME, mDoenteCheckBox.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME, mMedicamentoCheckBox.getText().toString());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME, mObservacaoEditText.getText().toString());

        String where = CalculoDeBolusContract.RecordEntry._ID + "=" + mRecord.getId();

        return mDb.update(CalculoDeBolusContract.RecordEntry.TABLE_NAME, cv, where, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save_delete, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save){
            boolean executed = saveData();

            if (executed){
                Toast.makeText(getApplicationContext(), "Salvo!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public class ClickHandler implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){
                case R.id.ibtn_time_picker_record:
                    callTimePiker();
                    break;
                case R.id.ibtn_date_picker_record:
                    callDatePiker();
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Date date = new Date (year,month,dayOfMonth);
            String formatedDate = String.format("dd/MM/yyyy", date);

            mDataEditText.setText(formatedDate);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeSeted = String.format("%02d:%02d", hourOfDay, minute);

            mHoraEditText.setText(timeSeted);
        }

        private void callTimePiker(){
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMin = calendar.get(Calendar.MINUTE);
            mTimePickerDialog = new TimePickerDialog(RecordDetailActivity.this,this,currentHour,currentMin,true);
            mTimePickerDialog.show();
        }

        private void callDatePiker(){
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePickerDialog = new DatePickerDialog(RecordDetailActivity.this, this, currentYear, currentMonth, currentDay);
            mDatePickerDialog.show();
        }


    }
}
