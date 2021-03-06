package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTimeBlockData;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

public class BolusTimeBlockAdapter extends RecyclerView.Adapter<BolusTimeBlockAdapter.BolusTimeBlockAdapterViewHolder> {

	private SQLiteDatabase mDb;
	CalculoDeBolusDBHelper mDbHelper;
	private ArrayList<BolusTimeBlockData> mBolusTimeBlockData;
	private final BolusTimeBlockAdapterOnClickHandler mClickHandler;
	public static final int ITEN_SELECT_NONE = -1;
	private int mSelectedItem = ITEN_SELECT_NONE;
	//private int mCount;

	public BolusTimeBlockAdapter(CalculoDeBolusDBHelper calculoDeBolusDBHelper, BolusTimeBlockAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mDbHelper = calculoDeBolusDBHelper;
		mDb = mDbHelper.getWritableDatabase();
        //configureDB();
	}



	public interface BolusTimeBlockAdapterOnClickHandler{
	    void onClick(BolusTimeBlockData bolusTimeBlockData, int itemSelected);
		void onLongClick(int selectedItem);
    }



	public class BolusTimeBlockAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public final TextView mBlocoDeTempoTextView;
		public final TextView mRelacaoTextView;
		public final TextView mFatorDeSensibilidadeTextView;
		public final TextView mAlvoTextView;

		public BolusTimeBlockAdapterViewHolder(View itemView){
			super(itemView);
			Log.d("bwvm", "BolusTimeBlockAdapterViewHolder: ");
			mBlocoDeTempoTextView = (TextView) itemView.findViewById(R.id.tv_inicio_do_bloco_de_tempo);
			mRelacaoTextView = (TextView) itemView.findViewById(R.id.tv_relacao);
			mFatorDeSensibilidadeTextView = (TextView) itemView.findViewById(R.id.tv_fator_sensibilidade);
			mAlvoTextView = (TextView) itemView.findViewById(R.id.tv_alvo);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		//Utilizado pelo onBind para deixá-lo mais limpo.
		private void setData(BolusTimeBlockData bolusTimeBlockData){
			Log.d("bwvm", "setData: ");
			mBlocoDeTempoTextView.setText(bolusTimeBlockData.start);
			mRelacaoTextView.setText(String.valueOf(bolusTimeBlockData.relation));
			mFatorDeSensibilidadeTextView.setText(String.valueOf(bolusTimeBlockData.sensibilityFactor));
			mAlvoTextView.setText(String.valueOf(bolusTimeBlockData.tarjet));
		}

        @Override
        public void onClick(View v) {
			Log.d("bwvm", "onClick: Item Clicado!");
            int position = getAdapterPosition();
            BolusTimeBlockData bolusTimeBlockData = mBolusTimeBlockData.get(position);
            mClickHandler.onClick(bolusTimeBlockData, mSelectedItem);
        }

		@Override
		public boolean onLongClick(View v) {
			mSelectedItem = getAdapterPosition();
			notifyDataSetChanged();
			mClickHandler.onLongClick(mSelectedItem);

			return true;
		}
	}

	@Override
    public BolusTimeBlockAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		Log.d("bwvm", "onCreateViewHolder: ");
		Context context = viewGroup.getContext();
		int layoutIdFromListItem = R.layout.calculate_list_item;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
		return new BolusTimeBlockAdapterViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder (BolusTimeBlockAdapterViewHolder bolusTimeBlockAdapterViewHolder, int position){
		Log.d("bwvm", "onBindViewHolder: ");
		BolusTimeBlockData timeBlockData =  mBolusTimeBlockData.get(position);
		//armazena o id na tag do itemView a fim de utilizá-lo quando for necessário deletar o registro.
		bolusTimeBlockAdapterViewHolder.itemView.setTag(timeBlockData.id);
        bolusTimeBlockAdapterViewHolder.setData(timeBlockData);

        //noite
		if (mSelectedItem == position){
			bolusTimeBlockAdapterViewHolder.itemView.setBackgroundColor(Color.LTGRAY);
		} else {
			bolusTimeBlockAdapterViewHolder.itemView.setBackgroundColor(Color.WHITE);
		}
	}
	
	@Override
    public int getItemCount() {
		Log.d("bwvm", "getItemCount: ");
        if (null == mBolusTimeBlockData) return 0;
        return mBolusTimeBlockData.size();
    }
	
	public void setBolusBlockTimeData(ArrayList<BolusTimeBlockData> bolusTimeBlockData){
		Log.d("bwvm", "setBolusBlockTimeData: ");
		mBolusTimeBlockData = bolusTimeBlockData;
		notifyDataSetChanged();
	}

	public BolusTimeBlockData getBolusTimeBlockData(int itemToDelete){
		return mBolusTimeBlockData.get(itemToDelete);

	}

	public boolean deleteBolusTimeBlockData(int itemToDelete){
		if (itemToDelete != mSelectedItem || itemToDelete == ITEN_SELECT_NONE) return false;
		if (mBolusTimeBlockData.size()==1) return false;

		BolusTimeBlockData bolusTimeBlockData = mBolusTimeBlockData.get(mSelectedItem);

		int deleted = mDb.delete(CalculoDeBolusContract.TimeBlockEntry.TABLE_NAME, CalculoDeBolusContract.TimeBlockEntry._ID + "= ?", new String[]{bolusTimeBlockData.id + ""});
		Log.d("bwvm", "deleteBolusTimeBlockData: valor de delete: " + deleted);
		if (deleted > 0 ) {
			setSelectedItem(ITEN_SELECT_NONE);
			return true;
		} else {
			return false;
		}
	}

	public void setSelectedItem(int selectedItem){
		mSelectedItem = selectedItem;
		notifyDataSetChanged();
	}


	private void configureDB() {
		// Configurando o banco de dados
		mDb = mDbHelper.getWritableDatabase();
	}

	private Cursor getAllData() {
		return mDb.query(CalculoDeBolusContract.TimeBlockEntry.TABLE_NAME,
				null, null, null, null,null,
				CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME);
	}

	public void refreshData(){
		Cursor cursor = getAllData();
		ArrayList<BolusTimeBlockData> bolusTimeBlockDataAL = new ArrayList<BolusTimeBlockData>();
		if (cursor.moveToFirst()){
			do {
				BolusTimeBlockData bolusTimeBlockData = new BolusTimeBlockData();
				bolusTimeBlockData.id = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry._ID));
				bolusTimeBlockData.start = cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_INITIAL_TIME_NAME));
				bolusTimeBlockData.relation = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_RELATION_NAME));
				bolusTimeBlockData.sensibilityFactor = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_SENSITIVITY_FACTOR_NAME));
				bolusTimeBlockData.tarjet = cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.TimeBlockEntry.COLUMN_TARGET_NAME));

				bolusTimeBlockDataAL.add(bolusTimeBlockData);
			}while(cursor.moveToNext());

		}
		cursor.close();
		setBolusBlockTimeData(bolusTimeBlockDataAL);
	}


	
}
