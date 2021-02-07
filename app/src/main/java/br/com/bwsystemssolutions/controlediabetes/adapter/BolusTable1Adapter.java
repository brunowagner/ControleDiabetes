package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.bwsystemssolutions.controlediabetes.BolusTableActivity;
import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.Bolus;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTable3Data;
import br.com.bwsystemssolutions.controlediabetes.classe.Meal;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusDAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusTableData2DAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.MealDAO;

public class BolusTable1Adapter extends RecyclerView.Adapter<BolusTable1Adapter.BolusTable1AdapterViewHolder> implements View.OnClickListener {

    private static final String TAG = "bwvm";
    Cursor mCursor;
    private SQLiteDatabase mDb;
    private CalculoDeBolusDBHelper dbHelper;
    //private ArrayList<Bolus> mBolusTableData;
    private ArrayList<BolusTable3Data> mBolusTableData;
    private Context context;
    private final BolusTable1AdapterOnClickHandler mClickHandler;
    private int mSelectedItem = ITEN_SELECT_NONE;
    private int mClickedItem;
    private HashMap<Integer, Integer> mSelectedItems;
    private Bolus bolusClicked;

    public static final int ITEN_SELECT_NONE = -1;


    public BolusTable1Adapter(Context context, CalculoDeBolusDBHelper dbHelper, BolusTable1AdapterOnClickHandler clickHandler){
        this.context = context;
        this.dbHelper = dbHelper;
        this.mDb = dbHelper.getWritableDatabase();
        this.mClickHandler = clickHandler;
        this.mSelectedItems = new HashMap<>();

        //TODO 2021 - este artifício é apenas para teste e precisa ser removido quando em produção.
        final String SQL_CREATE_BOLUS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                CalculoDeBolusContract.BolusEntry.TABLE_NAME + "(" +
                CalculoDeBolusContract.BolusEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CalculoDeBolusContract.BolusEntry.COLUMN_GLUCOSE_NAME + " INTEGER NOT NULL," +
                CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_ID_NAME + " INTEGER NOT NULL," +
                CalculoDeBolusContract.BolusEntry.COLUMN_MEAL_NAME + " TEXT NOT NULL," +
                CalculoDeBolusContract.BolusEntry.COLUMN_BOLUS_NAME + " REAL NOT NULL" +
                ");";

        mDb.execSQL(SQL_CREATE_BOLUS_TABLE);

        Log.d(TAG, "BolusTableAdapter: antes da consulta ");

        BolusDAO bolusDAO = new BolusDAO(context);
        ArrayList<Bolus> bolusArrayList = bolusDAO.fetchAll();

        Log.d(TAG, "BolusTableAdapter: quantidade de registros retornados: " + bolusArrayList.size());

        // se não hover conteúdo na tabela de bolus, um conteúdo será gerado.
        if (bolusArrayList.size() == 0) {

            Meal meal = new Meal();
            MealDAO mealDAO = new MealDAO(context);
            final ArrayList<Meal> meals = mealDAO.fetchAll();

            int glicose = 60;
            double insulina = 1.0;
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < meals.size(); i++) {
                    Bolus bolus = new Bolus();
                    bolus.setGlucose(glicose);
                    bolus.setMeal_id(meals.get(i).getId());
                    bolus.setMeal(meals.get(i).getMeal());
                    bolus.setBolus(insulina);

                    bolusArrayList.add(bolus);
                }
                glicose += 60;
                insulina += 1.0;
            }
            bolusDAO.add(bolusArrayList);
        }
    }

    @NonNull
    @Override
    public BolusTable1AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        int layoutIdFromListItem = (int)viewGroup.getTag();
        Log.d(TAG, "onCreateViewHolder: TAG do viewgroup(RecyclerView) = " + layoutIdFromListItem);


        int recyclerViewTag = (int) viewGroup.getTag();

        if (recyclerViewTag == BolusTableActivity.TAG_RECYCLERVIEW_GLUCOSE){
            layoutIdFromListItem = R.layout.list_item_row_bolus_table_glucose;
            Log.d("bwvm", "onCreateViewHolder: TAG = list_item_row_bolus_table_glucose" );
        } else if (recyclerViewTag == BolusTableActivity.TAG_RECYCLERVIEW_BOLUS){
            layoutIdFromListItem = R.layout.list_item_row_bolus_table_bolus;
            Log.d("bwvm", "onCreateViewHolder: TAG = list_item_row_bolus_table_bolus" );
        }


        if (layoutIdFromListItem == 0 ) Log.d("bwvm", "onCreateViewHolder: parent não é RV");

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
        return new BolusTable1AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BolusTable1AdapterViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: position: " + position);
        //Bolus bolus = mBolusTableData.get(position);
        BolusTable3Data bolusTable3Data = mBolusTableData.get(position);

        viewHolder.itemView.setTag(bolusTable3Data.getId());

        if (mSelectedItems.containsKey(position)){
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }

        if (null != viewHolder.mGlucose){
            viewHolder.mGlucose.setText(String.valueOf(bolusTable3Data.getGlucose()));
            return;
        }

        viewHolder.mInsul1.setText(String.valueOf(bolusTable3Data.getBolus1CafeDaManha().getBolus()));
        viewHolder.mInsul2.setText(String.valueOf(bolusTable3Data.getBolus2Colacao().getBolus()));
        viewHolder.mInsul3.setText(String.valueOf(bolusTable3Data.getBolus3Almoco().getBolus()));
        viewHolder.mInsul4.setText(String.valueOf(bolusTable3Data.getBolus4Lanche().getBolus()));
        viewHolder.mInsul5.setText(String.valueOf(bolusTable3Data.getBolus5Jantar().getBolus()));
        viewHolder.mInsul6.setText(String.valueOf(bolusTable3Data.getBolus6Ceia().getBolus()));
        viewHolder.mInsul7.setText(String.valueOf(bolusTable3Data.getBolus7Madrugada().getBolus()));

        viewHolder.mCard1.setTag(bolusTable3Data.getBolus1CafeDaManha().getBolus());
        viewHolder.mCard2.setTag(bolusTable3Data.getBolus2Colacao().getBolus());
        viewHolder.mCard3.setTag(bolusTable3Data.getBolus3Almoco().getBolus());
        viewHolder.mCard4.setTag(bolusTable3Data.getBolus4Lanche().getBolus());
        viewHolder.mCard5.setTag(bolusTable3Data.getBolus5Jantar().getBolus());
        viewHolder.mCard6.setTag(bolusTable3Data.getBolus6Ceia().getBolus());
        viewHolder.mCard7.setTag(bolusTable3Data.getBolus7Madrugada().getBolus());


