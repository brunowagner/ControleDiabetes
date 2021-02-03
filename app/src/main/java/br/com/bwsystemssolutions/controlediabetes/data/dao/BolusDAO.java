package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusDAO {
    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.BolusEntry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.BolusEntry._ID;

    public BolusDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    public ArrayList<Bolus> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME,null);
        return parseToBolus(cursor);
//        Seleciona todos as registros da tabela bolusTable linkando com a tabela meals.
//        select bolusTable.id as bolus_id, bolus.bolus, bolus.glucose, meals.id as meal_id, meals.meal, meals.sort, meals.source
//        from bolusTable
//        INNER JOIN meals
//        on bolusTable.meal_id = meals.id;

    }



    public boolean add (Bolus bolus){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolus);
        boolean inserted = db.insert(TABLE_NAME, null, cv) > 0;
        db.close();
        return inserted;
    }

    public boolean delete (int id){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = COLUMN_ID_NAME + " = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        boolean deleted = db.delete(TABLE_NAME, whereClause, whereArgs ) > 0;
        db.close();
        return deleted;
    }

    public boolean deleteByGlucose (int glucose){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME + " = ?";
        String[] whereArgs = new String[] {String.valueOf(glucose)};
        boolean deleted = db.delete(TABLE_NAME, whereClause, whereArgs ) > 0;
        db.close();
        return deleted;
    }

    public boolean update (Bolus bolus){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolus);
        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {bolus.getId() + ""}) > 0;
    }

    private ArrayList<Bolus> parseToBolus(Cursor cursor){
        ArrayList<Bolus> boluss = new ArrayList<>();

        while(cursor.moveToNext()){
            Bolus bolus = new Bolus();
            bolus.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry._ID)));
            bolus.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME)));
            bolus.setMeal_id(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_ID_NAME)));
            bolus.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_NAME)));
            bolus.setBolus(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_BOLUS_NAME)));
            boluss.add(bolus);
        }
        cursor.close();
        return boluss;
    }

    @NonNull
    private ContentValues parseToContentValues(Bolus bolus) {
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME, bolus.getGlucose());
        cv.put(CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_ID_NAME, bolus.getMeal_id());
        cv.put(CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_NAME, bolus.getMeal());
        cv.put(CalculoDeBolusContract.BolusEntry.COLUMN_BOLUS_NAME, bolus.getBolus());
        return cv;
    }
}
