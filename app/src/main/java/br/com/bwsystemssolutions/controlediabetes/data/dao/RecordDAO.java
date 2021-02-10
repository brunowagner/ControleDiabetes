package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.classe.Utilidades;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.interfaces.BasicDAO;

public class RecordDAO implements BasicDAO<Record> {
    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.RecordEntry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.RecordEntry._ID;

    public RecordDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    @Override
    public ArrayList<Record> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor  = db.query(TABLE_NAME,
                null, null, null, null,null,
                CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME + " DESC");
        ArrayList<Record> records = parseToRecord(cursor);
        db.close();
        return records;
    }

    public boolean exists(String date, String time){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME + "=?";
        String[] args = new String[] { Utilidades.convertDateTimeToSQLiteFormat(date, time) };
        final Cursor cursor = db.query(CalculoDeBolusContract.RecordEntry.TABLE_NAME, new String[]{CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME}, selection, args, null, null, null);

        boolean response = cursor.getCount() > 0 ? true : false;
        cursor.close();
        db.close();
        return response;
    }

    public boolean delete(Record record){
        boolean deleted = delete(record.getId());
        return deleted;
    }

    public boolean add(Record record){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(record);
        boolean inserted = db.insert(TABLE_NAME, null, cv) > 0;
        db.close();
        return inserted;
    }

    @Override
    public boolean delete(int id) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean deleted = db.delete(CalculoDeBolusContract.RecordEntry.TABLE_NAME, CalculoDeBolusContract.RecordEntry._ID + "= ?", new String[]{id + ""}) >0;
        Log.d("bwvm", "deleteRecord: valor de delete: " + deleted);
        db.close();
        return deleted;
    }


    @Override
    public boolean update (Record record){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(record);
        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {record.getId() + ""}) > 0;
    }



    //-- Parses --------------------------------------------------------------------------------

    private ArrayList<Record> parseToRecord(Cursor cursor) {
        ArrayList<Record> records = new ArrayList<>();

        while(cursor.moveToNext()){
            Record record = new Record();
            record.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry._ID)));
            record.setDateFromStringDateSqlite(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME)));
            record.setCarbohydrate(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME)));
            record.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME)));
            record.setFastInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME)));
            record.setBasalInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME)));
            record.setEvent(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME)));
            record.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_NAME)));
            record.setNote(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME)));
            record.setSick(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME))>0);
            record.setMedicament(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME))>0);
            records.add(record);
        }
        cursor.close();
        return records;
    }

    private ContentValues parseToContentValues(Record record){
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME, Utilidades.convertDateTimeToSQLiteFormat(record.getDate()));
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME, String.valueOf(record.getCarbohydrate()));
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME, String.valueOf(record.getGlucose()));
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME, record.getEvent());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_NAME, record.getMeal());
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME, String.valueOf(record.getFastInsulin()));
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME, String.valueOf(record.getBasalInsulin()));
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME, record.isSick()?1:0);
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME, record.isMedicament()?1:0);
        cv.put(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME, record.getNote());
        return cv;
    }




}
