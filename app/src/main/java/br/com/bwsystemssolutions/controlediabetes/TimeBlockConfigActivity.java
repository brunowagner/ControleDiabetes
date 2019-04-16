package br.com.bwsystemssolutions.controlediabetes;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.Calendar;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
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
        mRelacaoEditText.setText(mBolusTimeBlockData.relation);
        mSensibilidadeEditText.setText(mBolusTimeBlockData.sensibilityFactor);
        mAlvoEditText.setText(mBolusTimeBlockData.tarjet);

    }

    private void saveData(){
        if (!validateData()) { return;}

        if (mBolusTimeBlockData == null){
            addTimeBlock();
        } else {
            updateTimeBlock();
        }

    }

    private boolean validateData(){
        boolean validate = false;

        if (mInicioEditText.getText().length() == 0 ||
                mRelacaoEditText.getText().length() == 0 ||
                mSensibilidadeEditText.getText().length() == 0 ||
                mAlvoEditText.getText().length() == 0) {
            //TODO - exibir mensagem informado que todos os campos precisam ser preenchidos
        } else {
            validate = true;
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

    private void updateTimeBlock(){
        //TODO - codificar o método para modificar o time block atual no banco.
    }


    //TODO - criar um método que verifique se o time block já existe afim de evitar duplicidade no banco na hora de salvar.

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
            saveData();
            return true;
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
