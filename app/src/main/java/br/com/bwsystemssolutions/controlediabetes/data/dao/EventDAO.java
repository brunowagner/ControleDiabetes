package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Event;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.classe.Utilidades;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.interfaces.BasicDAO;

public class EventDAO implements BasicDAO<Event> {
    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.EventEntry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.EventEntry._ID;

    public EventDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    @Override
    public ArrayList<Event> fetchAll() {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        //final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        final Cursor cursor = db.query(CalculoDeBolusContract.EventEntry.TABLE_NAME,
                null, null, null, null,null,
                CalculoDeBolusContract.EventEntry.COLUMN_EVENT_NAME);
        return parseToEvent(cursor);
    }

    @Override
    public boolean add(Event event) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(event);
        boolean inserted = db.insert(TABLE_NAME, null, cv) > 0;
        db.close();
        return inserted;
    }

    public boolean addByAPP(Event event) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(event);
        boolean inserted = db.insert(TABLE_NAME, null, cv) > 0;
        db.close();
        return inserted;
    }

    @Override
    public boolean delete(int id) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean deleted = db.delete(CalculoDeBolusContract.RecordEntry.TABLE_NAME, COLUMN_ID_NAME + "= ?", new String[]{id + ""}) >0;
        Log.d("bwvm", "deleteRecord: valor de delete: " + deleted);
        db.close();
        return deleted;
    }

    @Override
    public boolean update(Event event) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(event);
        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {event.getId() + ""}) > 0;

    }

    //-- Parses --------------------------------------------------------------------------------

    private ArrayList<Event> parseToEvent(Cursor cursor) {
        ArrayList<Event> events = new ArrayList<>();

        while(cursor.moveToNext()){
            Event event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry._ID)));
            event.setSource(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry.COLUMN_SOURCE_NAME)));
            event.setSort(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry.COLUMN_SORT_NAME)));
            event.setText(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.EventEntry.COLUMN_EVENT_NAME)));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    private ContentValues parseToContentValues(Event event){
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.EventEntry.COLUMN_SORT_NAME, event.getSort());
        cv.put(CalculoDeBolusContract.EventEntry.COLUMN_SOURCE_NAME, event.getSource());
        cv.put(CalculoDeBolusContract.EventEntry.COLUMN_EVENT_NAME, event.getText());
        return cv;
    }
}
