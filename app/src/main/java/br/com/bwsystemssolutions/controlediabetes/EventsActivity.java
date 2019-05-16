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

import br.com.bwsystemssolutions.controlediabetes.adapter.EventAdapter;
import br.com.bwsystemssolutions.controlediabetes.adapter.RecordAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.Event;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class EventsActivity extends AppCompatActivity implements EventAdapter.EventAdapterOnClickHandler {

    RecyclerView mEventsRecyclerView;
    EventAdapter mEventAdapter;

    // - life cycle -----------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mEventsRecyclerView = findViewById(R.id.rv_events);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mEventsRecyclerView.setLayoutManager(linearLayoutManager);

        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mEventAdapter= new EventAdapter(dbHelper,this);

        mEventsRecyclerView.setAdapter(mEventAdapter);

        mEventsRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecyclerView();
    }

    // - Methods ------------------------------------------------------------------------------------------

    private void refreshRecyclerView(){
        mEventAdapter.refreshData();
    }

    // - Implementations -----------------------------------------------------------------------------------
    // EventAdapter.EventAdapterOnClickHandler
    @Override
    public void onClick(Event event) {
        // TODO codificar evento do clique
    }

    // - Menu ---------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_save_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_save:
                Context context = this;
                Intent intent = new Intent(context, RecordDetailActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_delete:
                // TODO codificar deleção
                return true;

            default:
                super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
