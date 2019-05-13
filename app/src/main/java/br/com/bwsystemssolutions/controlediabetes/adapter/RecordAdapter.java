package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordAdapterViewHolder> {
	private ArrayList<Record> mRecords;

	private SQLiteDatabase mDb;
	CalculoDeBolusDBHelper mDbHelper;
	private final RecordAdapterOnClickHandler mClickHandler;

	public RecordAdapter(CalculoDeBolusDBHelper calculoDeBolusDBHelper, RecordAdapterOnClickHandler clickHandler) {
		mDbHelper = calculoDeBolusDBHelper;
		mDb = mDbHelper.getWritableDatabase();
		mClickHandler = clickHandler;
	}

	public int getItemCount() {
        if (null == mRecords) return 0;
        return mRecords.size();
    }
	
	private Cursor getAllData() {
		//TODO: modifcar a query para ordenar de forma descendente (do mais recente para o mais antigo)
		return mDb.query(CalculoDeBolusContract.RecordEntry.TABLE_NAME,
				null, null, null, null,null,
				CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME);
	}

	public void refreshData(){

		Cursor cursor = getAllData();
		Log.d("bwvm", "refreshData: tamanho do cursor" + cursor.getCount());
		if (cursor.getCount() <= 0) return;
		ArrayList<Record> records = new ArrayList<Record>();
		if (cursor.moveToFirst()){
			do {
				Record record = new Record();
				record.setId(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry._ID)));
				record.setDateFromStringDateSqlite(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_DATE_TIME_NAME)));
				record.setCarbohydrate(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_CARBOHYDRATE_NAME)));
				record.setGlucose(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_GLUCOSE_NAME)));
				record.setFastInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_FAST_INSULIN_NAME)));
				record.setBasalInsulin(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_BASAL_INSULIN_NAME)));
				record.setEvent(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_EVENT_NAME)));
				record.setNote(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_NOTE_NAME)));
				record.setSick(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_SICK_NAME))>0);
				record.setMedicament(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.RecordEntry.COLUMN_MEDICAMENT_NAME))>0);

				records.add(record);
			}while(cursor.moveToNext());

		}
		cursor.close();
		setRecords(records);
	}

	public void setRecords(ArrayList<Record> records){
		mRecords = records;
		notifyDataSetChanged();
	}
	
	public class RecordAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		public final TextView mDataHoraTextView;
		public final TextView mGlicemiaTextView;
		public final TextView mEventoTextView;
		public final TextView mCarboidratoTextView;
		public final TextView mInsulinaRapidaTextView;
		public final TextView mInsulinaBasalTextView;
		
		public RecordAdapterViewHolder(View itemView){
			super(itemView);
			mDataHoraTextView = (TextView) itemView.findViewById(R.id.tv_data_hora_dia_semana);
			mGlicemiaTextView = (TextView) itemView.findViewById(R.id.tv_glicemia);
			mEventoTextView = (TextView) itemView.findViewById(R.id.tv_evento);
			mCarboidratoTextView = (TextView) itemView.findViewById(R.id.tv_carboidrato);
			mInsulinaRapidaTextView = (TextView) itemView.findViewById(R.id.tv_insulina_rapida);
			mInsulinaBasalTextView = (TextView) itemView.findViewById(R.id.tv_insulina_basal);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			 int position = getAdapterPosition();
            Record record = mRecords.get(position);
            mClickHandler.onClick(record);
		}
	}

	@Override
	public RecordAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		Context context = viewGroup.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		int layoutIdFromListItem = R.layout.register_list_item;
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
		return new RecordAdapterViewHolder(view);
	}

	@Override
	public void onBindViewHolder (RecordAdapterViewHolder registrosAdapterViewHolder, int position){
		Record record = mRecords.get(position);

		String dateTimeDayOfWeek = record.getDateTimeWeekDay();

		//TODO fazer um cast melhor do double
		registrosAdapterViewHolder.itemView.setTag(record.getId());
		registrosAdapterViewHolder.mDataHoraTextView.setText( dateTimeDayOfWeek );

		registrosAdapterViewHolder.mGlicemiaTextView.setText(String.valueOf(record.getGlucose()));
		registrosAdapterViewHolder.mEventoTextView.setText(record.getEvent());
		registrosAdapterViewHolder.mCarboidratoTextView.setText(String.valueOf(record.getCarbohydrate()));
		registrosAdapterViewHolder.mInsulinaRapidaTextView.setText(String.valueOf(record.getFastInsulin()));
		registrosAdapterViewHolder.mInsulinaBasalTextView.setText(String.valueOf(record.getBasalInsulin()));

	}
	
	public interface RecordAdapterOnClickHandler{
	    void onClick(Record record);
    }
}
