package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class MainActivity extends AppCompatActivity{

    Button mCalculoDeBolusButton;
    Button mRegistrosButton;
    Button mImportarDB;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculoDeBolusButton = (Button) findViewById(R.id.btn_calculo_de_bolus);
        mRegistrosButton = (Button) findViewById(R.id.btn_registros);
        mImportarDB = (Button) findViewById(R.id.btn_import_db);

        mCalculoDeBolusButton.setOnClickListener(new ListenerEvents());
        mRegistrosButton.setOnClickListener(new ListenerEvents());
        mImportarDB.setOnClickListener(new ListenerEvents());

        // Configurando o banco de dados
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        try {
            dbHelper.exportDB(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/bkp.db");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Backup Falhou!", Toast.LENGTH_SHORT)
                    .show();
        }

        Log.d("bwvm", "onCreate: getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() = "
                + getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath());

        Log.d("bwvm", "onCreate: Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() = "
                + Environment.getDataDirectory().getAbsolutePath());

        Log.d("bwvm", "onCreate: getFilesDir() = " + getFilesDir());

        //TODO - Links para implementar backup
        //TODO - https://respostas.guj.com.br/38618-criar-e-restaurar-backup-no-sqlite-como-fazer-
        //TODO - https://pt.stackoverflow.com/questions/38385/backup-em-banco-de-dados-sqlite
        //TODO - https://stackoverflow.com/questions/18322401/is-it-possible-backup-and-restore-a-database-file-in-android-non-root-devices/18322762#18322762
        //TODO - https://stackoverflow.com/questions/2170031/backup-and-restore-sqlite-database-to-sdcard
        //TODO - https://stackoverflow.com/questions/6540906/simple-export-and-import-of-a-sqlite-database-on-android (usei este)
        //TODO - https://gist.github.com/granoeste/5574148 (explica as pastas)





    }

    // Menu code block -------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_config_item){
            //Intent intent = new Intent(MainActivity.this, BolusCalculateConfig.class);
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Internal class to click handler --------------------------
    class ListenerEvents implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.btn_calculo_de_bolus){
                goToCalculoDeBolus();
            }

            if (id == R.id.btn_registros){
                goToRegistros();
            }

            if (id == R.id.btn_import_db){
                CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(MainActivity.this);
                try {
                    dbHelper.importDB(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/bkp.db");
                    Toast.makeText(getApplicationContext(), "Importação Realizada!", Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Importação Falhou!", Toast.LENGTH_SHORT)
                            .show();
                }
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
            Class destinationClass = RegistrosActivity.class;
            Intent intent = new Intent(context, destinationClass);

            startActivity(intent);
        }
    }
}
