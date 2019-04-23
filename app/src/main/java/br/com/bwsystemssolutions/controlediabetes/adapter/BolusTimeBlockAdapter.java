package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
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

public class BolusTimeBlockAdapter extends RecyclerView.Adapter<BolusTimeBlockAdapter.BolusTimeBlockAdapterViewHolder> {
	
	private ArrayList<BolusTimeBlockData> mBolusTimeBlockData;
	private final BolusTimeBlockAdapterOnClickHandler mClickHandler;
	public final int ITEN_SELECT_NONE = -1;
	private int mSelectedItem = ITEN_SELECT_NONE;
	//private int mCount;

	public BolusTimeBlockAdapter(BolusTimeBlockAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
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
			Log.d("bwvm", "onLongClick: AdapterPosition = " + getAdapterPosition());
			Log.d("bwvm", "onLongClick: getItemId = " + getItemId());
			Log.d("bwvm", "onLongClick: getLayoutPosition = " + getLayoutPosition());
			Log.d("bwvm", "onLongClick: getOldPosition = " + getOldPosition());

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
			bolusTimeBlockAdapterViewHolder.itemView.setBackgroundColor(Color.CYAN);
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

	public void setSelectedItem(int selectedItem){
		mSelectedItem = selectedItem;
		notifyDataSetChanged();
	}
	
}
