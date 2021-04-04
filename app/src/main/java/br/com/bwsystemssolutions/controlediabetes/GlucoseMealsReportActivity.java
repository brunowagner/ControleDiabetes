package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.bwsystemssolutions.controlediabetes.adapter.GlucoseMealsReportAdapter;
import br.com.bwsystemssolutions.controlediabetes.adapter.RecordAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.GlucoseMealsReport;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class GlucoseMealsReportActivity extends AppCompatActivity {
    RecyclerView mGlucoseMealsReportRecyclerView;
    GlucoseMealsReportAdapter mGlucoseMealsReportAdapter;
    boolean enableActionDelete = false;
    int mSelectedItem = RecordAdapter.ITEN_SELECT_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        mGlucoseMealsReportRecyclerView = (RecyclerView) findViewById(R.id.rv_registros);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mGlucoseMealsReportRecyclerView.setLayoutManager(linearLayoutManager);

        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        mGlucoseMealsReportAdapter = new GlucoseMealsReportAdapter(sp,this);

        mGlucoseMealsReportRecyclerView.setAdapter(mGlucoseMealsReportAdapter);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRegistrosRecyclerView.getContext(),
        //        linearLayoutManager.getOrientation());

        //mRegistrosRecyclerView.addItemDecoration(dividerItemDecoration);

        mGlucoseMealsReportRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecyclerView();
    }

    private void refreshRecyclerView(){
        mGlucoseMealsReportAdapter.refreshData();
    }

    private void setEnableActionDelete(boolean enable){
        enableActionDelete = enable;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_add_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add:
                Context context = this;
                Intent intent = new Intent(context, RecordDetailActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_delete:
                boolean deleted = mGlucoseMealsReportAdapter.deleteRecord(mSelectedItem);
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
        MenuItem item = menu.findItem(R.id.action_delete);
        item.setEnabled(enableActionDelete);
        item.setVisible(enableActionDelete);
        return true;
    }

    //implementação do RecordAdapterOnClickHandler
//    @Override
//    public void onClick(Record record, int selectedItem) {
//        if (selectedItem >= 0) {
//            //TODO decidir se vai manter
//            //mGlucoseMealsReportAdapter.setSelectedItem(RecordAdapter.ITEN_SELECT_NONE);
//            setEnableActionDelete(false);
//            return;
//        }
//
//
//        Context context = this;
//        Intent intent = new Intent(context, RecordDetailActivity.class);
//
//        //Empacotando o objeto
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Record.BUNDLE_STRING_KEY, record);
//
//        //passando o objeto na Intent
//        intent.putExtras(bundle);
//
//        startActivity(intent);
//    }

//    @Override
//    public void onLongClick(int selectedItem) {
//        mSelectedItem = selectedItem;
//        if (selectedItem != RecordAdapter.ITEN_SELECT_NONE){
//            setEnableActionDelete(true);
//        } else {
//            setEnableActionDelete(false);
//        }
//    }
}
