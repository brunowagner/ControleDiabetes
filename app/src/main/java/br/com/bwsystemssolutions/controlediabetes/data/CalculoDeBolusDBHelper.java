package br.com.bwsystemssolutions.controlediabetes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.util.FileUtils;

import static br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract.*;


public class CalculoDeBolusDBHelper extends SQLiteOpenHelper {
    //TODO mudar o nome do arquivo do banco se o app mudar de nome.
    /**
     * O nome do banco será o nome do arquivo local no dispositivo que armazenará todos os dados.
     */
    private static final String DATABASE_NAME = "calculoDeBolus.db";

    // fonte https://stackoverflow.com/questions/6540906/simple-export-and-import-of-a-sqlite-database-on-android
    public static String DB_FILEPATH = "//data//data//br.com.bwsystemssolutions.controlediabetes//databases//" + DATABASE_NAME;

    //Representa a versao do banco de dados atual
    private static final int DATABASE_VERSION=3;

    private Context mContext;

    public CalculoDeBolusDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("bwvm", "CalculoDeBolusDBHelper: Data Base Name = " + getDatabaseName());
        Log.d("bwvm", "CalculoDeBolusDBHelper: Environment.getDataDirectory() = " + Environment.getDataDirectory());
        Log.d("bwvm", "CalculoDeBolusDBHelper: Environment.getExternalStorageDirectory() = " + Environment.getExternalStorageDirectory());