//        viewHolder.mCard1.setTag(new FieldId(bolusTable3Data.getBolus1CafeDaManha().getId(),bolusTable3Data.getBolus1CafeDaManha().getMeal()));
//        viewHolder.mCard2.setTag(new FieldId(bolusTable3Data.getBolus2Colacao().getId(),bolusTable3Data.getBolus2Colacao().getMeal()));
//        viewHolder.mCard3.setTag(new FieldId(bolusTable3Data.getBolus3Almoco().getId(),bolusTable3Data.getBolus3Almoco().getMeal()));
//        viewHolder.mCard4.setTag(new FieldId(bolusTable3Data.getBolus4Lanche().getId(),bolusTable3Data.getBolus4Lanche().getMeal()));
//        viewHolder.mCard5.setTag(new FieldId(bolusTable3Data.getBolus5Jantar().getId(),bolusTable3Data.getBolus5Jantar().getMeal()));
//        viewHolder.mCard6.setTag(new FieldId(bolusTable3Data.getBolus6Ceia().getId(),bolusTable3Data.getBolus6Ceia().getMeal()));
//        viewHolder.mCard7.setTag(new FieldId(bolusTable3Data.getBolus7Madrugada().getId(),bolusTable3Data.getBolus7Madrugada().getMeal()));
    }

    @Override
    public int getItemCount() {
        if (null == mBolusTableData) return 0;
        return mBolusTableData.size();

    }

    @Override
    public void onViewAttachedToWindow(@NonNull BolusTable1AdapterViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        String text1 = holder.mGlucose == null ? "null" : String.valueOf(holder.mGlucose.getText());
        String text2 = holder.mInsul1 == null ? "null" : String.valueOf(holder.mInsul1.getText());

        Log.d(TAG, "onViewAttachedToWindow: Texto do Textview Glucose: " + text1);
        Log.d(TAG, "onViewAttachedToWindow: Texto do Textview Insulin1: " + text2);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: CardView Clicado = " + view.getId());
    }



    public class BolusTable1AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

