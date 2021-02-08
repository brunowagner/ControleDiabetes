package br.com.bwsystemssolutions.controlediabetes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;

import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTable1Adapter;
import br.com.bwsystemssolutions.controlediabetes.adapter.BolusTableAdapter;
import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTable2Data;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTable3Data;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.util.Converter;
import br.com.bwsystemssolutions.controlediabetes.util.Filters;


public class BolusTableActivity extends AppCompatActivity {

    RecyclerView mBolusRecyclerView;
    RecyclerView mGlucoseRecyclerView;
    BolusTable1Adapter mBolusTableAdapter;
    RecyclerView.OnScrollListener[] scrollListeners = new RecyclerView.OnScrollListener[2];
    boolean mEnableActionDelete = false;
    boolean mEnableActionEdit = false;
    private BolusTable3Data mSelectedItem;
    //private HashMap<Integer,Integer> mSelectedItems;


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
        //this.mSelectedItems = new HashMap<>();
        configureRecyclerView();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: iniciando o on start");
        super.onStart();
        Log.d(TAG, "onStart: acionando o refresh");
        mBolusTableAdapter.refreshData();
    }

    private void refreshRecyclerView(){
        mBolusTableAdapter.refreshData();
    }

    private void resetMenu(){
        mEnableActionDelete = false;
        mEnableActionEdit = false;
        mBolusTableAdapter.unselectAllItems();
        invalidateOptionsMenu();
    }

    private void configureRecyclerView(){
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mBolusTableAdapter = new BolusTable1Adapter(this,dbHelper, clickHandler());
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

    public BolusTable1Adapter.BolusTable1AdapterOnClickHandler clickHandler(){

        final BolusTable1Adapter.BolusTable1AdapterOnClickHandler handler = new BolusTable1Adapter.BolusTable1AdapterOnClickHandler() {
//            @Override
//            public void onClick(final BolusTable3Data bolusTableData, int itemSelected, int selectedItems, final String mealName, final BolusTable1Adapter.FieldId fieldId) {
//
//                if (mEnableActionDelete){
//                    mEnableActionDelete = selectedItems > 0;
//                    mEnableActionEdit = selectedItems == 1;
//                    invalidateOptionsMenu();
//                    return;
//                }
//
//                new AlertDialog.Builder(BolusTableActivity.this)
//                        .setTitle("Glicemia a patir de " + bolusTableData.getGlucose() + "mg/dl")
//                        .setMessage("Deseja editar a insulina do(a) " + mealName + " ?")
//                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                editFast(bolusTableData,mealName,fieldId);
//                            }
//                        })
//                        .setNegativeButton("Não", null)
//                        .create()
//                        .show();
//            }

            @Override
            public void onLongClick(HashMap<Integer, Integer> selectedItems, BolusTable3Data bolusTableData) {
                mEnableActionDelete = selectedItems.size() > 0;
                mEnableActionEdit = selectedItems.size() == 1;
                mSelectedItem = bolusTableData;
                if (bolusTableData == null){
                    Log.d(TAG, "onLongClick: bolus data :" + null );
                } else {
                    Log.d(TAG+2, "onLongClick: bolus data :" + "not null" );
                }
                invalidateOptionsMenu();
            }
        };
        return handler;
    }

//    private void editFast(final BolusTable3Data bolusTable3Data, final String mealName, final BolusTable1Adapter.FieldId fieldId){
//        String glucose = String.valueOf(bolusTable3Data.getGlucose());
//        AlertDialog.Builder builder = new AlertDialog.Builder(BolusTableActivity.this);
//        builder.setTitle("Insira a nova quantidade de insulina (U):");
//
//        FrameLayout container = new FrameLayout(BolusTableActivity.this);
//        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int px = Converter.toPx(BolusTableActivity.this, 30);
//        params.leftMargin = px;
//        params.rightMargin = px;
//
//        final EditText editText = new EditText(BolusTableActivity.this);
//        editText.setLayoutParams(params);
//        container.addView(editText);
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        editText.setFilters(new InputFilter[] {Filters.DecimalDigits(3, 1)});
//        builder.setView(container);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.d(TAG, "onClick: botao ok apertado -------------------------------------");
//                if (editText.getText().length() == 0){
//                    Log.d(TAG, "onClick: edittext e vazio --------------------------------------------------");
//                    return; // n~ao faz nada
//                }
//                Double newValue = Converter.toDouble(editText.getText().toString());
//                boolean updated = mBolusTableAdapter.updateItem(bolusTable3Data, fieldId.getMealId(), newValue);
//                String message = "";
//                message = updated ? "Insulina alterada!" : "Erro ao alterar!";
//                Toast.makeText(BolusTableActivity.this, message, Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("Cancelar", null);
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_iedit_idelete_vadd, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_edit:
                Intent intent1 = new Intent(this, BolusDetailActivity.class);

                //Empacotando o objeto
                Bundle bundle = new Bundle();
                bundle.putSerializable(BolusTable3Data.BUNDLE_STRING_KEY, mSelectedItem);

                //passando o objeto na Intent
                intent1.putExtras(bundle);
                startActivity(intent1);

                resetMenu();
                return true;

            case R.id.action_delete:
                int deleteds = mBolusTableAdapter.deleteSelectedItems();
                if (deleteds <= 0){
                    return super.onOptionsItemSelected(item);
                }
                resetMenu();
                refreshRecyclerView();
                return true;

            case R.id.action_add:
                Intent intent2 = new Intent(this, BolusDetailActivity.class);
                startActivity(intent2);

                resetMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem itemDelete = menu.findItem(R.id.action_delete);
        showMenuItens(itemDelete, mEnableActionDelete);

        final MenuItem itemEdit = menu.findItem(R.id.action_edit);
        showMenuItens(itemEdit,mEnableActionEdit);
        return true;
    }

    private void showMenuItens(MenuItem item, boolean show){
        item.setVisible(show);
        item.setEnabled(show);
    }

}
