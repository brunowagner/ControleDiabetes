package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private RecyclerView mParentRecyclerView;
    private Context context;
    private int mTouchedRvTag;

    public BolusTableAdapter (Context context, CalculoDeBolusDBHelper dbHelper, RecyclerView parentRecyclerView){
        this.context = context;
        this.dbHelper = dbHelper;
        this. mDb = dbHelper.getWritableDatabase();
        this.mParentRecyclerView = parentRecyclerView;

        final String SQL_CREATE_BOLUS_TABLE_2 = "CREATE TABLE IF NOT EXISTS " +
                CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME + "(" +
                CalculoDeBolusContract.BolusTable2Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME + " INTEGER NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME + " REAL NOT NULL" +
                ");";

        mDb.execSQL(SQL_CREATE_BOLUS_TABLE_2);



        Log.d("bwvm", "BolusTableAdapter: antes da consulta ");

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME, null);

        Log.d("bwvm", "BolusTableAdapter: tamanho do cursor kdjf ajd fklajd jasd fj: " + cursor.getCount());

        if (cursor.getCount() == 0) {

            String insert = "INSERT INTO " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME +
                    "(" + CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME + ") VALUES " +

                    "(60,1.0,1.0,1.5,0.0,2.0,0.0,0.0)," +
                    "(100,1.5,1.5,2.0,0.5,2.5,0.0,0.0)," +
                    "(150,2.0,2.0,2.5,1.0,3.0,0.5,0.0)," +
                    "(200,2.5,2.5,3.0,1.5,3.5,1.0,0.0)," +
                    "(250,3.0,3.0,3.5,2.0,4.0,1.5,0.0)," +
                    "(300,4.0,4.0,4.0,2.5,4.5,2.0,0.0)," +
                    "(400,5.0,5.0,5.0,3.0,5.0,2.5,0.0)," +
                    "(500,6.0,6.0,6.0,3.5,5.5,3.0,0.0);";

            mDb.execSQL(insert);
        }
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

