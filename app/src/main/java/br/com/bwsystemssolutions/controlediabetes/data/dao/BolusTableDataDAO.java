package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.apache.commons.collections.list.CursorableLinkedList;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusTableDataDAO {

    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.BolusTable2Entry._ID;

    public BolusTableDataDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    public ArrayList<BolusTableData> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME,null);
        return parseToBoluslusTableDatas(cursor);
    }

    public boolean add (BolusTableData bolusTableData){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolusTableData);
        return db.insert(TABLE_NAME, null, cv) > 0;
    }

    public boolean delete (int id){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = COLUMN_ID_NAME + " = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        return db.delete(TABLE_NAME, whereClause, whereArgs ) > 0;
    }

    public boolean update (BolusTableData bolusTableData){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(bolusTableData);
        return db.update (TABLE_NAME, cv, "WHERE " + COLUMN_ID_NAME + " = ?", new String[] {bolusTableData.getId() + ""}) > 0;
    }


    public BolusTableData fetchById(int id){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_NAME + " = ?";
        final Cursor cursor = db.rawQuery(sqlString, new String[] {String.valueOf(id)});

        return parseToBoluslusTableDatas(cursor).get(0);
    }

    @NonNull
    private ContentValues parseToContentValues(BolusTableData bolusTableData) {
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME, bolusTableData.getGlucose());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME, bolusTableData.getInsulin2());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME, bolusTableData.getInsulin3());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME, bolusTableData.getInsulin4());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME, bolusTableData.getInsulin5());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME, bolusTableData.getInsulin6());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME, bolusTableData.getInsulin7());
        return cv;
    }

    private ArrayList<BolusTableData> parseToBoluslusTableDatas(Cursor cursor){
        ArrayList<BolusTableData> bolusTableDatas = new ArrayList<>();

        while(cursor.moveToNext()){
            BolusTableData bolusTableData = new BolusTableData();
            bolusTableData.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry._ID)));
            bolusTableData.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME)));
            bolusTableData.setInsulin1(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME)));
            bolusTableData.setInsulin2(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME)));
            bolusTableData.setInsulin3(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME)));
            bolusTableData.setInsulin4(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME)));
            bolusTableData.setInsulin5(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME)));
            bolusTableData.setInsulin6(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME)));
            bolusTableData.setInsulin7(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME)));
            bolusTableDatas.add(bolusTableData);
        }
        cursor.close();
        return bolusTableDatas;
    }
}
