package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class MealDAO {
    private CalculoDeBolusDBHelper dbHelper;
    private String TABLE_NAME = CalculoDeBolusContract.MealEntry.TABLE_NAME;
    private String COLUMN_ID_NAME = CalculoDeBolusContract.MealEntry._ID;

    public MealDAO(Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
    }

    public ArrayList<Meal> fetchAll(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        //TODO - 2021 - trocar rawQuery por query (mais recomendado)
        final Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return parseToMeal(cursor);
    }

    public boolean add (Meal meal){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(meal);
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

    public boolean update (Meal meal){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = parseToContentValues(meal);
        return db.update (TABLE_NAME, cv, COLUMN_ID_NAME + " = ?", new String[] {meal.getId() + ""}) > 0;
    }

    private ArrayList<Meal> parseToMeal(Cursor cursor){
        ArrayList<Meal> meals = new ArrayList<>();

        while(cursor.moveToNext()){
            Meal meal = new Meal();
            meal.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.MealEntry._ID)));
            meal.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.MealEntry.COLUMN_MEAL_NAME)));
            meal.setSort(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.MealEntry.COLUMN_SORT_NAME)));
            meal.setSource(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.MealEntry.COLUMN_SOURCE_NAME)));
            meals.add(meal);
        }
        cursor.close();
        return meals;
    }

    @NonNull
    private ContentValues parseToContentValues(Meal meal) {
        ContentValues cv = new ContentValues();
        cv.put(CalculoDeBolusContract.MealEntry.COLUMN_MEAL_NAME, meal.getMeal());
        cv.put(CalculoDeBolusContract.MealEntry.COLUMN_SORT_NAME, meal.getSort());
        cv.put(CalculoDeBolusContract.MealEntry.COLUMN_SOURCE_NAME, meal.getSource());
        return cv;
    }

}
