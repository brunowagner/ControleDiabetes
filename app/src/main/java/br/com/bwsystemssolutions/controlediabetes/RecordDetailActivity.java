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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.bwsystemssolutions.controlediabetes.classe.Event;
import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.classe.Utilidades;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusDAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.EventDAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.MealDAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.RecordDAO;
import br.com.bwsystemssolutions.controlediabetes.util.Converter;

public class RecordDetailActivity extends AppCompatActivity {

    public  EditText mDataEditText;
    public  EditText mHoraEditText;
    public  EditText mGlicemiaEditText;
    //public  EditText mEventoEditText;
    public  Spinner mEventSpinner;
    public  Spinner mMealSpinner;
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
    //SQLiteDatabase mDb;

    boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        mDataEditText = (EditText) findViewById(R.id.et_data_record);
        mTimePickerImageButton = (ImageButton) findViewById(R.id.ibtn_time_picker_record);
        mHoraEditText = (EditText) findViewById(R.id.et_hora_record);
        mDatePickerImageButton = (ImageButton) findViewById(R.id.ibtn_date_picker_record);
        mGlicemiaEditText = (EditText) findViewById(R.id.et_glicemia_record);
        //mEventoEditText = (EditText) findViewById(R.id.et_evento_record);
        mEventSpinner = findViewById(R.id.sp_evento);
        mMealSpinner = findViewById(R.id.sp_refeicao);
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

//        configureDb();

        loadData();
    }

//    private void configureDb(){
//        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
//        mDb = dbHelper.getWritableDatabase();
//    }

    private void loadData(){

        loadEventsSpinner();
        loadMealsSpinner();

        if (mRecord == null){
            mDataEditText.setText(Utilidades.convertDateToString(new Date(), Utilidades.DEFAULT_DATE_FORMAT));
            mHoraEditText.setText(Utilidades.convertTimeToString(new Date(), Utilidades.DEFAULT_TIME_FORMAT));
            return;
        }

        mDataEditText.setText(Utilidades.convertDateToString(mRecord.getDate(), Utilidades.DEFAULT_DATE_FORMAT));
        mHoraEditText.setText(Utilidades.convertTimeToString(mRecord.getDate(), Utilidades.DEFAULT_TIME_FORMAT));
        mGlicemiaEditText.setText(String.valueOf(mRecord.getGlucose()));
        //mEventoEditText.setText(mRecord.getEvent());
        // Caso o evento náo exista mais (deletado da Base), utiliza o evento 'Outro';
        int positionEventSpinner = getEventSpinnerPositionByString(mEventSpinner, mRecord.getEvent());
        if (positionEventSpinner == -1) {
            positionEventSpinner = getEventSpinnerPositionByString(mEventSpinner, "Outro");
        }
        mEventSpinner.setSelection(positionEventSpinner);
        int positionMealSpinner = getMealSpinnerPositionByString(mMealSpinner, mRecord.getMeal());
        if (positionMealSpinner == -1) {
            positionMealSpinner = getMealSpinnerPositionByString(mMealSpinner, "");
        }

        mMealSpinner.setSelection(positionMealSpinner);
        mCarboidratoEditText.setText(String.valueOf(mRecord.getCarbohydrate()));
        mInsulinaRapidaEditText.setText(String.valueOf(mRecord.getFastInsulin()));
        mInsulinaBasalEditText.setText(String.valueOf(mRecord.getBasalInsulin()));
        mDoenteCheckBox.setChecked(mRecord.isSick());
        mMedicamentoCheckBox.setChecked(mRecord.isMedicament());
        mObservacaoEditText.setText(mRecord.getNote());
    }

//    private Cursor fetchAllMeals(){
//        MealDAO mealDAO = new MealDAO(this);
//        mealDAO.fetchAll();
//
//        return mDb.query(CalculoDeBolusContract.MealEntry.TABLE_NAME,
//                null, null, null, null,null, CalculoDeBolusContract.MealEntry.COLUMN_SORT_NAME);
//    }

    private void loadEventsSpinner(){
        EventDAO eventDAO = new EventDAO(this);
        Event eventEmpty = new Event();

        //deixa o primeiro item vazio, caso o registro nao seja sobre um evento.
        eventEmpty.setId(0);
        eventEmpty.setText("");

        List<Event> events = eventDAO.fetchAll();

        events.add(0,eventEmpty);

        ArrayAdapter<Event> eventsArrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,events);
        mEventSpinner.setAdapter(eventsArrayAdapter);
    }


