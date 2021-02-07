package br.com.bwsystemssolutions.controlediabetes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTable3Data;
import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusDAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.MealDAO;
import br.com.bwsystemssolutions.controlediabetes.util.Alert;
import br.com.bwsystemssolutions.controlediabetes.util.Converter;
import br.com.bwsystemssolutions.controlediabetes.util.Filters;

public class BolusDetailActivity extends AppCompatActivity {

    private BolusTable3Data mBolusTable3Data;
    private BolusDAO mBolusDAO;
    private boolean mEditAction = false;

    private EditText mGlucoseEditText;
    private EditText mBreakFastEditText; //café da manhã
    private EditText mBrunchEditText;    // colação
    private EditText mLunchEditText;     //almoço
    private EditText mTeaEditText;       //lanche da tarde
    private EditText mDinnerEditText;    //jantar
    private EditText mSupperEditText;    //ceia
    private EditText mDawnEditText;      //madrugada

    private final String TAG = "bwvm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_detail);

        //Verifica se veio objeto e set a variavel que informa que trata-se de edição ou não.
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(BolusTable3Data.BUNDLE_STRING_KEY)){
            Bundle bundle = intentThatStartedThisActivity.getExtras();
            mBolusTable3Data = (BolusTable3Data) bundle.getSerializable(BolusTable3Data.BUNDLE_STRING_KEY);
            mEditAction = true;
        }

        mBolusDAO = new BolusDAO(this);

        initComponents();
        setFilters();
        fillComponents();
    }

    private void setFilters() {
        final InputFilter[] inputFilter = {Filters.DecimalDigits(3, 2)};
        //mGlucoseEditText.setFilters(inputFilter);
        mBreakFastEditText.setFilters(inputFilter);
        mBrunchEditText.setFilters(inputFilter);
        mLunchEditText.setFilters(inputFilter);
        mTeaEditText.setFilters(inputFilter);
        mDinnerEditText.setFilters(inputFilter);
        mSupperEditText.setFilters(inputFilter);
        mDawnEditText.setFilters(inputFilter);
    }

    private void initComponents() {
        mGlucoseEditText = findViewById(R.id.et_glicemia);
        mBreakFastEditText = findViewById(R.id.et_cafe_da_manha);
        mBrunchEditText = findViewById(R.id.et_colacao);
        mLunchEditText = findViewById(R.id.et_almoco);
        mTeaEditText = findViewById(R.id.et_lanche);
        mDinnerEditText = findViewById(R.id.et_jantar);
        mSupperEditText = findViewById(R.id.et_ceia);
        mDawnEditText = findViewById(R.id.et_madrugada);
    }

    private void fillComponents() {
        if (mBolusTable3Data == null) return;

        mGlucoseEditText.setText(String.valueOf(mBolusTable3Data.getGlucose()));
        mBreakFastEditText.setText(String.valueOf(mBolusTable3Data.getBolus1CafeDaManha().getBolus()));
        mBrunchEditText.setText(String.valueOf(mBolusTable3Data.getBolus2Colacao().getBolus()));
        mLunchEditText.setText(String.valueOf(mBolusTable3Data.getBolus3Almoco().getBolus()));
        mTeaEditText.setText(String.valueOf(mBolusTable3Data.getBolus4Lanche().getBolus()));
        mDinnerEditText.setText(String.valueOf(mBolusTable3Data.getBolus5Jantar().getBolus()));
        mSupperEditText.setText(String.valueOf(mBolusTable3Data.getBolus6Ceia().getBolus()));
        mDawnEditText.setText(String.valueOf(mBolusTable3Data.getBolus7Madrugada().getBolus()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_time_block_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_save:
                if (mEditAction) {
                    update();
                } else {
                    save();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DialogInterface.OnClickListener replaceClick(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fillObjectWithActivityData(mBolusTable3Data);
                ArrayList<Bolus> bolusArrayList =  mBolusTable3Data.getBolusArrayList();
                final boolean updated = mBolusDAO.update(bolusArrayList);
                if (updated){
                    Toast.makeText(BolusDetailActivity.this,"Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BolusDetailActivity.this,"Alteração não realizada!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        };
    }

    private void save(){
        ArrayList<Bolus> bolusArrayList = mBolusDAO.fetchByGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        if (bolusArrayList != null) {
            mBolusTable3Data = new BolusTable3Data();
            mBolusTable3Data.setBolusArrayList(bolusArrayList);
        }
        //Se já existir a glicemia
        if (mBolusTable3Data != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Atenção!")
                    .setMessage("Glicemia informada já existe.\nDeseja substituir?")
                    .setPositiveButton("Substituir", replaceClick())//new DialogInterface.OnClickListener() {
                    .setNegativeButton("Cancelar", null)
                    .create()
                    .show();

        } else {
            mBolusTable3Data = new BolusTable3Data();
            fillObjectWithActivityData(mBolusTable3Data);
            bolusArrayList = mBolusTable3Data.getBolusArrayList();
            mBolusDAO.add(bolusArrayList);
            Toast.makeText(BolusDetailActivity.this,"Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean update(){


        int glucose = Converter.toInt(mGlucoseEditText.getText().toString());

        if (glucose != mBolusTable3Data.getGlucose() && existsGlucose(glucose)) {
            Alert.ok(this,"Atenção!",
                    "Este valor de glicemia já está configurado.\nUtilize outro valor de glicemia que ainda não foi utilizado.", "Ok", null).show();
            return false;
        }

        fillObjectWithActivityData(mBolusTable3Data);
        ArrayList<Bolus> bolusArrayList = mBolusTable3Data.getBolusArrayList();
        boolean updated = mBolusDAO.update(bolusArrayList);
        if (updated) {
            Toast.makeText(BolusDetailActivity.this,"Editado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(BolusDetailActivity.this,"Não foi possível editar!", Toast.LENGTH_SHORT).show();
        }
        return updated;
    }

    private boolean existsGlucose(int glucose){
        return mBolusDAO.fetchByGlucose(glucose) != null;
    }

    private void fillObjectWithActivityData(BolusTable3Data bolusTable3Data) {
        MealDAO mealDAO = new MealDAO(this);
        ArrayList<Meal> meals = mealDAO.fetchAll();

        Bolus bolusCafeDaManha = new Bolus();
        bolusCafeDaManha.setMeal_id(1);
        bolusCafeDaManha.setMeal(meals.get(0).getMeal());
        bolusCafeDaManha.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusCafeDaManha.setBolus(Converter.toDouble(mBreakFastEditText.getText().toString()));

        Bolus bolusColacao = new Bolus();
        bolusColacao.setMeal_id(2);
        bolusColacao.setMeal(meals.get(1).getMeal());
        bolusColacao.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusColacao.setBolus(Converter.toDouble(mBrunchEditText.getText().toString()));

        Bolus bolusAlmoco = new Bolus();
        bolusAlmoco.setMeal_id(3);
        bolusAlmoco.setMeal(meals.get(2).getMeal());
        bolusAlmoco.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusAlmoco.setBolus(Converter.toDouble(mLunchEditText.getText().toString()));

        Bolus bolusLanche = new Bolus();
        bolusLanche.setMeal_id(4);
        bolusLanche.setMeal(meals.get(3).getMeal());
        bolusLanche.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusLanche.setBolus(Converter.toDouble(mTeaEditText.getText().toString()));

        Bolus bolusJantar = new Bolus();
        bolusLanche.setMeal_id(5);
        bolusLanche.setMeal(meals.get(4).getMeal());
        bolusLanche.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusLanche.setBolus(Converter.toDouble(mDinnerEditText.getText().toString()));

        Bolus bolusCeia = new Bolus();
        bolusLanche.setMeal_id(6);
        bolusLanche.setMeal(meals.get(5).getMeal());
        bolusLanche.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusLanche.setBolus(Converter.toDouble(mSupperEditText.getText().toString()));

        Bolus bolusMadrugada = new Bolus();
        bolusLanche.setMeal_id(7);
        bolusLanche.setMeal(meals.get(6).getMeal());
        bolusLanche.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusLanche.setBolus(Converter.toDouble(mDawnEditText.getText().toString()));


        bolusTable3Data.setGlucose(Converter.toInt(mGlucoseEditText.getText().toString()));
        bolusTable3Data.setBolus1CafeDaManha(bolusCafeDaManha);
        bolusTable3Data.setBolus2Colacao(bolusColacao);
        bolusTable3Data.setBolus3Almoco(bolusAlmoco);
        bolusTable3Data.setBolus4Lanche(bolusLanche);
        bolusTable3Data.setBolus5Jantar(bolusJantar);
        bolusTable3Data.setBolus6Ceia(bolusCeia);
        bolusTable3Data.setBolus7Madrugada(bolusMadrugada);
    }

}
