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
    private static final int DATABASE_VERSION=1;

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
                TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME + "INTEGER NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_TIME_BLOCK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TimeBlockEntry.TABLE_NAME);
        onCreate(db);
    }
}
