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

    public void setBolusBlockTimeData(ArrayList<BolusTableData> bolusTableData){
        Log.d("bwvm", "setBolusBlockTimeData: ");
        mBolusTableData = bolusTableData;
        notifyDataSetChanged();
    }



    // ---------------------------------- Data Base ---------------------------------------------

    private Cursor getAllData() {
        String sqlQuery = CalculoDeBolusContract.BolusTableQuery.FETCH_ALL_DATA;

        return mDb.rawQuery(sqlQuery,null);
    }

    public void refreshData(){
        Cursor cursor = getAllData();
        int lastGlucoseId = -1;
        ArrayList<BolusTableData> bolusTableDataAL = new ArrayList<BolusTableData>();
        BolusTableData bolusTableData;
        if (cursor.moveToFirst()){
            do {
                int glucoseId = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_ID_NAME));
                if (lastGlucoseId != glucoseId){
                    if (bolusTableData != null){
                        bolusTableDataAL.add(bolusTableData);
                        //iniciar novo
                    }
                } else {



                }


                BolusTableData bolusTableData = new BolusTableData();
                bolusTableData.setGlucoseId (cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_ID_NAME)));
                bolusTableData.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_GLUCOSE_NAME)));

                BolusTableDataMeals bolusTableDataMeals = new BolusTableDataMeals();
                bolusTableDataMeals.setMealId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_MEAL_ID_NAME)));
                bolusTableDataMeals.setMeal(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.BolusTableEntry.COLUMN_MEAL_NAME)));

                if (/*mesmo id glicose*/){
                    cursor.
                }




                bolusTableData.relation = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_RELATION_NAME));
                bolusTableData.sensibilityFactor = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME));
                bolusTableData.tarjet = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_TARGET_NAME));

                bolusTimeBlockDataAL.add(bolusTableData);
            }while(cursor.moveToNext());

        }
        cursor.close();
        setBolusBlockTimeData(bolusTimeBlockDataAL);
    }

}
