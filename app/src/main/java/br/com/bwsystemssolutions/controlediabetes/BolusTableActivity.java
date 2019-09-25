package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTableAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.util.Filters;


public class BolusTableActivity extends AppCompatActivity {

    RecyclerView mBolusRecyclerView;
    RecyclerView mGlucoseRecyclerView;
    BolusTableAdapter mBolusTableAdapter;
    RecyclerView.OnScrollListener[] scrollListeners = new RecyclerView.OnScrollListener[2];

    public static final int TAG_RECYCLERVIEW_GLUCOSE = 0;
    public static final int TAG_RECYCLERVIEW_BOLUS = 1;

    public static final String TAG = "bwvm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_table);

        mBolusRecyclerView = findViewById(R.id.rv_bolus_table_bolus);
        mBolusRecyclerView.setTag(TAG_RECYCLERVIEW_BOLUS);
        mGlucoseRecyclerView = findViewById(R.id.rv_bolus_table_glucose);
        mGlucoseRecyclerView.setTag(TAG_RECYCLERVIEW_GLUCOSE);
        configureRecyclerView();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: iniciando o on start");
        super.onStart();
        Log.d(TAG, "onStart: acionando o refresh");
        mBolusTableAdapter.refreshData();
    }

    private void configureRecyclerView(){
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mBolusTableAdapter = new BolusTableAdapter(this,dbHelper, clickHandler());
        mBolusRecyclerView.setAdapter(mBolusTableAdapter);
        mGlucoseRecyclerView.setAdapter(mBolusTableAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mBolusRecyclerView.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mGlucoseRecyclerView.setLayoutManager(linearLayoutManager2);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mBolusRecyclerView.setHasFixedSize(true);
        mGlucoseRecyclerView.setHasFixedSize(true);

        configureScrollListeners();
    }

    public BolusTableAdapter.BolusTableAdapterOnClickHandler clickHandler(){

        final BolusTableAdapter.BolusTableAdapterOnClickHandler handler = new BolusTableAdapter.BolusTableAdapterOnClickHandler() {
            @Override
            public void onClick(BolusTableData bolusTableData, int itemSelected) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BolusTableActivity.this);
                builder.setTitle("Alterar valor do Bolus")
                        .setMessage("Digite o novo valor");

                final EditText editText = new EditText(BolusTableActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setFilters(new InputFilter[] {Filters.DecimalDigits(3, 1)});
                builder.setView(editText);
                builder.setPositiveButton("OK", null);
                builder.setNegativeButton("Cancelar", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(int selectedItem) {

            }
        };
        return handler;
    }

    private void configureScrollListeners(){
        scrollListeners[0] = new RecyclerView.OnScrollListener( )
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                mBolusRecyclerView.removeOnScrollListener(scrollListeners[1]);
                mBolusRecyclerView.scrollBy(dx, dy);
                mBolusRecyclerView.addOnScrollListener(scrollListeners[1]);
            }
        };
        scrollListeners[1] = new RecyclerView.OnScrollListener( )
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                mGlucoseRecyclerView.removeOnScrollListener(scrollListeners[0]);
                mGlucoseRecyclerView.scrollBy(dx, dy);
                mGlucoseRecyclerView.addOnScrollListener(scrollListeners[0]);
            }
        };
        //mGlucoseRecyclerView.addOnScrollListener(scrollListeners[0]);  //se colocar este agora, dá erro quando rotaciona. (o recyclerview do bolus perde um item)
        mBolusRecyclerView.addOnScrollListener(scrollListeners[1]);
    }





}
