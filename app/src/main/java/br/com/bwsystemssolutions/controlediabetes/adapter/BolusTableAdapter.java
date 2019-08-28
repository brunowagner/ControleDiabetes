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


//        String insert = "INSERT INTO " + CalculoDeBolusContract.BolusEntry.TABLE_NAME +
//                "(" + CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_ID_NAME +
//                "," + CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_ID_NAME +
//                "," + CalculoDeBolusContract.BolusEntry.COLUMN_INSULIN_NAME + ") VALUES " +
//                "(1,1,1.0)," +
//        "(1,2,1.0)," +
//        "(1,3,1.5)," +
//        "(1,4,0.0)," +
//        "(1,5,2.0)," +
//        "(1,6,0.0)," +
//        "(1,7,0.0)," +
//        "(2,1,1.5)," +
//        "(2,2,1.5)," +
//        "(2,3,2.0)," +
//        "(2,4,0.5)," +
//        "(2,5,2.5)," +
//        "(2,6,0.0)," +
//        "(2,7,0.0)," +
//        "(3,1,2.0)," +
//        "(3,2,2.0)," +
//        "(3,3,2.5)," +
//        "(3,4,1.0)," +
//        "(3,5,3.0)," +
//        "(3,6,0.5)," +
//        "(3,7,0.0)," +
//        "(4,1,2.5)," +
//        "(4,2,2.5)," +
//        "(4,3,3.0)," +
//        "(4,4,1.5)," +
//        "(4,5,3.5)," +
//        "(4,6,1.0)," +
//        "(4,7,0.0)," +
//        "(5,1,3.0)," +
//        "(5,2,3.0)," +
//        "(5,3,3.5)," +
//        "(5,4,2.0)," +
//        "(5,5,4.0)," +
//        "(5,6,1.5)," +
//        "(5,7,0.0)," +
//        "(6,7,0.0)," +
//        "(7,7,0.0)," +
//        "(8,7,0.0)," +
//        "(6,1,4.0)," +
//        "(6,2,4.0)," +
//        "(6,3,4.0)," +
//        "(6,4,2.5)," +
//        "(6,5,4.5)," +
//        "(6,6,2.0)," +
//        "(7,1,5.0)," +
//        "(7,2,5.0)," +
//        "(7,3,5.0)," +
//        "(7,4,3.0)," +
//        "(7,5,5.0)," +
//        "(7,6,2.5)," +
//        "(8,1,6.0)," +
//        "(8,2,6.0)," +
//        "(8,3,6.0)," +
//        "(8,4,3.5)," +
//        "(8,5,5.5)," +
//        "(8,6,3.0);";
//
//        mDb.execSQL(insert);

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
        BolusTableData bolusTableData = mBolusTableData.get(i);
        bolusTableAdapterViewHolder.mGlucoseTextView.setText(String.valueOf(bolusTableData.getGlucose()));

        BolusTableMealAdapter bolusTableMealAdapter = new BolusTableMealAdapter(bolusTableData.getBolusTableDataMeals());
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
            mBolusRecyclerView = itemView.findViewById(R.id.rv_bolus_table_glucose);
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

        String teste = "SELECT * FROM " + CalculoDeBolusContract.RecordEntry.TABLE_NAME +
                " WHERE " + CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME + " = ?;";

        Cursor c = mDb.rawQuery(sqlQuery, null);

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
        Log.d("bwvm", "refreshData: tamanho do cursor" + cursor.getCount());
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

                    hasNext = cursor.moveToNext();

                    currentId = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_ID_NAME));
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
