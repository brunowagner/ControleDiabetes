package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract.TimeBlockEntry;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;


public class TimeBlockConfigActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    EditText mInicioEditText;
    EditText mRelacaoEditText;
    EditText mSensibilidadeEditText;
    EditText mAlvoEditText;
    ImageButton mTimePickerImageButton;
    TimePickerDialog mTimePickerDialog;

    BolusTimeBlockData mBolusTimeBlockData;
    SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_block_config);

        mInicioEditText = (EditText) findViewById(R.id.et_inicio);
        mRelacaoEditText = (EditText) findViewById(R.id.et_relacao);
        mSensibilidadeEditText = (EditText) findViewById(R.id.et_fator_de_sensibilidade);
        mAlvoEditText = (EditText) findViewById(R.id.et_alvo);
        mTimePickerImageButton = (ImageButton) findViewById(R.id.btn_time_picker);
        mTimePickerImageButton.setOnClickListener(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(BolusTimeBlockData.BUNDLE_STRING_KEY)){
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            mBolusTimeBlockData = (BolusTimeBlockData) bundle.getSerializable(BolusTimeBlockData.BUNDLE_STRING_KEY);
        }

        //Carrega dados caso seja recebido pela Entity que criou a activity;
        loadData();

        configureDb();
    }

    private void configureDb(){
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void loadData(){

        if (mBolusTimeBlockData == null){ return; }

        mInicioEditText.setText(mBolusTimeBlockData.start);
        mRelacaoEditText.setText(String.valueOf(mBolusTimeBlockData.relation));
        mSensibilidadeEditText.setText(String.valueOf(mBolusTimeBlockData.sensibilityFactor));
        mAlvoEditText.setText(String.valueOf(mBolusTimeBlockData.tarjet));

    }

    private boolean saveData(){
        if (!validateData()) { return false;}
        
        boolean executed = false;

        if (mBolusTimeBlockData == null){

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
        if (mInicioEditText.getText().length() == 0 || mRelacaoEditText.getText().length() == 0 ||
                mSensibilidadeEditText.getText().length() == 0 || mAlvoEditText.getText().length() == 0) {

            message = "Todos os campos dever ser preenchidos.";

            //se o campo inicio foi preenchido e já existir o bloco de horas
        } else if (mInicioEditText.getText().toString().length() > 0 && existsRegister(mInicioEditText.getText().toString())){
                message = "Hora de início já existe.\nO bloco não pôde ser salvo.";
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

    private long addTimeBlock(){
        ContentValues cv = new ContentValues();
        cv.put(TimeBlockEntry.COLUMN_INITIAL_TIME_NAME, mInicioEditText.getText().toString());
        cv.put(TimeBlockEntry.COLUMN_RELATION_NAME, mRelacaoEditText.getText().toString());
        cv.put(TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME, mSensibilidadeEditText.getText().toString());
        cv.put(TimeBlockEntry.COLUMN_TARGET_NAME, mAlvoEditText.getText().toString());

        return mDb.insert(TimeBlockEntry.TABLE_NAME, null, cv);
    }

    private int updateTimeBlock(){
        Log.d("bwvm", "updateTimeBlock: Iniciou");

        ContentValues cv = new ContentValues();
        cv.put(TimeBlockEntry.COLUMN_INITIAL_TIME_NAME, mInicioEditText.getText().toString());
        cv.put(TimeBlockEntry.COLUMN_RELATION_NAME, mRelacaoEditText.getText().toString());
        cv.put(TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME, mSensibilidadeEditText.getText().toString());
        cv.put(TimeBlockEntry.COLUMN_TARGET_NAME, mAlvoEditText.getText().toString());

        Log.d("bwvm", "updateTimeBlock: TimeBlockEntry._ID: " + TimeBlockEntry._ID);
        Log.d("bwvm", "updateTimeBlock: id: " + mBolusTimeBlockData.id);

        String where = TimeBlockEntry._ID + "=" + mBolusTimeBlockData.id;

        return mDb.update(TimeBlockEntry.TABLE_NAME, cv, where, null);
    }


    private boolean existsRegister(String time){

        String selection = TimeBlockEntry.COLUMN_INITIAL_TIME_NAME + "=?";
        String[] args = new String[] { time };


        Cursor cursor = mDb.query(TimeBlockEntry.TABLE_NAME, new String[]{TimeBlockEntry.COLUMN_INITIAL_TIME_NAME}, selection, args, null, null, null);

        Log.d("bwvm", "existsRegister: Tamanho do cursor: " + cursor.getCount());
        return cursor.getCount() > 0 ? true : false;

    }

    //TODO - Criar metodo para confirmar saída sem salvar caso haja alguma alteração.

    private void callTimePiker(){
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        mTimePickerDialog = new TimePickerDialog(this,this,currentHour,currentMin,true);
        mTimePickerDialog.show();
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeSeted = String.format("%02d:%02d", hourOfDay, minute);



        mInicioEditText.setText(timeSeted);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_time_picker) {
            callTimePiker();
        }
    }
}
