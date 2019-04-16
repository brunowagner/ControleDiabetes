package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTimeBlockAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

import static br.com.bwsystemssolutions.controlediabetes.adapter.BolusTimeBlockAdapter.*;
import static br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract.*;

public class BolusCalculateConfig extends AppCompatActivity implements BolusTimeBlockAdapterOnClickHandler {

    BolusTimeBlockAdapter mBolusTimeBlockAdapter;
    SQLiteDatabase mDb;
    RecyclerView mRecyclerView;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_calcular_bolus);

        configureDB();

        mRecyclerView = findViewById(R.id.rv_dados_para_calculo);
        configureRecyclerView();

        mCursor = getAllData();
        refreshRecyclerView();
    }

    //implementação do BolusTimeBlockAdapterOnClickHandler
    @Override
    public void onClick(BolusTimeBlockData bolusTimeBlockData) {
        Context context = this;
        Intent intent = new Intent(context, TimeBlockConfigActivity.class);

        //Empacotando o objeto
        Bundle bundle = new Bundle();
        bundle.putSerializable(BolusTimeBlockData.BUNDLE_STRING_KEY, bolusTimeBlockData);

        //passando o objeto na Intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void refreshRecyclerView(){
        ArrayList<BolusTimeBlockData> bolusTimeBlockDataAL = new ArrayList<BolusTimeBlockData>();
        if (mCursor.moveToFirst()){
            do {
                BolusTimeBlockData bolusTimeBlockData = new BolusTimeBlockData();
                bolusTimeBlockData.start = mCursor.getString(mCursor.getColumnIndex(TimeBlockEntry.COLUMN_INITIAL_TIME_NAME));
                bolusTimeBlockData.relation = mCursor.getInt(mCursor.getColumnIndex(TimeBlockEntry.COLUMN_RELATION_NAME));
                bolusTimeBlockData.sensibilityFactor = mCursor.getInt(mCursor.getColumnIndex(TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME));
                bolusTimeBlockData.tarjet = mCursor.getInt(mCursor.getColumnIndex(TimeBlockEntry.COLUMN_TARGET_NAME));

                bolusTimeBlockDataAL.add(bolusTimeBlockData);
            }while(mCursor.moveToNext());

        }
        mBolusTimeBlockAdapter.setBolusBlockTimeData(bolusTimeBlockDataAL);
    }

    private Cursor getAllData() {
        return mDb.query(TimeBlockEntry.TABLE_NAME,
                null, null, null, null,null,
                TimeBlockEntry.COLUMN_INITIAL_TIME_NAME);
    }

    private void configureDB() {
        // Configurando o banco de dados
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void configureRecyclerView(){
        mBolusTimeBlockAdapter = new BolusTimeBlockAdapter(this);
        mRecyclerView.setAdapter(mBolusTimeBlockAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
    }
}
