package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTimeBlockAdapter;
import br.com.bwsystemssolutions.controlediabetes.adapter.RegistrosAdapter;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class RegistrosActivity extends AppCompatActivity {
    RecyclerView mRegistrosRecyclerView;
    RegistrosAdapter mRegistrosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        mRegistrosRecyclerView = (RecyclerView) findViewById(R.id.rv_registros);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRegistrosRecyclerView.setLayoutManager(linearLayoutManager);

        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mRegistrosAdapter= new RegistrosAdapter(dbHelper);

        mRegistrosRecyclerView.setAdapter(mRegistrosAdapter);

        mRegistrosRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecyclerView();
    }

    private void refreshRecyclerView(){
        mRegistrosAdapter.refreshData();                  //migracao do mDB
    }
}