//    @Override
//    public void onBindViewHolder(@NonNull BolusTableAdapter.BolusTableAdapterViewHolder bolusTableAdapterViewHolder, int i) {
//        Log.d("bwvm", "onBindViewHolder: position: " + i);
//        BolusTableData bolusTableData = mBolusTableData.get(i);
//        bolusTableAdapterViewHolder.mGlucoseTextView.setText(String.valueOf(bolusTableData.getGlucose()));
//
//        BolusTableMealAdapter bolusTableMealAdapter = new BolusTableMealAdapter(bolusTableData.getBolusTableDataMeals());
//
//        bolusTableAdapterViewHolder.mBolusRecyclerView.setAdapter(bolusTableMealAdapter);
//        bolusTableAdapterViewHolder.mBolusRecyclerView.setTag(bolusTableData.getGlucoseId());
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
//        bolusTableAdapterViewHolder.mBolusRecyclerView.setLayoutManager(linearLayoutManager);
//        bolusTableAdapterViewHolder.mBolusRecyclerView.addOnScrollListener(synchronizeScroll);
//        bolusTableAdapterViewHolder.mBolusRecyclerView.addOnItemTouchListener(touchedItem);
//
//
//        /*
//         * Use this setting to improve performance if you know that changes in content do not
//         * change the child layout size in the RecyclerView
//         */
//        bolusTableAdapterViewHolder.mBolusRecyclerView.setHasFixedSize(true);
//
//    }

    @Override
    public void onBindViewHolder(@NonNull BolusTableAdapter.BolusTableAdapterViewHolder bolusTableAdapterViewHolder, int i) {
        Log.d("bwvm", "onBindViewHolder: position: " + i);
        BolusTableData bolusTableData = mBolusTableData.get(i);
        //bolusTableAdapterViewHolder.mGlucoseTextView.setText(String.valueOf(bolusTableData.getGlucose()));

        bolusTableAdapterViewHolder.mMeal1.setText(bolusTableData.getMeal1());
        bolusTableAdapterViewHolder.mMeal2.setText(bolusTableData.getMeal2());
        bolusTableAdapterViewHolder.mMeal3.setText(bolusTableData.getMeal3());
        bolusTableAdapterViewHolder.mMeal4.setText(bolusTableData.getMeal4());
        bolusTableAdapterViewHolder.mMeal5.setText(bolusTableData.getMeal5());
        bolusTableAdapterViewHolder.mMeal6.setText(bolusTableData.getMeal6());
        bolusTableAdapterViewHolder.mMeal7.setText(bolusTableData.getMeal7());

        bolusTableAdapterViewHolder.mInsul1.setText(String.valueOf(bolusTableData.getInsulin1()));
        bolusTableAdapterViewHolder.mInsul2.setText(String.valueOf(bolusTableData.getInsulin2()));
        bolusTableAdapterViewHolder.mInsul3.setText(String.valueOf(bolusTableData.getInsulin3()));
        bolusTableAdapterViewHolder.mInsul4.setText(String.valueOf(bolusTableData.getInsulin4()));
        bolusTableAdapterViewHolder.mInsul5.setText(String.valueOf(bolusTableData.getInsulin5()));
        bolusTableAdapterViewHolder.mInsul6.setText(String.valueOf(bolusTableData.getInsulin6()));
        bolusTableAdapterViewHolder.mInsul7.setText(String.valueOf(bolusTableData.getInsulin7()));
    }

    @Override
    public int getItemCount() {
        if (null == mBolusTableData) return 0;
        return mBolusTableData.size();

    }

    private RecyclerView.OnScrollListener synchronizeScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int numOfRecyclesView = getItemCount();

            if ((int) recyclerView.getTag() == mTouchedRvTag) {
                for (int noOfRecyclerView = 0; noOfRecyclerView <= numOfRecyclesView; noOfRecyclerView++) {
                    if (noOfRecyclerView != (int) recyclerView.getTag()) {
                        RecyclerView tempRecyclerView = (RecyclerView) recyclerView.getRootView().findViewWithTag(noOfRecyclerView);
                        if (tempRecyclerView != null) tempRecyclerView.scrollBy(dx, dy);
                    }
                }
            }
        }
    };

    RecyclerView.OnItemTouchListener touchedItem = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            mTouchedRvTag = (int) recyclerView.getTag();
            Log.d("bwvm", "onInterceptTouchEvent: tag touched: " + mTouchedRvTag);
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    };



    public class BolusTableAdapterViewHolder extends RecyclerView.ViewHolder {

//        public final TextView mGlucoseTextView;
//        public final RecyclerView mBolusRecyclerView;
        public final TextView mMeal1;
        public final TextView mMeal2;
        public final TextView mMeal3;
        public final TextView mMeal4;
        public final TextView mMeal5;
        public final TextView mMeal6;
        public final TextView mMeal7;

        public final TextView mInsul1;
        public final TextView mInsul2;
        public final TextView mInsul3;
        public final TextView mInsul4;
        public final TextView mInsul5;
        public final TextView mInsul6;
        public final TextView mInsul7;




        public BolusTableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
//            mGlucoseTextView = itemView.findViewById(R.id.tv_glucose);
//            mBolusRecyclerView = itemView.findViewById(R.id.rv_bolus_table_row_meals);
            mMeal1 = itemView.findViewById(R.id.tv_meal_1);
            mMeal2 = itemView.findViewById(R.id.tv_meal_2);
            mMeal3 = itemView.findViewById(R.id.tv_meal_3);
            mMeal4 = itemView.findViewById(R.id.tv_meal_4);
            mMeal5 = itemView.findViewById(R.id.tv_meal_5);
            mMeal6 = itemView.findViewById(R.id.tv_meal_6);
            mMeal7 = itemView.findViewById(R.id.tv_meal_7);

            mInsul1 = itemView.findViewById(R.id.tv_insulin_1);
            mInsul2 = itemView.findViewById(R.id.tv_insulin_2);
            mInsul3 = itemView.findViewById(R.id.tv_insulin_3);
            mInsul4 = itemView.findViewById(R.id.tv_insulin_4);
            mInsul5 = itemView.findViewById(R.id.tv_insulin_5);
            mInsul6 = itemView.findViewById(R.id.tv_insulin_6);
            mInsul7 = itemView.findViewById(R.id.tv_insulin_7);
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
        String sqlQuery = "SELECT * FROM " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME;//CalculoDeBolusContract.BolusTableQuery.FETCH_ALL_DATA;
        Log.d("bwvm", "getAllData: sqlQuery: " + sqlQuery);

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
        String[] meals = context.getResources().getStringArray(R.array.meal_names_array);

        Cursor cursor = getAllData();
        Log.d("bwvm", "refreshData BolusTableAdapter: tamanho do cursor: " + cursor.getCount());
        if (cursor.getCount() <= 0) return;

        ArrayList<BolusTableData> bolusTableDatas = new ArrayList<>();


        if (cursor.moveToFirst()){

            do {
                BolusTableData bolusTableData = new BolusTableData();

                bolusTableData.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME)));

                bolusTableData.setMeal1(meals[1]);
                bolusTableData.setMeal2(meals[2]);
                bolusTableData.setMeal3(meals[3]);
                bolusTableData.setMeal4(meals[4]);
                bolusTableData.setMeal5(meals[5]);
                bolusTableData.setMeal6(meals[6]);
                bolusTableData.setMeal7(meals[7]);


                bolusTableData.setInsulin1(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME)));
                bolusTableData.setInsulin2(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME)));
                bolusTableData.setInsulin3(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME)));
                bolusTableData.setInsulin4(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME)));
                bolusTableData.setInsulin5(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME)));
                bolusTableData.setInsulin6(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME)));
                bolusTableData.setInsulin7(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME)));

                bolusTableDatas.add(bolusTableData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        setBolusTableData(bolusTableDatas);
    }

}