        this.mContext = context;
    }

    /**
     * Retorno de chamada que cria o arquivo de banco de dados
     * @param db SQLiteDataBase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TIME_BLOCK_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TimeBlockEntry.TABLE_NAME + "(" +
                TimeBlockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TimeBlockEntry.COLUMN_INITIAL_TIME_NAME + " STRING NOT NULL," +
                TimeBlockEntry.COLUMN_RELATION_NAME + " INTEGER NOT NULL," +
                TimeBlockEntry.COLUMN_TARGET_NAME + " INTEGER NOT NULL," +
                TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME + " INTEGER NOT NULL" +
                ");";
        
        final String SQL_CREATE_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                RecordEntry.TABLE_NAME + "(" +
                RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecordEntry.COLUMN_DATE_TIME_NAME + " TEXT NOT NULL," +
                RecordEntry.COLUMN_GLUCOSE_NAME + " INTEGER," +
                RecordEntry.COLUMN_MEAL_NAME + " TEXT NOT NULL," +
                RecordEntry.COLUMN_MEAL_TIME_NAME + " TEXT NOT NULL," +
                RecordEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL," +
                RecordEntry.COLUMN_CARBOHYDRATE_NAME + " INTEGER," +
                RecordEntry.COLUMN_FAST_INSULIN_NAME + " REAL," +
                RecordEntry.COLUMN_BASAL_INSULIN_NAME + " REAL," +
                RecordEntry.COLUMN_SICK_NAME + " INTEGER," +
                RecordEntry.COLUMN_MEDICAMENT_NAME + " INTEGER," +
                RecordEntry.COLUMN_NOTE_NAME + " TEXT" +
                ");";
        
        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                EventEntry.TABLE_NAME + "(" +
                EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL," +
                EventEntry.COLUMN_SORT_NAME + " INTEGER NOT NULL," +
                EventEntry.COLUMN_SOURCE_NAME + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_MEALS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                MealEntry.TABLE_NAME + "(" +
                MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MealEntry.COLUMN_MEAL_NAME + " TEXT NOT NULL UNIQUE," +
                MealEntry.COLUMN_SORT_NAME + " INTEGER NOT NULL UNIQUE," +
                MealEntry.COLUMN_SOURCE_NAME + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_GLUCOSES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                GlucoseEntry.TABLE_NAME + "(" +
                GlucoseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GlucoseEntry.COLUMN_GLUCOSE_NAME + " INTEGER NOT NULL UNIQUE" +
                ");";

        final String SQL_CREATE_BOLUS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                BolusEntry.TABLE_NAME + "(" +
                BolusEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BolusEntry.COLUMN_GLUCOSE_ID_NAME + " INTEGER NOT NULL," +
                BolusEntry.COLUMN_GLUCOSE_NAME + " INTEGER NOT NULL," +
                BolusEntry.COLUMN_MEAL_ID_NAME + " INTEGER NOT NULL," +
                BolusEntry.COLUMN_INSULIN_NAME + " REAL NOT NULL" +
                ");";

        final String SQL_CREATE_BOLUS_TABLE_2 = "CREATE TABLE IF NOT EXISTS " +
                BolusTable2Entry.TABLE_NAME + "(" +
                BolusTable2Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BolusTable2Entry.COLUMN_BREAKFAST_NAME + " REAL NOT NULL," +
                BolusTable2Entry.COLUMN_BRUNCH_NAME + " REAL NOT NULL," +
                BolusTable2Entry.COLUMN_LUNCH_NAME + " REAL NOT NULL," +
                BolusTable2Entry.COLUMN_TEA_NAME + " REAL NOT NULL," +
                BolusTable2Entry.COLUMN_DINNER_NAME + " REAL NOT NULL," +
                BolusTable2Entry.COLUMN_SUPPER_NAME + " REAL NOT NULL," +
                BolusTable2Entry.COLUMN_DAWN_NAME + " REAL NOT NULL" +
                ");";

        final String SQL_POPULATE_EVENT_TABLE = getPopulateEventTableString();
        final String SQL_POPULATE_MEAL_TABLE = getPopulateMealTableString();


        db.execSQL(SQL_CREATE_TIME_BLOCK_TABLE);
        db.execSQL(SQL_CREATE_RECORDS_TABLE);
        db.execSQL(SQL_CREATE_EVENTS_TABLE);
        db.execSQL(SQL_CREATE_MEALS_TABLE);
        db.execSQL(SQL_POPULATE_EVENT_TABLE);
        db.execSQL(SQL_POPULATE_MEAL_TABLE);
        db.execSQL(SQL_CREATE_GLUCOSES_TABLE);
        db.execSQL(SQL_CREATE_BOLUS_TABLE);
        db.execSQL(SQL_CREATE_BOLUS_TABLE_2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TimeBlockEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME);
          db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
//        onCreate(db);

        String updateVersion2a = "ALTER TABLE " + EventEntry.TABLE_NAME + " ADD COLUMN " + EventEntry.COLUMN_SOURCE_NAME + " TEXT NOT NULL DEFAULT ''";
        String updateVersion2b = getPopulateEventTableString();
        String updateVersion3a = "ALTER TABLE " + RecordEntry.TABLE_NAME + " ADD COLUMN " + RecordEntry.COLUMN_MEAL_NAME + " TEXT NOT NULL DEFAULT ''";
        String updateVersion3b = "ALTER TABLE " + RecordEntry.TABLE_NAME + " ADD COLUMN " + RecordEntry.COLUMN_MEAL_TIME_NAME + " TEXT NOT NULL DEFAULT ''";
        String updateVersion3c = "ALTER TABLE " + EventEntry.TABLE_NAME + " ADD COLUMN " + EventEntry.COLUMN_SORT_NAME + " INTEGER NOT NULL DEFAULT 0";
        String updateVersion3d = "CREATE TABLE " +
                MealEntry.TABLE_NAME + "(" +
                MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MealEntry.COLUMN_MEAL_NAME + " TEXT NOT NULL," +
                MealEntry.COLUMN_SORT_NAME + " INTEGER NOT NULL," +
                MealEntry.COLUMN_SOURCE_NAME + " TEXT NOT NULL" +
                ");";
        String updateVersion3e = getPopulateMealTableString();

        String updateVersion3f = "ALTER TABLE " + BolusEntry.TABLE_NAME + " ADD COLUMN " + BolusEntry.COLUMN_GLUCOSE_NAME + " INTEGER NOT NULL DEFAULT 0";

        switch (oldVersion){
            case 1:
                db.execSQL(updateVersion2a);
                db.execSQL(updateVersion2b);
                break;
            default:
                db.execSQL(updateVersion2a);
                db.execSQL(updateVersion2b);
                db.execSQL(updateVersion3a);
                db.execSQL(updateVersion3b);
                db.execSQL(updateVersion3c);
                db.execSQL(updateVersion3d);
                db.execSQL(updateVersion3e);
                db.execSQL(updateVersion3f);
        }
    }

    private String getPopulateEventTableString(){
            String array[] = mContext.getResources().getStringArray(R.array.event_names_array);

            StringBuffer sb = new StringBuffer("INSERT INTO " +
                    EventEntry.TABLE_NAME + " (" +
                    EventEntry.COLUMN_EVENT_NAME + "," + EventEntry.COLUMN_SORT_NAME + "," + EventEntry.COLUMN_SOURCE_NAME + ") VALUES ");

            for (int i = 0; i < array.length; i++){
                sb.append("('"  + array[i] + "','" + i + "','app')");
                if (i < array.length -1 ) sb.append(",");
            }
            sb.append(";");

            return sb.toString();
    }

    private String getPopulateMealTableString(){
        String array[] = mContext.getResources().getStringArray(R.array.meal_names_array);

        StringBuffer sb = new StringBuffer("INSERT INTO " +
                MealEntry.TABLE_NAME + " (" +
                MealEntry.COLUMN_MEAL_NAME + "," + MealEntry.COLUMN_SORT_NAME + "," + MealEntry.COLUMN_SOURCE_NAME + ") VALUES ");

        for (int i = 0; i < array.length; i++){
            sb.append("('"  + array[i] + "','" + i + "','app')");
            if (i < array.length -1 ) sb.append(",");
        }
        sb.append(";");

        return sb.toString();
    }



    /* --------------------------------------------------------------------------------------------
    *                       Histório de mudança de versão
    *  --------------------------------------------------------------------------------------------
    *
    *   - Versão 2:
    *        - Foi adicionada a coluna 'EventEntry.COLUMN_SOURCE_NAME' na tabela 'Events'
    *        - Foi implementado o SQL_POPULATE_EVENT_TABLE para popular com valores padrões a tabela Events
    *
    *   - Versão 3:
    *       - Foi adicionada a coluna 'RecordEntry.COLUMN_MEAL_NAME' na tabela 'Records'
    *       - Foi adicionada a coluna 'RecordEntry.COLUMN_MEAL_TIME_NAME' na tabela 'Events'
    *       - Foi adicionada a coluna 'EventEntry.COLUMN_SORT_NAME' na tabela 'Events'
    *       - Foi adicionada a coluna  'BolusEntry.COLUMN_GLUCOSE_NAME' na tabela Bolus
    *       - Foi criada a tabela Meals
    *       - Foi implementado o SQL_POPULATE_MEAL_TABLE para popular com valores padrões a tabela Meals
    *       - Foi reescrito o SQL_POPULATE_EVENT_TABLE para popular com os novos valores padrões a tabela Events
    *
    * */


    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     *
     * fonte: https://stackoverflow.com/questions/6540906/simple-export-and-import-of-a-sqlite-database-on-android
     * */
    public boolean importDB (String dbPath) throws IOException {
        close();
        File newDB = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);

        if (newDB.exists()){
            FileUtils.copyFile(new FileInputStream(newDB), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;

    }

    /**
     * Copies the database from current
     * internal application database over file at the specified location.
     *
     * fonte: https://stackoverflow.com/questions/6540906/simple-export-and-import-of-a-sqlite-database-on-android
     * */
    public boolean exportDB (String dbPath) throws IOException {
        //close();
        File newDB = new File(DB_FILEPATH);
        File oldDb = new File(dbPath);

        if (newDB.exists()){
            FileUtils.copyFile(new FileInputStream(newDB), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }


    public static void importDataBase(String backupDBPath) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        if (sd.canWrite()) {
            String currentDBPath = "//data//br.com.bwsystemssolutions.controlediabetes//databases//" + DATABASE_NAME;
            File backupDB = new File(data, currentDBPath);
            File currentDB = new File(sd, backupDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        }

    }

    public static void exportDataBase(String backupDBPath) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        if (sd.canWrite()) {
            String currentDBPath = "//data//br.com.bwsystemssolutions.controlediabetes//databases//" + DATABASE_NAME;
            //String backupDBPath = "<backupDBPath>";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        }
    }

}
