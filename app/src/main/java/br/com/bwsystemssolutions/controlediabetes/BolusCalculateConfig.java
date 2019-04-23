package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTimeBlockAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

import static br.com.bwsystemssolutions.controlediabetes.adapter.BolusTimeBlockAdapter.*;

public class BolusCalculateConfig extends AppCompatActivity implements BolusTimeBlockAdapterOnClickHandler {

    BolusTimeBlockAdapter mBolusTimeBlockAdapter;
    RecyclerView mRecyclerView;
    boolean enableActionDelete = false;
    int mSelectedItem = BolusTimeBlockAdapter.ITEN_SELECT_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_calcular_bolus);

        mRecyclerView = findViewById(R.id.rv_dados_para_calculo);
        configureRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecyclerView();
    }

    //implementação do BolusTimeBlockAdapterOnClickHandler
    @Override
    public void onClick(BolusTimeBlockData bolusTimeBlockData, int selectedItem) {
        if (selectedItem >= 0) {
            mBolusTimeBlockAdapter.setSelectedItem(mBolusTimeBlockAdapter.ITEN_SELECT_NONE);
            setEnableActionDelete(false);
            return;
        }

        Context context = this;
        Intent intent = new Intent(context, TimeBlockConfigActivity.class);

        //Empacotando o objeto
        Bundle bundle = new Bundle();
        bundle.putSerializable(BolusTimeBlockData.BUNDLE_STRING_KEY, bolusTimeBlockData);

        //passando o objeto na Intent
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onLongClick(int selectedItem) {
        mSelectedItem = selectedItem;
        if (selectedItem != BolusTimeBlockAdapter.ITEN_SELECT_NONE){
            setEnableActionDelete(true);
        } else {
            setEnableActionDelete(false);
        }
    }

    private void refreshRecyclerView(){
        mBolusTimeBlockAdapter.refreshData();                  //migracao do mDB
    }

    private void configureRecyclerView(){
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mBolusTimeBlockAdapter = new BolusTimeBlockAdapter(dbHelper,this);
        mRecyclerView.setAdapter(mBolusTimeBlockAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);
    }

    private void setEnableActionDelete(boolean enable){
        enableActionDelete = enable;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_time_block_operation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_time_block_add:
                Intent intent = new Intent(BolusCalculateConfig.this, TimeBlockConfigActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_time_block_delete:
                boolean deleted = mBolusTimeBlockAdapter.deleteBolusTimeBlockData(mSelectedItem);
                if (deleted){
                    setEnableActionDelete(false);
                    refreshRecyclerView();
                }
                return deleted;

            default:
                super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_time_block_delete);
        item.setEnabled(enableActionDelete);
        return true;
    }
}
