package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
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

    public int count(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT count(*) FROM " + TABLE_NAME,null);
        Log.d("bwvm", "count registros: " + cursor.getCount() + "    qtd coluna = " + cursor.getColumnCount());
        int count = 0;
        while(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        return count;
    }

    public Bolus fetchLessThanOrEqualToGlucoseByMeal(int glucose, String meal, int limit) {
        String tableName = TABLE_NAME;
        String selection = CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME + "<=?" +
                " and " + CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_NAME + "=?";
        String[] selectionArgs = new String[] {glucose + "", meal};
        String orderBy = CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME + " DESC";

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName,null,selection,
                selectionArgs,null,null,orderBy, String.valueOf(limit));

        return cursor.getCount() == 0? null: parseToBolus(cursor).get(0);
    }

    public ArrayList<Bolus> fetchByGlucose(int glucose) {
        //TODO 2021 - mudar o objeto retornado pelo fetch
        return fetchBolusArrayBy(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME, glucose);
    }

    private Bolus fetchBolusBy(String field, int value){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_NAME + " WHERE " + field + " = ?";
        final Cursor cursor = db.rawQuery(sqlString, new String[] {String.valueOf(value)});
        final ArrayList<Bolus> bolusArrayList = parseToBolus(cursor);
        db.close();
        if (bolusArrayList.size() == 0){
            return null;
        }
        return bolusArrayList.get(0);

    }

    private ArrayList<Bolus> fetchBolusArrayBy(String field, int value){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_NAME + " WHERE " + field + " = ?";
        final Cursor cursor = db.rawQuery(sqlString, new String[] {String.valueOf(value)});
        final ArrayList<Bolus> bolusArrayList = parseToBolus(cursor);
        db.close();
        if (bolusArrayList.size() == 0){
            return null;
        }
        return bolusArrayList;

    }



    public boolean add (Bolus bolus){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolus);
        boolean inserted = db.insert(TABLE_NAME, null, cv) > 0;
        db.close();
        return inserted;
    }

    public boolean add (ArrayList<Bolus> bolusArrayList){
        boolean allInserted = true;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Bolus b : bolusArrayList) {
            ContentValues cv = parseToContentValues(b);
            boolean inserted = db.insert(TABLE_NAME, null, cv) > 0;
            if (!inserted) { allInserted = false; }
        }
        db.close();
        return allInserted;
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

    public boolean update (ArrayList<Bolus> bolusArrayList){
        boolean allUpdated = true;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Bolus b : bolusArrayList) {
            ContentValues cv = parseToContentValues(b);
            boolean updated = db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {b.getId() + ""}) > 0;
            if (!updated) { allUpdated = false; }
        }
        db.close();
        return allUpdated;
    }


//    public boolean updateInsulineField (BolusTable3Data bolusTable3Data, int mealId, Double value){
//        final SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(CalculoDeBolusContract.BolusEntry.COLUMN_BOLUS_NAME, value);
//        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {bolus.getId() + ""}) > 0;
//    }

    private ArrayList<Bolus> parseToBolus(Cursor cursor){
        ArrayList<Bolus> bolusArrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            Bolus bolus = new Bolus();
            bolus.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry._ID)));
            bolus.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME)));
            bolus.setMeal_id(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_ID_NAME)));
            bolus.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_NAME)));
            bolus.setBolus(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusEntry.COLUMN_BOLUS_NAME)));
            bolusArrayList.add(bolus);
        }
        cursor.close();
        return bolusArrayList;
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
