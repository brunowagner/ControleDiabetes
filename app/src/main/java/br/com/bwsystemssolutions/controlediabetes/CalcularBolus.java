package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class CalcularBolus extends AppCompatActivity implements View.OnClickListener {

    EditText mGlicemiaEditText;
    EditText mCarboidratosEditText;
    TextView mResultado;
    TextView mUnitTextView;
    TextView mWarningTextView;
    Button mCalcularButton;
    SQLiteDatabase mDb;

    final double mGraduacao = 0.5;

    boolean enableActionSave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_bolus);

        mGlicemiaEditText = (EditText) findViewById(R.id.et_glicemia);
        mCarboidratosEditText = (EditText) findViewById(R.id.et_carboidratos);
        mResultado = (TextView) findViewById(R.id.tv_resultado);
        mUnitTextView = findViewById(R.id.tv_unit);
        mWarningTextView = findViewById(R.id.tv_warning);
        mCalcularButton = (Button) findViewById(R.id.btn_calcular);
        mCalcularButton.setOnClickListener(this);

        mGlicemiaEditText.addTextChangedListener(new OnTextEdit());
        mCarboidratosEditText.addTextChangedListener(new OnTextEdit());
    }

    private void calcular(){

        hideKeyboard();

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

            int glicemiaAtual = mGlicemiaEditText.length() == 0 ? 0 : Integer.parseInt(mGlicemiaEditText.getText().toString());
            int carboidratos = mCarboidratosEditText.length() == 0 ? 0 : Integer.parseInt(mCarboidratosEditText.getText().toString());
            int alvo = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_TARGET_NAME));
            int relacao = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_RELATION_NAME));
            int fatorDeSensibilidade = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME));

            //Double d = (double) (glicemiaAtual - alvo) / (fatorDeSensibilidade);

            Log.d("bwvm", "calcular: ((glicemiaAtual - alvo) / (fatorDeSensibilidade)) + (carboidratos * 1 / relacao)" );
            Log.d("bwvm", "calcular: " + "(( "+ glicemiaAtual + " - " + alvo + " ) / ( " + fatorDeSensibilidade + " )) + ( " + carboidratos + " * 1 / " + relacao + ")");

            double resultado;
            if (glicemiaAtual == 0) {
                resultado =  (carboidratos * 1.0 / relacao);
                mWarningTextView.setText("Glicemia = 0 -> O valor do resultado é referente apenas para a correção");
            } else {
                resultado =  (((double)glicemiaAtual - alvo) / (fatorDeSensibilidade)) + (carboidratos * 1.0 / relacao);
                mWarningTextView.setText("");
            }

            if (resultado < 0) resultado = 0.0;


            Log.d("bwvm", "calcular: Resultado = " + resultado);

            //TODO encontrar o ponte de arredondamento


            double resultadoAjustado = adjustResult(resultado, mGraduacao);

            //a solução abaixo foi encontrado em https://www.devmedia.com.br/forum/arredondar-numero-0-885650224-para-0-89/564800
            BigDecimal valorExato = new BigDecimal(resultado).setScale(1, RoundingMode.HALF_DOWN);
            BigDecimal ve = BigDecimal.valueOf(resultado);
            BigDecimal round = ve.round(new MathContext(2));
            Log.d("bwvm" , "calcular: resultado: " + round);


            //mResultado.setText(valorExato + "");
            mResultado.setText(resultadoAjustado + "");
            mUnitTextView.setVisibility(View.VISIBLE);
            setEnableActionSave(true);
        } else {

            String message = "Não existe bloco de tempo configurado para realização do cálculo.\n" +
                    "Deseja criar o bloco agora?";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setTitle("Atenção!")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToTimeBlockConfig();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private double adjustResult(double value, double graduation){
        double graduationFactor = 1/graduation;

        return Math.round(value * graduationFactor) / graduationFactor;
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

    private void goToTimeBlockConfig(){
        Intent intent = new Intent(this,TimeBlockConfigActivity.class);
        startActivity(intent);
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
            clearResult();
        }
    }

    private void clearResult(){
        mResultado.setText("");
        mUnitTextView.setVisibility(View.INVISIBLE);
        mWarningTextView.setText("");
        setEnableActionSave(false);
    }

    private void setEnableActionSave(boolean enable){
        enableActionSave = enable;
        invalidateOptionsMenu();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calculate_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save){
            saveCalculateAsRecord();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_save);
        item.setEnabled(enableActionSave);
        return true;
    }

    private void saveCalculateAsRecord(){

        Intent intent = new Intent(this,RecordDetailActivity.class);

        Record record = new Record();
        record.setDate(new Date());
        record.setCarbohydrate(Integer.parseInt(mCarboidratosEditText.getText().toString()));
        record.setGlucose(Integer.parseInt(mGlicemiaEditText.getText().toString()));
        record.setFastInsulin(Double.parseDouble(mResultado.getText().toString()));

        //Empacotando o objeto
        Bundle bundle = new Bundle();
        bundle.putSerializable(Record.BUNDLE_STRING_KEY, record);

        //passando o objeto na Intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

}
