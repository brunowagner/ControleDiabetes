package br.com.bwsystemssolutions.controlediabetes;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bwsystemssolutions.controlediabetes.androidFileAndDirectoryPicker.PickerByDialog;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mCalculoDeBolusButton;
    Button mRegistrosButton;
    Button mExportarData;
    Button mCreateBK;
    SQLiteDatabase mDb;
    Context mContextMain = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculoDeBolusButton = (Button) findViewById(R.id.btn_calculo_de_bolus);
        mRegistrosButton = (Button) findViewById(R.id.btn_registros);
        mExportarData = (Button) findViewById(R.id.btn_export_data);
        mCreateBK = (Button) findViewById(R.id.btn_bolus_table);

        mCalculoDeBolusButton.setOnClickListener(this);//new ListenerEvents());
        mRegistrosButton.setOnClickListener(this);//new ListenerEvents());
        mExportarData.setOnClickListener(this);//new ListenerEvents());
        mCreateBK.setOnClickListener(this);


        //mDb = dbHelper.getWritableDatabase();

//        try {
//            //dbHelper.exportDB(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/bkp.db");
//            CalculoDeBolusDBHelper.exportDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Backup Falhou!", Toast.LENGTH_SHORT)
//                    .show();
//        }

        if (hasExternalStorageReadPermission()){
            //continue
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }

        if (hasExternalStorageWritePermission()){
            //continue
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
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
        //TODO - https://code.google.com/archive/p/android-file-dialog/source/default/source (repositorio do google) ()encontrado pelo link abaixo
        //TODO - https://stackoverflow.com/questions/8587325/how-to-select-folder-in-android/8883886 (me levou ao repositorio acima)
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

        if (id == R.id.action_config_item) {
            //Intent intent = new Intent(MainActivity.this, BolusCalculateConfig.class);
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String Fpath = data.getDataString();
//        //TODO handle your request here
//
//
//        Uri uri = data.getData();
//
//        Log.d("bwvm", "onActivityResult: retorno: " + uri);
//        Log.d("bwvm", "onActivityResult: retorno: " + uri.getPath());
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    // Internal class to click handler --------------------------
    //class ListenerEvents extends Activity implements View.OnClickListener {   com a classe interna, o intent não funciona

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.btn_calculo_de_bolus) {
                goToCalculoDeBolus();
            }

            if (id == R.id.btn_registros) {
                goToRegistros();
            }

            if (id == R.id.btn_export_data) {
                //performFileSearch();
                exportDataToCSV();

//                CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(MainActivity.this);
//                try {
//                    dbHelper.importDB(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/bkp.db");
//                    Toast.makeText(getApplicationContext(), "Importação Realizada!", Toast.LENGTH_SHORT)
//                            .show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Importação Falhou!", Toast.LENGTH_SHORT)
//                            .show();
//                }
            }

            if (id == R.id.btn_bolus_table) {
                Context context = this;
                Class destClass = BolusTableActivity.class;
                Intent intent = new Intent(context, destClass);
                startActivity(intent);
            }
        }



        public void exportDataToCSV(){
            //File dbFile=getDatabasePath("yourDBname.sqlite");

            PickerByDialog.OnResponseListener closure = new PickerByDialog.OnResponseListener() {
                @Override
                public void onResponse(boolean canceled, String response) {
                    if (canceled){
                        Toast.makeText(MainActivity.this, "Escolha de pasta cancelada!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    CalculoDeBolusDBHelper dbhelper = new CalculoDeBolusDBHelper(MainActivity.this);
                    String path = response;
                    File exportDir = new File(path, "");
                    if (!exportDir.exists())
                    {
                        exportDir.mkdirs();
                    }

                    Date date = new Date();
                    SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd_HHmm");
                    String dataFormatada = formataData.format(date);
                    File file = new File(exportDir, "Registros_" + dataFormatada + ".csv");
                    try
                    {
                        file.createNewFile();
                        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                        SQLiteDatabase db = dbhelper.getReadableDatabase();
                        Cursor curCSV = db.rawQuery("SELECT * FROM " + CalculoDeBolusContract.RecordEntry.TABLE_NAME,null);
                        String arrColumnNames[] = new String[]{"ID", "Data e Hora", "Evento", "Glicose", "Carboidrato", "Insul. Rápida", "Insul. Basal", "Medicamento", "Doente", "Obs"};
                        csvWrite.writeNext(arrColumnNames);
                        //csvWrite.writeNext(curCSV.getColumnNames());
                        while(curCSV.moveToNext())
                        {
                            //Which column you want to exprort
                            StringBuffer sb = new StringBuffer();
                            String arrStr[] = {
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry._ID)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME)),
                                    curCSV.getString(curCSV.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME))
                            };

                            //String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                            csvWrite.writeNext(arrStr);
                        }
                        csvWrite.close();
                        curCSV.close();
                        Toast.makeText(MainActivity.this,"Exportação realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception sqlEx)
                    {
                        Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                        Toast.makeText(MainActivity.this,"Erro de exportação!", Toast.LENGTH_SHORT).show();
                    }
                }
            };


            String strRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
            PickerByDialog picker = new PickerByDialog(this,strRoot);
            picker.setItemBackgroundColor(getResources().getColor(R.color.colorDefaultBackgroudListView),getResources().getColor(R.color.colorSelectedBackgroundItem));
            picker.setSelectType(PickerByDialog.SELECT_TYPE_FOLDER);
            picker.setOnResponseListener(closure);
            picker.show();
        }

        public void performFileSearch() {
//            String path = getExternalFilesDir(null).getAbsolutePath();
//            String path = getFilesDir().getParent();
//            String path = "//data//data//br.com.bwsystemssolutions.controlediabetes";

            if (hasExternalStorageReadPermission()){
                //continue
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }

            String path = Environment.getExternalStorageDirectory().toString();


            PickerByDialog picker = new PickerByDialog(MainActivity.this, path);
            picker.setItemBackgroundColor(getResources().getColor(R.color.colorDefaultBackgroudListView),getResources().getColor(R.color.colorSelectedBackgroundItem));
            picker.setSelectType(PickerByDialog.SELECT_TYPE_FILE);
            picker.setOnResponseListener(new PickerByDialog.OnResponseListener() {
                @Override
                public void onResponse(boolean canceled, String response) {
                    if (canceled){
                        Log.d("bwvm", "onResponse: Canceled!");
                    } else {
                        Log.d("bwvm", "onResponse: Selected: " + response);

                        try {
                            Log.d("bwvm", "onCreate: Tentou recuperar backup!");
                            // Configurando o banco de dados
                            CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(MainActivity.this);
                            dbHelper.importDB(response);
                            //CalculoDeBolusDBHelper.exportDataBase(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            //        "//ControleDeDiabetes//Backup//BackupDB_" + dataFormatada + ".db");
                            Toast.makeText(MainActivity.this,"Backup Realizado com sucesso.", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Log.d("bwvm", "onCreate: excessão ao criar backup.");
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Falha ao criar o backup.", Toast.LENGTH_LONG).show();

                        }

                    }
                }
            });
            picker.show();
        }

        void goToCalculoDeBolus() {
            Context context = mContextMain;//MainActivity.this;
            Class destinationClass = CalcularBolus.class;
            Intent intent = new Intent(context, destinationClass);

            startActivity(intent);
        }

        void goToRegistros() {
            Context context = mContextMain;//MainActivity.this;
            Class destinationClass = RegistrosActivity.class;
            Intent intent = new Intent(context, destinationClass);

            startActivity(intent);
        }
    //}

    private boolean hasExternalStorageWritePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private boolean hasExternalStorageReadPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }
}
