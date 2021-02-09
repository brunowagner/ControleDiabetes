package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class RecordDAO {
    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.RecordEntry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.RecordEntry._ID;

    public RecordDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    public ArrayList<Record> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor cursor  = db.query(CalculoDeBolusContract.RecordEntry.TABLE_NAME,
                null, null, null, null,null,
                CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME + " DESC");

        return parseToRecord(cursor);
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


}
