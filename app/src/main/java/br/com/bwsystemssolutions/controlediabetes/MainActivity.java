package br.com.bwsystemssolutions.controlediabetes;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.data.directoryOrFileChoose.FileDialog;

public class MainActivity extends AppCompatActivity{

    Button mCalculoDeBolusButton;
    Button mRegistrosButton;
    Button mImportarDB;
    SQLiteDatabase mDb;
    FileDialog mFileDialog;

    public final static int PICKFILE_REQUEST_CODE = 1111;

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
        //mDb = dbHelper.getWritableDatabase();

//        try {
//            //dbHelper.exportDB(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + "/bkp.db");
//            CalculoDeBolusDBHelper.exportDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Backup Falhou!", Toast.LENGTH_SHORT)
//                    .show();
//        }

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

        testar_filedialog();


    }

    private void testar_filedialog(){
        Log.d("bwvm", "onCreate: Main: Environment.getExternalStorageDirectory().getAbsolutePath() = " + Environment.getExternalStorageDirectory());
        Log.d("bwvm", "onCreate: Main: Environment.getExternalStorageDirectory().getAbsolutePath() =  " + Environment.getExternalStorageDirectory().getAbsolutePath());

        // Bloco colocado para poder verificar a permissáo de leitura e escrita na memoria externa.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Log.d("bwvm", "onCreate: não tem permissao para ler no Storage");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        10001);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Log.d("bwvm", "onCreate: não tem permissao para escrever no Storage");
        }

        //Codigo conforme exemplo da fonte
        File mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android");
        //File mPath = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath());
        //File mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//BW");
        mFileDialog = new FileDialog(this, mPath );
        mFileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                Log.d("bwvm", "selected file " + file.toString());
            }
        });

        mFileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
            @Override
            public void directorySelected(File directory) {
                Log.d("bwvm", "selected dir " + directory.toString());
            }
        });

        mFileDialog.setSelectDirectoryOption(true);

        mFileDialog.showDialog();
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Fpath = data.getDataString();
        //TODO handle your request here


        Uri uri = data.getData();

        Log.d("bwvm", "onActivityResult: retorno: " + uri);
        Log.d("bwvm", "onActivityResult: retorno: " + uri.getPath());

        super.onActivityResult(requestCode, resultCode, data);
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
                performFileSearch();

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
        }

        public void performFileSearch() {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            startActivityForResult(intent,PICKFILE_REQUEST_CODE);

//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("file/*");
//            startActivityForResult(intent, PICKFILE_REQUEST_CODE);

            //Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            //i.addCategory(Intent.CATEGORY_DEFAULT);
            //startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);

//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("*/*");
//            startActivityForResult(intent, 8888);
            mFileDialog.showDialog();
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
