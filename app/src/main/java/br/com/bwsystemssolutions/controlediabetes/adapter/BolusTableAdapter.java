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
        return mDb.query(CalculoDeBolusContract.TimeBlockEntry.TABLE_NAME,
                null, null, null, null,null,
                CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME);
    }

    public void refreshData(){
        Cursor cursor = getAllData();
        ArrayList<BolusTimeBlockData> bolusTimeBlockDataAL = new ArrayList<BolusTimeBlockData>();
        if (cursor.moveToFirst()){
            do {
                BolusTimeBlockData bolusTableData = new BolusTimeBlockData();
                bolusTableData.id = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry._ID));
                bolusTableData.start = cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME));
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
