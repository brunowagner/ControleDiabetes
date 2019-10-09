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
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.data.dao.BolusTableDataDAO;

public class BolusTableAdapter extends RecyclerView.Adapter<BolusTableAdapter.BolusTableAdapterViewHolder> implements View.OnClickListener {

    private static final String TAG = "bwvm";
    Cursor mCursor;
    private SQLiteDatabase mDb;
    private CalculoDeBolusDBHelper dbHelper;
    private ArrayList<BolusTableData> mBolusTableData;
    private Context context;
    private final BolusTableAdapterOnClickHandler mClickHandler;
    private int mSelectedItem = ITEN_SELECT_NONE;
    private int mClickedItem;
    private HashMap<Integer, Integer> mSelectedItems;
    private FieldId fieldClicked;

    public static final int ITEN_SELECT_NONE = -1;


    public BolusTableAdapter (Context context, CalculoDeBolusDBHelper dbHelper, BolusTableAdapterOnClickHandler clickHandler){
        this.context = context;
        this.dbHelper = dbHelper;
        this. mDb = dbHelper.getWritableDatabase();
        this.mClickHandler = clickHandler;
        this.mSelectedItems = new HashMap<>();

        final String SQL_CREATE_BOLUS_TABLE_2 = "CREATE TABLE IF NOT EXISTS " +
                CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME + "(" +
                CalculoDeBolusContract.BolusTable2Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME + " INTEGER NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME + " REAL NOT NULL," +
                CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME + " REAL NOT NULL" +
                ");";

        mDb.execSQL(SQL_CREATE_BOLUS_TABLE_2);



        Log.d(TAG, "BolusTableAdapter: antes da consulta ");

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME, null);

        Log.d(TAG, "BolusTableAdapter: tamanho do cursor kdjf ajd fklajd jasd fj: " + cursor.getCount());

