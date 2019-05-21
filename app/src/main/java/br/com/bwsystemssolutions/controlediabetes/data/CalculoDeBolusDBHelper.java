package br.com.bwsystemssolutions.controlediabetes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;

import static br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract.*;


public class CalculoDeBolusDBHelper extends SQLiteOpenHelper {
    //TODO mudar o nome do arquivo do banco se o app mudar de nome.
    /**
     * O nome do banco será o nome do arquivo local no dispositivo que armazenará todos os dados.
     */
    private static final String DATABESE_NAME = "calculoDeBolus.db";

    //Representa a versao do banco de dados atual
    private static final int DATABASE_VERSION=2;

    public CalculoDeBolusDBHelper(Context context){
        super(context, DATABESE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Retorno de chamada que cria o arquivo de banco de dados
     * @param db SQLiteDataBase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TIME_BLOCK_TABLE = "CREATE TABLE " +
                TimeBlockEntry.TABLE_NAME + "(" +
                TimeBlockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TimeBlockEntry.COLUMN_INITIAL_TIME_NAME + " STRING NOT NULL," +
                TimeBlockEntry.COLUMN_RELATION_NAME + " INTEGER NOT NULL," +
                TimeBlockEntry.COLUMN_TARGET_NAME + " INTEGER NOT NULL," +
                TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME + " INTEGER NOT NULL" +
                ");";
        
        final String SQL_CREATE_RECORDS_TABLE = "CREATE TABLE " +
                RecordEntry.TABLE_NAME + "(" +
                RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecordEntry.COLUMN_DATE_TIME_NAME + " TEXT NOT NULL," +
                RecordEntry.COLUMN_GLUCOSE_NAME + " INTEGER," +
                RecordEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL," +
                RecordEntry.COLUMN_CARBOHYDRATE_NAME + " INTEGER," +
                RecordEntry.COLUMN_FAST_INSULIN_NAME + " REAL," +
                RecordEntry.COLUMN_BASAL_INSULIN_NAME + " REAL," +
                RecordEntry.COLUMN_SICK_NAME + " INTEGER," +
                RecordEntry.COLUMN_MEDICAMENT_NAME + " INTEGER," +
                RecordEntry.COLUMN_NOTE_NAME + " TEXT" +
                ");";
        
        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " +
                EventEntry.TABLE_NAME + "(" +
                EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL," +
                EventEntry.COLUMN_EVENT_SOURCE + " TEXT NOT NULL" +
                ");";

        final String SQL_POPULATE_EVENT_TABLE = "INSERT INTO " +
                EventEntry.TABLE_NAME + " (" +
                EventEntry.COLUMN_EVENT_NAME + "," + EventEntry.COLUMN_EVENT_SOURCE + ") VALUES " +
                "('Antes do café da manhã','app')," +
                "('Antes da colação','app')," +
                "('Antes do almoço','app')," +
                "('Antes do lanche','app'), " +
                "('Antes do jantar','app'), " +
                "('Antes da ceia','app'), " +
                "('Após o café da manhã','app')," +
                "('Após a colação','app')," +
                "('Após o almoço','app')," +
                "('Após o lanche','app'), " +
                "('Após o jantar','app'), " +
                "('Após a ceia','app'), " +
                "('Medição Extra','app')," +
                "('Outro','app');";


        db.execSQL(SQL_CREATE_TIME_BLOCK_TABLE);
        db.execSQL(SQL_CREATE_RECORDS_TABLE);
        db.execSQL(SQL_CREATE_EVENTS_TABLE);
        db.execSQL(SQL_POPULATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TimeBlockEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(db);
    }

    /* --------------------------------------------------------------------------------------------
    *                       Histório de mudança de versão
    *  --------------------------------------------------------------------------------------------
    *
    *   - Versão 2:
    *        - Foi adicionada a coluna 'EventEntry.COLUMN_EVENT_SOURCE' na tabela 'Events'
    *        - Foi implementado o SQL_POPULATE_EVENT_TABLE para popular com valores padrões a tabela Events
    *
    * */
}
