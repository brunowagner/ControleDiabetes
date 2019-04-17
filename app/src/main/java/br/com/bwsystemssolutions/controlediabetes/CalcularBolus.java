package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalcularBolus extends AppCompatActivity {

    EditText mGlicemiaEditText;
    EditText mCarboidratosEditText;
    TextView mResultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_bolus);

        mGlicemiaEditText = (EditText) findViewById(R.id.et_glicemia);
        mCarboidratosEditText = (EditText) findViewById(R.id.et_carboidratos);
        mResultado = (TextView) findViewById(R.id.tv_resultado);

        mGlicemiaEditText.addTextChangedListener(new OnTextEdit());
        mCarboidratosEditText.addTextChangedListener(new OnTextEdit());
    }

    private void calcular(){

        //TODO implementar codigo para pegar os valores do banco.

    //TODO implementar o código gara clcular o Bolus
        /*Fómula:
        *
        * [(glicemia - alvo) / fator de sensibilidade]   +   (carboidratos * relacao)
        *
        * */
        Log.d("bwvm", "calcular: Entrou");
        Log.d("bwvm", "calcular: Saiu");

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
            calcular();
        }
    }
}
