package br.com.bwsystemssolutions.controlediabetes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private boolean saveData() {
        //TODO codificar metodo para salvar os dados.
        return false;
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
