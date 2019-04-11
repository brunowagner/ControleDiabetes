package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        mGlicemiaEditText.setOnKeyListener(new onEditorListener());
        mCarboidratosEditText.setOnKeyListener(new onEditorListener());
    }

    private void calcular(){

        //TODO implementar codigo para pegar os valores do banco.

    //TODO implementar o código gara clcular o Bolus
        /*Fómula:
        *
        * [(glicemia - alvo) / fator de sensibilidade]   +   (carboidratos * relacao)
        *
        * */

    }



    class onEditorListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            calcular();
            return true;
        }
    }
}
