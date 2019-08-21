package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.time.Instant;
import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusTableAdapter extends RecyclerView.Adapter<BolusTableAdapter.BolusTableAdapterViewHolder> {

    Cursor mCursor;
    private SQLiteDatabase mDb;
    private CalculoDeBolusDBHelper dbHelper;
    private ArrayList<BolusTableData> mBolusTableData;

    public BolusTableAdapter (CalculoDeBolusDBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public BolusTableAdapter.BolusTableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BolusTableAdapter.BolusTableAdapterViewHolder bolusTableAdapterViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class BolusTableAdapterViewHolder extends RecyclerView.ViewHolder {
        public BolusTableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);


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
