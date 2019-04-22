package br.com.bwsystemssolutions.controlediabetes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class CalcularBolus extends AppCompatActivity implements View.OnClickListener {

    EditText mGlicemiaEditText;
    EditText mCarboidratosEditText;
    TextView mResultado;
    Button mCalcularButton;
    SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_bolus);

        mGlicemiaEditText = (EditText) findViewById(R.id.et_glicemia);
        mCarboidratosEditText = (EditText) findViewById(R.id.et_carboidratos);
        mResultado = (TextView) findViewById(R.id.tv_resultado);
        mCalcularButton = (Button) findViewById(R.id.btn_calcular);
        mCalcularButton.setOnClickListener(this);

        mGlicemiaEditText.addTextChangedListener(new OnTextEdit());
        mCarboidratosEditText.addTextChangedListener(new OnTextEdit());
    }

    private void calcular(){

        //TODO implementar codigo para pegar os valores do banco.

        if (mGlicemiaEditText.length() == 0 || mCarboidratosEditText.length() == 0){
            return;
        }

        String horaAtual = getHoraAtual();

        //"select * from TABLE where inicio < horaAtual orderBy inicio desc limit = 1"

        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);

        mDb = dbHelper.getReadableDatabase();
        String tableName = CalculoDeBolusContract.TimeBlockEntry.TABLE_NAME;
        String selection = CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME + "<=?";
        String[] selectionArgs = new String[] {horaAtual};
        String orderBy = CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME + " DESC";
        String limit = "1";

        Cursor cursor = mDb.query(tableName,null,selection,
                selectionArgs,null,null,orderBy, limit);

//        Cursor cursor = mDb.query(tableName,null,selection,
//                selectionArgs,null,null,null, null);

        Log.d("bwvm", "calcular: Tamanho do cursor!" + cursor.getCount());
        Log.d("bwvm", "calcular: Printando cursor!" + cursor.toString());

        if (cursor.getCount() > 0 ){
            cursor.moveToFirst();

            Log.d("bwvm", "calcular: Bloco de Hora encontrada " + cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME)));

            int glicemiaAtual = Integer.parseInt(mGlicemiaEditText.getText().toString());
            int carboidratos = Integer.parseInt(mCarboidratosEditText.getText().toString());
            int alvo = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_TARGET_NAME));
            int relacao = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_RELATION_NAME));
            int fatorDeSensibilidade = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME));

            //Double d = (double) (glicemiaAtual - alvo) / (fatorDeSensibilidade);

            Log.d("bwvm", "calcular: ((glicemiaAtual - alvo) / (fatorDeSensibilidade)) + (carboidratos * 1 / relacao)" );
            Log.d("bwvm", "calcular: " + "(( "+ glicemiaAtual + " - " + alvo + " ) / ( " + fatorDeSensibilidade + " )) + ( " + carboidratos + " * 1 / " + relacao + ")");

            final double resultado =  (((double)glicemiaAtual - alvo) / (fatorDeSensibilidade)) + (double)(carboidratos * 1 / relacao);

            Log.d("bwvm", "calcular: Resultado = " + resultado);

            //TODO encontrar o ponte de arredondamento
            //a solução abaixo foi encontrado em https://www.devmedia.com.br/forum/arredondar-numero-0-885650224-para-0-89/564800
            BigDecimal velorExato = new BigDecimal(resultado).setScale(1, RoundingMode.HALF_DOWN);

            mResultado.setText(velorExato + " U");
        }


    //TODO implementar o código gara clcular o Bolus
        /*Fómula:
        *
        * [(glicemia - alvo) / fator de sensibilidade]   +   (carboidratos * relacao)
        *
        * */
        Log.d("bwvm", "calcular: Entrou");
        Log.d("bwvm", "calcular: Saiu");

    }

    private String getHoraAtual(){
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_calcular){
            calcular();
        }

    }


    private class OnTextEdit implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("bwvm", "afterTextChanged: " + s);
            mResultado.setText("");
        }
    }
}
