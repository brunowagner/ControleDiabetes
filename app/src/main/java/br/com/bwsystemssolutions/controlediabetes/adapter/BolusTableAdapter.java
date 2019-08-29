package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Instant;
import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableDataMeals;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusTableAdapter extends RecyclerView.Adapter<BolusTableAdapter.BolusTableAdapterViewHolder> {

    Cursor mCursor;
    private SQLiteDatabase mDb;
    private CalculoDeBolusDBHelper dbHelper;
    private ArrayList<BolusTableData> mBolusTableData;
    private Context context;

    public BolusTableAdapter (Context context, CalculoDeBolusDBHelper dbHelper){
        this.context = context;
        this.dbHelper = dbHelper;
        mDb = dbHelper.getWritableDatabase();


        Log.d("bwvm", "BolusTableAdapter: antes da consulta ");

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + CalculoDeBolusContract.BolusEntry.TABLE_NAME, null);

        Log.d("bwvm", "BolusTableAdapter: tamanho do cursor kdjf ajd fklajd jasd fj: " + cursor.getCount());

        if (cursor.getCount() == 0) {
            String insert = "INSERT INTO " + CalculoDeBolusContract.BolusEntry.TABLE_NAME +
                    "(" + CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_ID_NAME +
                    "," + CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_ID_NAME +
                    "," + CalculoDeBolusContract.BolusEntry.COLUMN_INSULIN_NAME + ") VALUES " +
                    "(1,2,1.0)," +
            "(1,3,1.0)," +
            "(1,4,1.5)," +
            "(1,5,0.0)," +
            "(1,6,2.0)," +
            "(1,7,0.0)," +
            "(1,8,0.0)," +
            "(2,2,1.5)," +
            "(2,3,1.5)," +
            "(2,4,2.0)," +
            "(2,5,0.5)," +
            "(2,6,2.5)," +
            "(2,7,0.0)," +
            "(2,8,0.0)," +
            "(3,2,2.0)," +
            "(3,3,2.0)," +
            "(3,4,2.5)," +
            "(3,5,1.0)," +
            "(3,6,3.0)," +
            "(3,7,0.5)," +
            "(3,8,0.0)," +
            "(4,2,2.5)," +
            "(4,3,2.5)," +
            "(4,4,3.0)," +
            "(4,5,1.5)," +
            "(4,6,3.5)," +
            "(4,7,1.0)," +
            "(4,8,0.0)," +
            "(5,2,3.0)," +
            "(5,3,3.0)," +
            "(5,4,3.5)," +
            "(5,5,2.0)," +
            "(5,6,4.0)," +
            "(5,7,1.5)," +
            "(5,8,0.0)," +
            "(6,8,0.0)," +
            "(7,8,0.0)," +
            "(8,8,0.0)," +
            "(6,2,4.0)," +
            "(6,3,4.0)," +
            "(6,4,4.0)," +
            "(6,5,2.5)," +
            "(6,6,4.5)," +
            "(6,7,2.0)," +
            "(7,2,5.0)," +
            "(7,3,5.0)," +
            "(7,4,5.0)," +
            "(7,5,3.0)," +
            "(7,6,5.0)," +
            "(7,7,2.5)," +
            "(8,2,6.0)," +
            "(8,3,6.0)," +
            "(8,4,6.0)," +
            "(8,5,3.5)," +
            "(8,6,5.5)," +
            "(8,7,3.0);";

            mDb.execSQL(insert);
        }

        cursor = mDb.rawQuery("SELECT * FROM glucoses;", null);

        if (cursor.getCount() == 0 ) {
            String insertGlucoses = "INSERT INTO glucoses (glucose) values (60),(100),(150),(200),(250),(300),(400),(500);";
            mDb.execSQL(insertGlucoses);
        }

        Log.d("bwvm", "BolusTableAdapter: curso do glucoses: " + cursor.getCount());



//        mDb.rawQuery(insert,null);


    }

    @NonNull
    @Override
    public BolusTableAdapter.BolusTableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdFromListItem = R.layout.list_item_row_bolus_table;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
        return new BolusTableAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BolusTableAdapter.BolusTableAdapterViewHolder bolusTableAdapterViewHolder, int i) {
        Log.d("bwvm", "onBindViewHolder: position: " + i);
        BolusTableData bolusTableData = mBolusTableData.get(i);
        bolusTableAdapterViewHolder.mGlucoseTextView.setText(String.valueOf(bolusTableData.getGlucose()));

        BolusTableMealAdapter bolusTableMealAdapter = new BolusTableMealAdapter(bolusTableData.getBolusTableDataMeals());

        if (null == bolusTableAdapterViewHolder.mBolusRecyclerView) {
            Log.d("bwvm", "onBindViewHolder: bolusTableMealAdapter é nulo");
        } else {
            Log.d("bwvm", "onBindViewHolder: bolusTableMealAdapter não é nulo");
        }

        bolusTableAdapterViewHolder.mBolusRecyclerView.setAdapter(bolusTableMealAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        bolusTableAdapterViewHolder.mBolusRecyclerView.setLayoutManager(linearLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        bolusTableAdapterViewHolder.mBolusRecyclerView.setHasFixedSize(true);

    }

    @Override
    public int getItemCount() {
        if (null == mBolusTableData) return 0;
        return mBolusTableData.size();

    }



    public class BolusTableAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mGlucoseTextView;
        public final RecyclerView mBolusRecyclerView;


        public BolusTableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mGlucoseTextView = itemView.findViewById(R.id.tv_glucose);
            mBolusRecyclerView = itemView.findViewById(R.id.rv_bolus_table_row_meals);
        }
    }


    // ---------------------------------- Functions ---------------------------------------------

    public void setBolusTableData(ArrayList<BolusTableData> bolusTableData){
        Log.d("bwvm", "setBolusBlockTimeData: ");
        mBolusTableData = bolusTableData;
        notifyDataSetChanged();
    }



    // ---------------------------------- Data Base ---------------------------------------------

    private Cursor getAllData() {
        String sqlQuery = CalculoDeBolusContract.BolusTableQuery.FETCH_ALL_DATA;
        Log.d("bwvm", "getAllData: sqlQuery: " + sqlQuery);

        String teste2 = "SELECT glucoses._id, glucoses.glucose, meals._id, meals.meal, bolus.insulin " +
                        "FROM glucoses, meals, bolus " +
                        "WHERE bolus.glucose_id = glucoses._id AND meals._id = bolus.meal_id ;";

        String teste = "SELECT * FROM " + CalculoDeBolusContract.RecordEntry.TABLE_NAME +
                " WHERE " + CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME + " = 90;";

        //String teste3 = "SELECT glucoses._id AS glucose_id, glucoses.glucose AS glucose, meals._id AS meal_id, meals.meal AS meal, bolus.insulin AS insulin FROM glucoses LEFT JOIN bolus on bolus.glucose_id = glucoses._id LEFT JOIN meals on meals._id = bolus.meal_id ORDER BY glucose_id, meal_id;";
        String teste3 = "SELECT glucoses._id AS glucose_id, glucoses.glucose AS glucose, bolus.insulin AS insulin FROM glucoses LEFT JOIN bolus on bolus.glucose_id = glucoses._id;";
        //Cursor c = mDb.rawQuery(teste, null);

        Cursor c = mDb.rawQuery(sqlQuery, null);

        //Cursor c = mDb.rawQuery(teste3,null);

        return c;
    }


    /*
    * 	public void refreshData(){

		Cursor cursor = getAllData();
		Log.d("bwvm", "refreshData: tamanho do cursor" + cursor.getCount());
		if (cursor.getCount() <= 0) return;
		ArrayList<Record> records = new ArrayList<Record>();
		if (cursor.moveToFirst()){
			do {
				Record record = new Record();
				record.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry._ID)));
				record.setDateFromStringDateSqlite(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME)));
				record.setCarbohydrate(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME)));
				record.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME)));
				record.setFastInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME)));
				record.setBasalInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME)));
				record.setEvent(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME)));
				record.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_NAME)));
				record.setMealTime(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEAL_TIME_NAME)));
				record.setNote(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME)));
				record.setSick(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME))>0);
				record.setMedicament(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME))>0);

				records.add(record);
			}while(cursor.moveToNext());

		}
		cursor.close();
		setRecords(records);
	}
    * */

    public void refreshData(){

        int contaLoop1 = 0;
        int contaLoop2 = 0;

        Cursor cursor = getAllData();
        Log.d("bwvm", "refreshData BolusTableAdapter: tamanho do cursor: " + cursor.getCount());
        if (cursor.getCount() <= 0) return;
        boolean hasNext = false;
        ArrayList<BolusTableData> bolusTableDatas = new ArrayList<BolusTableData>();
        if (cursor.moveToFirst()){
            do {
                BolusTableData bolusTableData = new BolusTableData();
                bolusTableData.setGlucoseId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_ID_NAME)));
                bolusTableData.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_NAME)));

                int glucoseId = bolusTableData.getGlucoseId();
                int currentId = -1;

                ArrayList<BolusTableDataMeals> bolusTableDataMeals = new ArrayList<BolusTableDataMeals>();
                do {
                    BolusTableDataMeals bolus = new BolusTableDataMeals();
                    bolus.setMealId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_MEAL_ID_NAME)));
                    bolus.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_MEAL_NAME)));
                    bolus.setmInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_INSULIN_NAME)));

                    bolusTableDataMeals.add(bolus);

                    Log.d("bwvm", "refreshData: glucose:" + glucoseId + "    mealId" + bolus.getMealId() + "    Insulin:" + bolus.getmInsulin());

                    hasNext = cursor.moveToNext();

                    if (hasNext) currentId = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_ID_NAME));
                    contaLoop2 += 1;

                }while(hasNext && glucoseId == currentId && contaLoop2 < 1000);

                bolusTableData.setBolusTableDataMeals(bolusTableDataMeals);

                bolusTableDatas.add(bolusTableData);
                contaLoop1 += 1;
            }while(hasNext && contaLoop1 < 1000);

        }

        Log.d("bwvm", "refreshData: contaLoop1: " + contaLoop1);
        Log.d("bwvm", "refreshData: contaLoop2: " + contaLoop2);

//        ArrayList<BolusTableDataMeals> bolusTableDataMeals = new ArrayList<BolusTableDataMeals>();
//        cursor.moveToFirst();
//        do {
//            BolusTableDataMeals bolus = new BolusTableDataMeals();
//            bolus.setMealId();
//            bolus.setMeal();
//            bolus.setmInsulin();
//
//            bolusTableDataMeals.add(bolus);
//        }while(cursor.moveToNext());

        cursor.close();
        setBolusTableData(bolusTableDatas);
    }

}