        if (cursor.getCount() == 0) {

            String insert = "INSERT INTO " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME +
                    "(" + CalculoDeBolusContract.BolusTable2Entry.COLUMN_GLUCOSE_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME +
                    "," + CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME + ") VALUES " +

                    "(60,1.0,1.0,1.5,0.0,2.0,0.0,0.0)," +
                    "(100,1.5,1.5,2.0,0.5,2.5,0.0,0.0)," +
                    "(150,2.0,2.0,2.5,1.0,3.0,0.5,0.0)," +
                    "(200,2.5,2.5,3.0,1.5,3.5,1.0,0.0)," +
                    "(250,3.0,3.0,3.5,2.0,4.0,1.5,0.0)," +
                    "(300,4.0,4.0,4.0,2.5,4.5,2.0,0.0)," +
                    "(400,5.0,5.0,5.0,3.0,5.0,2.5,0.0)," +
                    "(500,6.0,6.0,6.0,3.5,5.5,3.0,0.0);";

            mDb.execSQL(insert);
        }
    }

    @NonNull
    @Override
    public BolusTableAdapter.BolusTableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
        return new BolusTableAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BolusTableAdapter.BolusTableAdapterViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: position: " + position);
        BolusTableData bolusTableData = mBolusTableData.get(position);
        //viewHolder.mGlucoseTextView.setText(String.valueOf(bolusTableData.getGlucose()));
        viewHolder.itemView.setTag(bolusTableData.getId());

        if (mSelectedItems.containsKey(position)){
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }

        if (null != viewHolder.mGlucose){
            viewHolder.mGlucose.setText(String.valueOf(bolusTableData.getGlucose()));
            return;
        }

        viewHolder.mInsul1.setText(String.valueOf(bolusTableData.getInsulin1()));
        viewHolder.mInsul2.setText(String.valueOf(bolusTableData.getInsulin2()));
        viewHolder.mInsul3.setText(String.valueOf(bolusTableData.getInsulin3()));
        viewHolder.mInsul4.setText(String.valueOf(bolusTableData.getInsulin4()));
        viewHolder.mInsul5.setText(String.valueOf(bolusTableData.getInsulin5()));
        viewHolder.mInsul6.setText(String.valueOf(bolusTableData.getInsulin6()));
        viewHolder.mInsul7.setText(String.valueOf(bolusTableData.getInsulin7()));

        viewHolder.mCard1.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_BREAKFAST_NAME));
        viewHolder.mCard2.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_BRUNCH_NAME));
        viewHolder.mCard3.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_LUNCH_NAME));
        viewHolder.mCard4.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_TEA_NAME));
        viewHolder.mCard5.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_DINNER_NAME));
        viewHolder.mCard6.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_SUPPER_NAME));
        viewHolder.mCard7.setTag(new FieldId(bolusTableData.getId(),CalculoDeBolusContract.BolusTable2Entry.COLUMN_DAWN_NAME));
    }

    @Override
    public int getItemCount() {
        if (null == mBolusTableData) return 0;
        return mBolusTableData.size();

    }

    @Override
    public void onViewAttachedToWindow(@NonNull BolusTableAdapterViewHolder holder) {
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



    public class BolusTableAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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

        public BolusTableAdapterViewHolder(@NonNull View itemView) {
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
            fieldClicked = null;
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
            fieldClicked = (FieldId) cardView.getTag();
            mSelectedItem = position;

            BolusTableData bolusTableData = mBolusTableData.get(position);
            mClickHandler.onClick(bolusTableData, mClickedItem, 0, mealName, fieldClicked);
        }

        @Override
        public boolean onLongClick(View v) {
            int clickedItem = getAdapterPosition();
            mSelectedItem = ITEN_SELECT_NONE;
            fieldClicked = null;
            if (mSelectedItems.containsKey(clickedItem)){
                mSelectedItems.remove(clickedItem);
            } else {
                mSelectedItems.put(clickedItem,clickedItem);
            }
            notifyDataSetChanged();
            BolusTableData bolusTableData = null;
            if (mSelectedItems.size() == 1){
                bolusTableData = mBolusTableData.get(clickedItem);
                mSelectedItem = clickedItem;
            }
            mClickHandler.onLongClick(mSelectedItems, bolusTableData);

            return true;
        }
    }


    // ---------------------------------- Functions ---------------------------------------------

    public void setBolusTableData(ArrayList<BolusTableData> bolusTableData){
        Log.d(TAG, "setBolusBlockTimeData: ");
        mBolusTableData = bolusTableData;
        notifyDataSetChanged();
    }



    // ---------------------------------- Data Base ---------------------------------------------

    private Cursor getAllData() {
        String sqlQuery = "SELECT * FROM " + CalculoDeBolusContract.BolusTable2Entry.TABLE_NAME;//CalculoDeBolusContract.BolusTableQuery.FETCH_ALL_DATA;
        Log.d(TAG, "getAllData: sqlQuery: " + sqlQuery);

        Cursor c = mDb.rawQuery(sqlQuery, null);

        return c;
    }

    public void refreshData(){
        BolusTableDataDAO bolusTableDataDAO = new BolusTableDataDAO(context);
        ArrayList<BolusTableData> bolusTableDatas = bolusTableDataDAO.fetchAll();
        setBolusTableData(bolusTableDatas);
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
        BolusTableDataDAO bolusTableDataDAO = new BolusTableDataDAO(context);
        HashMap<Integer,Integer> selecteds = new HashMap<>(mSelectedItems);
        int cont = 0;
        for (Map.Entry<Integer,Integer> item : selecteds.entrySet()){
            final int id = mBolusTableData.get(item.getKey()).getId();
            if (bolusTableDataDAO.delete(id)) {
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
//        BolusTableDataDAO bolusTableDataDAO = new BolusTableDataDAO(context);
//        return bolusTableDataDAO.updateInsulineField(mBolusTableData.get(mClickedItem),fieldName,insuline);
//    }

    public boolean updateItem(BolusTableData bolusTableData, String fieldName, Double insuline){
        int id = bolusTableData.getId();
        if (id <= 0){
            Log.d("Error", "updateItem: Item have no id.");
            return false;
        }

        BolusTableDataDAO bolusTableDataDAO = new BolusTableDataDAO(context);
        boolean updated = bolusTableDataDAO.updateInsulineField(bolusTableData,fieldName,insuline);
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

    public interface BolusTableAdapterOnClickHandler{
        void onClick(BolusTableData bolusTableData, int clickedItem, int SelectedItems, String mealName, FieldId fieldId);
        void onLongClick(HashMap<Integer,Integer> selectedItens, BolusTableData bolusTableData);
    }



}
