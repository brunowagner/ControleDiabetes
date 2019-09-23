package br.com.bwsystemssolutions.controlediabetes;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTableAdapter;
import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTimeBlockAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.util.Filters;


public class BolusTableActivity extends AppCompatActivity {

    RecyclerView mParentRecyclerView;
    BolusTableAdapter mBolusTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_table);

        mParentRecyclerView = findViewById(R.id.rv_bolus_table_glucose);
        configureRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBolusTableAdapter.refreshData();
    }

    private void configureRecyclerView(){
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mBolusTableAdapter = new BolusTableAdapter(this,dbHelper, clickHandler(), mParentRecyclerView);
        mParentRecyclerView.setAdapter(mBolusTableAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mParentRecyclerView.setLayoutManager(linearLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mParentRecyclerView.setHasFixedSize(true);
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


}
