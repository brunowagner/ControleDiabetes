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

public class RegistrosAdapter extends RecyclerView.Adapter<RegistrosAdapter.RegistrosAdapterViewHolder> {
	private ArrayList<Record> mRecord;
	
	public int getItemCount() {
        if (null == mRecord) return 0;
        return mRecord.size();
    }
	
	private Cursor getAllData() {
		//TODO: modifcar a query para ordenar de forma descendente (do mais recente para o mais antigo)
		return mDb.query(CalculoDeBolusContract.RecordEntry.TABLE_NAME,
				null, null, null, null,null,
				CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME);
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
	
	public class RegistrosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		public final TextView mDataHoraTextView;
		//public final TextView mHora;
		public final TextView mGlicemiaTextView;
		public final TextView mEventoTextView;
		public final TextView mCarboidratoTextView;
		public final TextView mInsulinaRapidaTextView;
		public final TextView mInsulinaBasal;
		public final Checkbox mDoenteCheckbox;
		public final Checkbox mMedicamentoCheckbox;
		public final TextView mObservacaoTextView;
		
		public RegistrosAdapterViewHolder (View itemView){
			mDataHora = (TextView) itemView.findViewById(R.id.tv_dateHora);
			//mHora;
			mGlicemiaTextView = (TextView) itemView.findViewById(R.id.tv_glicemia);
			mEventoTextView = (TextView) itemView.findViewById(R.id.tv_evento);
			mCarboidratoTextView = (TextView) itemView.findViewById(R.id.tv_carboidrato);
			mInsulinaRapidaTextView = (TextView) itemView.findViewById(R.id.tv_insulina_rapida);
			mInsulinaBasalTextView = (TextView) itemView.findViewById(R.id.tv_insulina_basal);
			mDoenteCheckbox = (Checkbox) itemView.findViewById(R.id.tv_doente);
			mMedicamentoCheckbox = (Checkbox) itemView.findViewById(R.id.tv_medicamento);
			mObservacaoTextView = (TextView) itemView.findViewById(R.id.tv_observacao);
		}
		
		@Override
		public RegistrosAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			Context context = viewGroup.getContext();
			LayoutInflater inflater = LayoutInflater.from(context);
			int layoutIdFromListItem = R.layout.register_list_item;
			boolean shouldAttachToParentImmediately = false;
			
			View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
			return new RegistrosAdapterViewHolder(view);
		}
		
		@Override
		public void onBindViewHolder (RegistrosAdapterViewHolder registrosAdapterViewHolder, int position){
			Record record = mRecord.get(position)
			
			//TODO setar os views com os dados do objeto record.
			registrosAdapterViewHolder.itemView.setTag(registro.id);
			mGlicemiaTextView.setText();
			mEventoTextView.setText();
			mCarboidratoTextView.setText();
			mInsulinaRapidaTextView.setText();
			mInsulinaBasalTextView.setText();
			mDoenteCheckbox.setText();
			mMedicamentoCheckbox.setText();
			mObservacaoTextView.setText();
		}
	}
	
	
}
