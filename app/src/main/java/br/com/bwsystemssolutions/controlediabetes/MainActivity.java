package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class MainActivity extends AppCompatActivity{

    Button mCalculoDeBolusButton;
    Button mRegistrosButton;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculoDeBolusButton = (Button) findViewById(R.id.btn_calculo_de_bolus);
        mRegistrosButton = (Button) findViewById(R.id.btn_registros);

        mCalculoDeBolusButton.setOnClickListener(new ListenerEvents());
        mRegistrosButton.setOnClickListener(new ListenerEvents());

        // Configurando o banco de dados
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mDb = dbHelper.getWritableDatabase();


    }

    //TODO criar menu

    class ListenerEvents implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btn_calculo_de_bolus){
                goToCalculoDeBolus();
            }

            if (v.getId() == R.id.btn_registros){
                goToRegistros();
            }
        }

        void goToCalculoDeBolus(){
            Context context = MainActivity.this;
            Class destinationClass = CalcularBolus.class;
            Intent intent = new Intent(context, destinationClass);

            startActivity(intent);
        }

        void goToRegistros(){
            Context context = MainActivity.this;
            Class destinationClass = Registros.class;
            Intent intent = new Intent(context, destinationClass);

            startActivity(intent);
        }
    }
}
