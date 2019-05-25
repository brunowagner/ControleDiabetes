package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.bwsystemssolutions.controlediabetes.adapter.RecordAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class RegistrosActivity extends AppCompatActivity implements RecordAdapter.RecordAdapterOnClickHandler {
    RecyclerView mRegistrosRecyclerView;
    RecordAdapter mRegistrosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        mRegistrosRecyclerView = (RecyclerView) findViewById(R.id.rv_registros);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRegistrosRecyclerView.setLayoutManager(linearLayoutManager);

        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mRegistrosAdapter= new RecordAdapter(dbHelper,this);

        mRegistrosRecyclerView.setAdapter(mRegistrosAdapter);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRegistrosRecyclerView.getContext(),
        //        linearLayoutManager.getOrientation());

        //mRegistrosRecyclerView.addItemDecoration(dividerItemDecoration);

        mRegistrosRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecyclerView();
    }

    private void refreshRecyclerView(){
        mRegistrosAdapter.refreshData();
    }
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_records_operation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_record_add:
				Context context = this;
                Intent intent = new Intent(context, RecordDetailActivity.class);
                startActivity(intent);
                return true;

            default:
                super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
	
	//implementação do RecordAdapterOnClickHandler
    @Override
    public void onClick(Record record) {
        Context context = this;
        Intent intent = new Intent(context, RecordDetailActivity.class);

        //Empacotando o objeto
        Bundle bundle = new Bundle();
        bundle.putSerializable(Record.BUNDLE_STRING_KEY, record);

        //passando o objeto na Intent
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