//    private void loadMealsSpinnerOld(){
//        Cursor cursor = fetchAllMeals();
//
//        List<String> mealsList = new ArrayList<>();
//        //deixa o primeiro item vazio, caso o registro nao seja sobre uma refeicao.
//        mealsList.add(0,"");
//
//        if (cursor.getCount() <= 0){
//            //Do nothing
//        } else {
//            if (cursor.moveToFirst()){
//                do {
//                    mealsList.add(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.MealEntry.COLUMN_MEAL_NAME)));
//                }while(cursor.moveToNext());
//            }
//        }
//        cursor.close();
//
//        ArrayAdapter<String> mealsArrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,mealsList);
//        mMealSpinner.setAdapter(mealsArrayAdapter);
//    }

    private void loadMealsSpinner(){
        MealDAO mealDAO = new MealDAO(this);
        Meal mealEmpty = new Meal();

        //deixa o primeiro item vazio, caso o registro nao seja sobre uma refeicao.
        mealEmpty.setId(0);
        mealEmpty.setMeal("");

        List<Meal> meals = mealDAO.fetchAll();

        meals.add(0,mealEmpty);

        ArrayAdapter<Meal> mealsArrayAdapter = new ArrayAdapter<Meal>(this,R.layout.support_simple_spinner_dropdown_item,meals);
        mMealSpinner.setAdapter(mealsArrayAdapter);
    }

    /**
     * Method to shearch position of text in spinner.
     * @param spinner The spinner widget.
     * @param text The text that quant loock for.
     * @return int If have not the text, returns -1, else returns the position number.
     */
    private int getSpinnerPositionByString(Spinner spinner, String text){

        ArrayAdapter spinnerAdapter = (ArrayAdapter) spinner.getAdapter();

        try {
            int i = spinnerAdapter.getPosition(text);
            Log.d("bwvm", "getSpinnerPositionByString: texto procurado: " + text);
            Log.d("bwvm", "getSpinnerPositionByString: valor do position: " + i);
            return i;
        } catch (Exception e){
            Log.d("bwvm", "getSpinnerPositionByString: não achou position");
            return -1;

        }
    }

    private int getEventSpinnerPositionByString(Spinner spinner, String text){

        ArrayAdapter spinnerAdapter = (ArrayAdapter) spinner.getAdapter();

        for (int i=0; i<spinner.getCount();i++){
            Event event = (Event) spinnerAdapter.getItem(i);
            if (event.getText().equals(text)){
                Log.d("bwvm", "getEventSpinnerPositionByString: texto procurado: " + text);
                Log.d("bwvm", "getEventSpinnerPositionByString: valor do position: " + i);
                return i;
            }
        }
        Log.d("bwvm", "getEventSpinnerPositionByString: não achou position");
        return -1;
    }

    private int getMealSpinnerPositionByString(Spinner spinner, String text){

        ArrayAdapter spinnerAdapter = (ArrayAdapter) spinner.getAdapter();

        for (int i=0; i<spinner.getCount();i++){
            Meal meal = (Meal) spinnerAdapter.getItem(i);
            if (meal.getMeal().equals(text)){
                Log.d("bwvm", "getMealSpinnerPositionByString: texto procurado: " + text);
                Log.d("bwvm", "getMealSpinnerPositionByString: valor do position: " + i);
                return i;
            }
        }
        Log.d("bwvm", "getMealSpinnerPositionByString: não achou position");
        return -1;
    }

    private boolean saveData(){
        isSaved = false;
        boolean executed = false;

        if (mRecord == null || mRecord.getId() == 0){
            if (!validateFields(true)) { return false;}
            Log.d("SAVE", "saveData: Entrou no addRecord()");
            addRecord();

        } else {
            if (!validateFields(false)) { return false;}

            updateRecord();
        }

        if (isSaved){ executed = true; }

        return executed;
    }

    private boolean validateFields(boolean isToSave){
        boolean validate = false;
        String message = "";

        // se data ou hora não for preenchido
        if (mDataEditText.getText().length() == 0 || mHoraEditText.getText().length() == 0 ) {
                //mEventoEditText.getText().length() == 0) {


            message = "A data ou a hora não foi preenchido!";

        } else if (mCarboidratoEditText.getText().length() == 0 && mGlicemiaEditText.getText().length() == 0 &&
                mInsulinaBasalEditText.getText().length() == 0 && mInsulinaRapidaEditText.getText().length() == 0) {

            message = "Preencha pelo menos um dos campos abaixo:\n" +
                    "- Carbohidrato\n" +
                    "- Glicemia\n" +
                    "- Insulina Rápida\n" +
                    "- Insulina Basal";
        } else if (isToSave && mDataEditText.getText().toString().length() > 0 && mDataEditText.getText().toString().length() > 0 && existsRecord(mDataEditText.getText().toString(), mHoraEditText.getText().toString()   )) {
            message = "Data e hora ja' existem.\nO registro não pôde ser salvo.";

        // se a refeição escolhida já existir para a data em questão.
        } else if (existsRecordWithMealatDate(mDataEditText.getText().toString(), mMealSpinner.getSelectedItem().toString())){

            message = " A refeição " + mMealSpinner.getSelectedItem().toString() + " foi já existe para esta data.\n" +
                    "Escolha outra refeição ou altere a refeição desejada.";

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

    private boolean existsRecord(String date, String time){
        RecordDAO recordDAO = new RecordDAO(this);
        return recordDAO.exists(date,time);
    }

    private boolean existsRecordWithMealatDate(String date,String meal){
        RecordDAO recordDAO = new RecordDAO(this);
        return recordDAO.existsByDateAndMeal(date,meal);
    }

    private void addRecord(){
        RecordDAO recordDAO = new RecordDAO(this);
        mRecord = new Record();
        fillObjectWithActivityData(mRecord);
        boolean saved = recordDAO.add(mRecord);
        if (saved) {
            isSaved = true;
            Toast.makeText(getApplicationContext(), "Salvo!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            isSaved = false;
            Toast.makeText(getApplicationContext(), "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecord(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente alterar o registro?")
                .setTitle("Atenção!")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("bwvm", "updateRecord: Iniciou");
                        RecordDAO recordDAO = new RecordDAO(builder.getContext());
                        fillObjectWithActivityData(mRecord);
                        Log.d("bwvm", "onClick: fara o update!");
                        boolean saveds = recordDAO.update(mRecord);
                        if (saveds) {
                            isSaved = true;
                            Toast.makeText(getApplicationContext(), "Salvo!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            isSaved = false;
                            Toast.makeText(getApplicationContext(), "Não foi possível salvar!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void fillObjectWithActivityData(Record record){
        record.setDateFromStringDateSqlite(Utilidades.convertDateTimeToSQLiteFormat(mDataEditText.getText().toString(),  mHoraEditText.getText().toString()));
        record.setCarbohydrate(Converter.toInt(mCarboidratoEditText.getText().toString()));
        record.setGlucose(Converter.toInt(mGlicemiaEditText.getText().toString()));
        record.setEvent(mEventSpinner.getSelectedItem().toString());
        record.setMeal(mMealSpinner.getSelectedItem().toString());
        record.setFastInsulin(Converter.toDouble(mInsulinaRapidaEditText.getText().toString()));
        record.setBasalInsulin(Converter.toDouble(mInsulinaBasalEditText.getText().toString()));
        record.setSick(mDoenteCheckBox.isChecked());
        record.setMedicament(mMedicamentoCheckBox.isChecked());
        record.setNote(mObservacaoEditText.getText().toString());
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
            saveData();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
            int monthNumber = month + 1;
            String sDate = String.format("%02d/%02d/%04d", dayOfMonth, monthNumber, year);

//            Log.d("bwvm", "onDateSet: date" + date );
//
            Log.d("bwvm", "onDateSet: dd  " + dayOfMonth);
            Log.d("bwvm", "onDateSet: MM  " + month);
            Log.d("bwvm", "onDateSet: yy  " + year);
//            SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
//
//            String formatedDate = sdff.format(date);

            mDataEditText.setText(sDate);
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
