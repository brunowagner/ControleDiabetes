package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TimeBlockConfigActivity extends AppCompatActivity {
	
	EditText mInicioEditText;
	EditText mRelacaoEditText;
	EditText mSensibilidadeEditText;
	EditText mAlvoEditText;
	
	BolusTimeBlockData mBolusTimeBlockData;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_block_config);
		
		mInicioEditText = (EditText) findViewById(R.id.et_inicio);
		mRelacaoEditText = (EditText) findViewById(R.id.et_relacao);
		mSensibilidadeEditText = (EditText) findViewById(R.id.et_fator_de_sensibilidade);
		mAlvoEditText = (EditText) findViewById(R.id.et_alvo);
		
		//Carrega dados caso seja recebido pela Entity que criou a activity;
		loadData();
		
    }
	
	private void loadData(){
		
		if (mBolusTimeBlockData == null){ return; }
		
		mInicioEditText.setText(mBolusTimeBlockData.start);
		mRelacaoEditText.setText(mBolusTimeBlockData.relation);
		mSensibilidadeEditText.setText(mBolusTimeBlockData.sensibilityFactor);
		mAlvoEditText.setText(mBolusTimeBlockData.tarjet);
		
	}
	
	private void save(){
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
	
	private void addTimeBlock(){
		ContentValues cv = new ContentValues();
		cv.put(TimeBlockEntry.COLUMN_INITIAL_TIME_NAME, mInicioEditText.getText());
		cv.put(TimeBlockEntry.COLUMN_RELATION_NAME, mRelacaoEditText.getText());
		cv.put(TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME, mSensibilidadeEditText.getText());
		cv.put(TimeBlockEntry.COLUMN_TARGET_NAME, mAlvoEditText.getText());
		
		return mDb.insert(TimeBlockEntry.TABLE_NAME, null, cv);
	}
	
	//TODO - criar método chamado updateTimeBlock para modificar o time block atual.
	
	//TODO - criar um método que verifique se o time block já existe afim de evitar duplicidade no banco na hora de salvar.

	//TODO - Criar metodo para criar menu e incluir um item para salvar

	//TODO - Criar metodo para confirmar saída sem salvar caso haja alguma alteração.
	
	
	
}
