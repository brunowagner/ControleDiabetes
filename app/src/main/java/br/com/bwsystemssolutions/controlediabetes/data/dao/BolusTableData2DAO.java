package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTable2Data;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusTableData2DAO {

    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.BolusTable2Entry._ID;

    public BolusTableData2DAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    public ArrayList<BolusTable2Data> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME,null);
        return parseToBolusTableDatas(cursor);
    }

    public boolean add (BolusTable2Data bolusTable2Data){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolusTable2Data);
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

    public boolean update (BolusTable2Data bolusTable2Data){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolusTable2Data);
        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {bolusTable2Data.getId() + ""}) > 0;
    }

    public boolean updateInsulineField (BolusTable2Data bolusTable2Data, String fieldName, Double value){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(fieldName, value);
        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {bolusTable2Data.getId() + ""}) > 0;
    }


    public BolusTable2Data fetchById(int id){
        return fetchBy(COLUMN_ID_NAME, id);
//        final SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String sqlString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_NAME + " = ?";
//        final Cursor cursor = db.rawQuery(sqlString, new String[] {String.valueOf(id)});
//
//        return parseToBolusTableDatas(cursor).get(0);
    }

    @NonNull
    private ContentValues parseToContentValues(BolusTable2Data bolusTable2Data) {
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME, bolusTable2Data.getGlucose());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME, bolusTable2Data.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME, bolusTable2Data.getInsulin2());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME, bolusTable2Data.getInsulin3());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME, bolusTable2Data.getInsulin4());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME, bolusTable2Data.getInsulin5());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME, bolusTable2Data.getInsulin6());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME, bolusTable2Data.getInsulin7());
        return cv;
    }

    private ArrayList<BolusTable2Data> parseToBolusTableDatas(Cursor cursor){
        ArrayList<BolusTable2Data> bolusTable2DataArrayList = new ArrayList<>();


        while(cursor.moveToNext()){
            BolusTable2Data bolusTable2Data = new BolusTable2Data();
            bolusTable2Data.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry._ID)));
            bolusTable2Data.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME)));
            bolusTable2Data.setInsulin1(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME)));
            bolusTable2Data.setInsulin2(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME)));
            bolusTable2Data.setInsulin3(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME)));
            bolusTable2Data.setInsulin4(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME)));
            bolusTable2Data.setInsulin5(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME)));
            bolusTable2Data.setInsulin6(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME)));
            bolusTable2Data.setInsulin7(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME)));
            bolusTable2DataArrayList.add(bolusTable2Data);
        }
        cursor.close();
        return bolusTable2DataArrayList;
    }

    public BolusTable2Data fetchByGlucose(int glucose) {
        return fetchBy(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME, glucose);
    }

    public BolusTable2Data fetchLessThanOrEqualToGlucose(int glucose, int limit) {
        String tableName = TABLE_NAME;
        String selection = CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME + "<=?";
        String[] selectionArgs = new String[] {glucose + ""};
        String orderBy = CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME + " DESC";

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName,null,selection,
                selectionArgs,null,null,orderBy, String.valueOf(limit));

        return cursor.getCount() == 0? null: parseToBolusTableDatas(cursor).get(0);
    }

    private BolusTable2Data fetchBy(String fiend, int value){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_NAME + " WHERE " + fiend + " = ?";
        final Cursor cursor = db.rawQuery(sqlString, new String[] {String.valueOf(value)});
        final ArrayList<BolusTable2Data> bolusTable2Data = parseToBolusTableDatas(cursor);
        db.close();
        if (bolusTable2Data.size() == 0){
            return null;
        }
        return bolusTable2Data.get(0);

    }
}