//        public final TextView mGlucoseTextView;
//        public final RecyclerView mBolusRecyclerView;
        public  CardView mCard1;
        public  CardView mCard2;
        public  CardView mCard3;
        public  CardView mCard4;
        public  CardView mCard5;
        public  CardView mCard6;
        public  CardView mCard7;

        public  TextView mInsul1;
        public  TextView mInsul2;
        public  TextView mInsul3;
        public  TextView mInsul4;
        public  TextView mInsul5;
        public  TextView mInsul6;
        public  TextView mInsul7;

        public TextView mGlucose;

        public BolusTable1AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mGlucose = itemView.findViewById(R.id.tv_item_bolus_table_glucose);

            if (null != mGlucose) {
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                return;
            }

            mCard1 = itemView.findViewById(R.id.cv_1);
            mCard2 = itemView.findViewById(R.id.cv_2);
            mCard3 = itemView.findViewById(R.id.cv_3);
            mCard4 = itemView.findViewById(R.id.cv_4);
            mCard5 = itemView.findViewById(R.id.cv_5);
            mCard6 = itemView.findViewById(R.id.cv_6);
            mCard7 = itemView.findViewById(R.id.cv_7);

            mInsul1 = itemView.findViewById(R.id.tv_insulin_1);
            mInsul2 = itemView.findViewById(R.id.tv_insulin_2);
            mInsul3 = itemView.findViewById(R.id.tv_insulin_3);
            mInsul4 = itemView.findViewById(R.id.tv_insulin_4);
            mInsul5 = itemView.findViewById(R.id.tv_insulin_5);
            mInsul6 = itemView.findViewById(R.id.tv_insulin_6);
            mInsul7 = itemView.findViewById(R.id.tv_insulin_7);

            mCard1.setOnClickListener(this);
            mCard2.setOnClickListener(this);
            mCard3.setOnClickListener(this);
            mCard4.setOnClickListener(this);
            mCard5.setOnClickListener(this);
            mCard6.setOnClickListener(this);
            mCard7.setOnClickListener(this);

            mCard1.setOnLongClickListener(this);
            mCard2.setOnLongClickListener(this);
            mCard3.setOnLongClickListener(this);
            mCard4.setOnLongClickListener(this);
            mCard5.setOnLongClickListener(this);
            mCard6.setOnLongClickListener(this);
            mCard7.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickedItem = position;
            bolusClicked = null;
            if (mSelectedItems.size() > 0) {
                if (mSelectedItems.containsKey(position)) {
                    mSelectedItems.remove(position);
                } else {
                    mSelectedItems.put(position, position);
                }
                mSelectedItem = ITEN_SELECT_NONE;
                notifyDataSetChanged();
                mClickHandler.onClick(null, mClickedItem, mSelectedItems.size(), null, null);
                return;
            }

            if (v instanceof CardView == false){
                return;
            }

            CardView cardView = (CardView)v;
            LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);
            TextView textView = (TextView) linearLayout.getChildAt(0);
            String mealName = textView.getText().toString();
            bolusClicked = (Bolus) cardView.getTag();
            mSelectedItem = position;

            BolusTable3Data bolusTable3Data = mBolusTableData.get(position);
            mClickHandler.onClick(bolusTable3Data, mClickedItem, 0, mealName, bolusClicked);
        }

        @Override
        public boolean onLongClick(View v) {
            int clickedItem = getAdapterPosition();
            mSelectedItem = ITEN_SELECT_NONE;
            bolusClicked = null;
            if (mSelectedItems.containsKey(clickedItem)){
                mSelectedItems.remove(clickedItem);
            } else {
                mSelectedItems.put(clickedItem,clickedItem);
            }
            notifyDataSetChanged();
            BolusTable3Data bolusTable3Data = null;
            if (mSelectedItems.size() == 1){
                bolusTable3Data = mBolusTableData.get(clickedItem);
                mSelectedItem = clickedItem;
            }
            mClickHandler.onLongClick(mSelectedItems, bolusTable3Data);

            return true;
        }
    }


    // ---------------------------------- Functions ---------------------------------------------

    public void setBolusTableData(ArrayList<BolusTable3Data> bolusTable3Data){
        Log.d(TAG, "setBolusBlockTimeData: ");
        mBolusTableData = bolusTable3Data;
        notifyDataSetChanged();
    }


    private ArrayList<BolusTable3Data> parseBolusToBolusTable3Data(ArrayList<Bolus> bolusArrayList){
        ArrayList<BolusTable3Data> bolusTable3Datas = new ArrayList<>();
        BolusTable3Data bolusTable3Data = new BolusTable3Data();
        //int id = 0;

        int previousGlucose = 0;

        for (Bolus b : bolusArrayList){

            //se a glicose for diferente da anterior entao cria-se novo objeto
            if (previousGlucose != b.getGlucose()) {
                bolusTable3Data = new BolusTable3Data();
                //id ++;
                bolusTable3Data.setId(b.getGlucose());
                bolusTable3Datas.add(bolusTable3Data);
            }

            bolusTable3Data.addBolusIds(b.getId());
            bolusTable3Data.setGlucose(b.getGlucose());

            switch (b.getMeal_id()){
                case 1: bolusTable3Data.setBolus1CafeDaManha(b);
                    break;
                case 2: bolusTable3Data.setBolus2Colacao(b);
                    break;
                case 3: bolusTable3Data.setBolus3Almoco(b);
                    break;
                case 4: bolusTable3Data.setBolus4Lanche(b);
                    break;
                case 5: bolusTable3Data.setBolus5Jantar(b);
                    break;
                case 6: bolusTable3Data.setBolus6Ceia(b);
                    break;
                case 7: bolusTable3Data.setBolus7Madrugada(b);
                default:
            }

            //bolusTable3Datas.add(bolusTable3Data);

            previousGlucose = b.getGlucose();
        }

        return bolusTable3Datas;
    }



    // ---------------------------------- Data Base ---------------------------------------------

    private Cursor getAllData() {
        String sqlQuery = "SELECT * FROM " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME;//CalculoDeBolusContract.BolusTableQuery.FETCH_ALL_DATA;
        Log.d(TAG, "getAllData: sqlQuery: " + sqlQuery);

        Cursor c = mDb.rawQuery(sqlQuery, null);

        return c;
    }

    public void refreshData(){
        BolusDAO bolusDAO = new BolusDAO(context);
        ArrayList<Bolus> bolusArrayList = bolusDAO.fetchAll();
        ArrayList<BolusTable3Data> bolusTable3Data = parseBolusToBolusTable3Data(bolusArrayList);
        setBolusTableData(bolusTable3Data);
    }

    public void selectItem(int item){
        mSelectedItems.put(item, item);
        notifyDataSetChanged();
    }

    public void unselectItem(int item){
        mSelectedItems.remove(item);
        notifyDataSetChanged();
    }
    public void unselectAllItems(){
        if (mSelectedItems == null || mSelectedItems.size() <= 0){
            return;
        }
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public int deleteSelectedItems() {
        BolusTableData2DAO bolusTableData2DAO = new BolusTableData2DAO(context);
        HashMap<Integer,Integer> selecteds = new HashMap<>(mSelectedItems);
        int cont = 0;
        for (Map.Entry<Integer,Integer> item : selecteds.entrySet()){
            final int id = mBolusTableData.get(item.getKey()).getId();
            if (bolusTableData2DAO.delete(id)) {
                mSelectedItems.remove(item.getKey());
                cont+=1;
                Log.d(TAG, "deleteSelectedItems: qtd selecionados: " + mSelectedItems.size());
            }
        }
        notifyDataSetChanged();
        return cont;
    }

//    public boolean updateClickedItem(String fieldName, Double insuline){
//        if (fieldName == null){
//            Log.d(TAG, "updateClickedItem: fieldName = null");
//            return false;
//        }
//        if (mSelectedItems.size() != 1) {
//            Log.d(TAG, "updateClickedItem: mselectedItens diferente de 1");
//            return false;
//        }
//        BolusTableData2DAO bolusTableDataDAO = new BolusTableData2DAO(context);
//        return bolusTableDataDAO.updateInsulineField(mBolusTableData.get(mClickedItem),fieldName,insuline);
//    }

    public boolean updateItem(BolusTable3Data bolusTable3Data, Bolus bolus, Double insuline){
        int id = bolusTable3Data.getId();
        if (id <= 0){
            Log.d("Error", "updateItem: Item have no id.");
            return false;
        }

        BolusTableData2DAO bolusTableData2DAO = new BolusTableData2DAO(context);
        BolusDAO bolusDAO = new BolusDAO(context);

        boolean updated = bolusDAO.updateInsulineField(bolusTable3Data,bolus,insuline);
        if (updated) refreshData();
        return updated;
    }

    public class FieldId{
        private int id;
        private String columnName;

        public FieldId(int id, String columnName) {
            this.id = id;
            this.columnName = columnName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

    }

    public interface BolusTable1AdapterOnClickHandler {
        void onClick(BolusTable3Data bolusTable3Data, int clickedItem, int SelectedItems, String mealName, Bolus bolus);
        void onLongClick(HashMap<Integer, Integer> selectedItens, BolusTable3Data bolusTable3Data);
    }



}
