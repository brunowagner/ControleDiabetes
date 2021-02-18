package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.data.Constants;
import br.com.bwsystemssolutions.controlediabetes.data.dao.RecordDAO;

import static br.com.bwsystemssolutions.controlediabetes.SettingsActivity.*;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordAdapterViewHolder> {
	private ArrayList<Record> mRecords;

	private SQLiteDatabase mDb;
	CalculoDeBolusDBHelper mDbHelper;
	private final RecordAdapterOnClickHandler mClickHandler;
	private String lastDay = "";
	private boolean revert = false;
	private int lastCollor;
	private int mSelectedItem = ITEN_SELECT_NONE;
	private Context mContext;

	public static final int ITEN_SELECT_NONE = -1;
	private static final int COLOR_RESOURSE_ITEM_PRIMARY = R.color.geloTransparente;
	private static final int COLOR_RESOURSE_ITEM_SECONDARY = R.color.white;

	int mGlicemiaBaixa;
	int mGlicemiaAlta;
	int mGlicemiaNormal;

	public RecordAdapter(CalculoDeBolusDBHelper calculoDeBolusDBHelper, RecordAdapterOnClickHandler clickHandler, Context context, SharedPreferences sharedPreferences) {
		mDbHelper = calculoDeBolusDBHelper;
		mDb = mDbHelper.getWritableDatabase();
		mClickHandler = clickHandler;
		mContext = context;

		SharedPreferences settings = sharedPreferences;

		String sGlicemiaBaixa = settings.getString( context.getString(R.string.pref_glicemia_baixa_key), context.getString(R.string.pref_glicemia_baixa_default_value));
		String sGlicemiaAlta = settings.getString( context.getString(R.string.pref_glicemia_alta_key), context.getString(R.string.pref_glicemia_alta_default_value));
		String sGlicemiaNormal = settings.getString( context.getString(R.string.pref_glicemia_normal_key), context.getString(R.string.pref_glicemia_normal_default_value));

		mGlicemiaBaixa = Integer.parseInt(sGlicemiaBaixa);
		mGlicemiaAlta = Integer.parseInt(sGlicemiaAlta);
		mGlicemiaNormal = Integer.parseInt(sGlicemiaNormal);
	}

	public int getItemCount() {
        if (null == mRecords) return 0;
        return mRecords.size();
    }

	public void refreshData(){
		RecordDAO recordDAO = new RecordDAO(mContext);
		ArrayList<Record> records = recordDAO.fetchAll();
		setRecords(records);
	}

	public void setRecords(ArrayList<Record> records){
		mRecords = records;
		notifyDataSetChanged();
	}

	public boolean deleteRecord(int itemToDelete){
		if (itemToDelete != mSelectedItem || itemToDelete == ITEN_SELECT_NONE) return false;

		Record record = mRecords.get(mSelectedItem);
		RecordDAO recordDAO = new RecordDAO(mContext);
		final boolean deleted = recordDAO.delete(record);
		if (deleted ) {
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
	
	public class RecordAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
		public final TextView mDataDiaSemanaTextView;
		public final TextView mHoraTextView;
		public final TextView mGlicemiaTextView;
		public final TextView mRefeicaoTextView;
		public final TextView mEventoTextView;
		public final TextView mCarboidratoTextView;
		public final TextView mInsulinaRapidaTextView;
		public final TextView mInsulinaBasalTextView;
		public final TextView mHeaderTextView;
		public final TextView mObsTextView;
		public final LinearLayout mObsLinerLayout;
		
		public RecordAdapterViewHolder(View itemView){
			super(itemView);
			mDataDiaSemanaTextView = (TextView) itemView.findViewById(R.id.tv_data_dia_semana);
			mHoraTextView = (TextView) itemView.findViewById(R.id.tv_hora);
			mGlicemiaTextView = (TextView) itemView.findViewById(R.id.tv_glicemia);
			mRefeicaoTextView = (TextView) itemView.findViewById(R.id.tv_refeicao);
			mEventoTextView = (TextView) itemView.findViewById(R.id.tv_evento);
			mCarboidratoTextView = (TextView) itemView.findViewById(R.id.tv_carboidrato);
			mInsulinaRapidaTextView = (TextView) itemView.findViewById(R.id.tv_insulina_rapida);
			mInsulinaBasalTextView = (TextView) itemView.findViewById(R.id.tv_insulina_basal);
			mHeaderTextView = itemView.findViewById(R.id.tv_header);
			mObsTextView = itemView.findViewById(R.id.tv_obs);
			mObsLinerLayout = itemView.findViewById(R.id.ll_obs);

			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View v) {
			 int position = getAdapterPosition();
            Record record = mRecords.get(position);
            mClickHandler.onClick(record, mSelectedItem);
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
	public RecordAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		Context context = viewGroup.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		//int layoutIdFromListItem = R.layout.register_list_item;
		int layoutIdFromListItem = R.layout.register_list_item_prototype;
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
		return new RecordAdapterViewHolder(view);
	}

	@Override
	public void onBindViewHolder (RecordAdapterViewHolder registrosAdapterViewHolder, int position){
		Record record = mRecords.get(position);

		registrosAdapterViewHolder.itemView.setTag(record.getId());
		registrosAdapterViewHolder.mDataDiaSemanaTextView.setText(record.getDateWeekDayString() );
		registrosAdapterViewHolder.mHoraTextView.setText( record.getTime() );
		registrosAdapterViewHolder.mGlicemiaTextView.setText(String.valueOf(record.getGlucose()));
		registrosAdapterViewHolder.mRefeicaoTextView.setText(record.getMeal());
		registrosAdapterViewHolder.mEventoTextView.setText(record.getEvent());
		registrosAdapterViewHolder.mCarboidratoTextView.setText(String.valueOf(record.getCarbohydrate()));
		registrosAdapterViewHolder.mInsulinaRapidaTextView.setText(String.valueOf(record.getFastInsulin()));
		registrosAdapterViewHolder.mInsulinaBasalTextView.setText(String.valueOf(record.getBasalInsulin()));
		registrosAdapterViewHolder.mObsTextView.setText(record.getNote());

		//Bloco que exibe ou omite Refeicao ou Evento
		if (record.getEvent().equals("")){
			registrosAdapterViewHolder.mEventoTextView.setVisibility(View.GONE);
		} else{
			registrosAdapterViewHolder.mEventoTextView.setVisibility(View.VISIBLE);
		}

		if (record.getMeal().equals("")){
			registrosAdapterViewHolder.mRefeicaoTextView.setVisibility(View.GONE);
		} else{
			registrosAdapterViewHolder.mRefeicaoTextView.setVisibility(View.VISIBLE);
		}


		//Bloco que seta a selecao do item
		if (mSelectedItem == position){
			registrosAdapterViewHolder.itemView.setBackgroundColor(Color.LTGRAY);
		} else {
			registrosAdapterViewHolder.itemView.setBackgroundColor(Color.WHITE);
		}

		int g = record.getGlucose();

		if (g == 0){
            registrosAdapterViewHolder.mGlicemiaTextView.setVisibility(View.INVISIBLE);
        } else {
			registrosAdapterViewHolder.mGlicemiaTextView.setVisibility(View.VISIBLE);
		}

        //if (g <= 65){
		if (g <= mGlicemiaBaixa){
			registrosAdapterViewHolder.mGlicemiaTextView.setBackgroundResource(R.drawable.circle_hipo);
		}  else if (g > mGlicemiaBaixa && g < mGlicemiaAlta){
			registrosAdapterViewHolder.mGlicemiaTextView.setBackgroundResource(R.drawable.circle_normal);
		} else {
			registrosAdapterViewHolder.mGlicemiaTextView.setBackgroundResource(R.drawable.circle_hiper);
		}



		if (registrosAdapterViewHolder.mObsTextView.length() == 0 || registrosAdapterViewHolder.mObsTextView.getText().toString() == ""){
			registrosAdapterViewHolder.mObsLinerLayout.setVisibility(View.GONE);
		} else {
			registrosAdapterViewHolder.mObsLinerLayout.setVisibility(View.VISIBLE);
		}

		// if not first item check if item above has the same header
		if (position > 0 && mRecords.get(position - 1).getDateWeekDayString().substring(0, 17).equals(mRecords.get(position).getDateWeekDayString().substring(0, 17))) {
			registrosAdapterViewHolder.mHeaderTextView.setVisibility(View.GONE);

		} else {
			registrosAdapterViewHolder.mHeaderTextView.setText(mRecords.get(position).getDateWeekDayString().substring(0, 17));
			registrosAdapterViewHolder.mHeaderTextView.setVisibility(View.VISIBLE);
		}
	}
	
	public interface RecordAdapterOnClickHandler{
	    void onClick(Record record, int itemSelected);
		void onLongClick(int selectedItem);
    }
}
