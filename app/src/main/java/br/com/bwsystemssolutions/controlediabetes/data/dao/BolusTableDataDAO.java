package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusTableDataDAO {

    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME;

    public BolusTableDataDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    public ArrayList<BolusTableData> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<BolusTableData> bolusTableDatas = new ArrayList<>();

        while(cursor.moveToNext()){
            BolusTableData bolusTableData = new BolusTableData();
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

    public boolean add (BolusTableData bolusTableData){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME, bolusTableData.getInsulin1());
        cv.put(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME, bolusTableData.getInsulin1());
        return db.insert(TABLE_NAME, null, cv) > 0;
    }

    public boolean delete (BolusTableData bolusTableData){
        // TODO codificar delete
        return false;
    }

    public boolean delete (int id){
        // TODO codificar delete
        return false;
    }

    public BolusTableData fetchById(int id){
        // TODO codificar fetchById
        return null;
    }

}
