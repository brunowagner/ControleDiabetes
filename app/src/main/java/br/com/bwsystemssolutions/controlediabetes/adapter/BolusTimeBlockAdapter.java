package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
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
	//private int mCount;

	public BolusTimeBlockAdapter(BolusTimeBlockAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
	}

	public interface BolusTimeBlockAdapterOnClickHandler{
	    void onClick(BolusTimeBlockData bolusTimeBlockData);
    }

	public class BolusTimeBlockAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public final TextView mBlocoDeTempoTextView;
		public final TextView mRelacaoTextView;
		public final TextView mFatorDeSensibilidadeTextView;
		public final TextView mAlvoTextView;

		public BolusTimeBlockAdapterViewHolder(View itemView){
			super(itemView);
			mBlocoDeTempoTextView = (TextView) itemView.findViewById(R.id.tv_inicio_do_bloco_de_tempo);
			mRelacaoTextView = (TextView) itemView.findViewById(R.id.tv_relacao);
			mFatorDeSensibilidadeTextView = (TextView) itemView.findViewById(R.id.tv_fator_sensibilidade);
			mAlvoTextView = (TextView) itemView.findViewById(R.id.tv_alvo);
			itemView.setOnClickListener(this);
		}

		//Utilizado pelo onBind para deixá-lo mais limpo.
		private void setData(BolusTimeBlockData bolusTimeBlockData){
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
            mClickHandler.onClick(bolusTimeBlockData);
        }
    }

	@Override
    public BolusTimeBlockAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		Context context = viewGroup.getContext();
		int layoutIdFromListItem = R.layout.calculate_list_item;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
		return new BolusTimeBlockAdapterViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder (BolusTimeBlockAdapterViewHolder bolusTimeBlockAdapterViewHolder, int position){
		BolusTimeBlockData timeBlockData =  mBolusTimeBlockData.get(position);
		//armazena o id na tag do itemView a fim de utilizá-lo quando for necessário deletar o registro.
		bolusTimeBlockAdapterViewHolder.itemView.setTag(timeBlockData.id);
        bolusTimeBlockAdapterViewHolder.setData(timeBlockData);
	}
	
	@Override
    public int getItemCount() {
        if (null == mBolusTimeBlockData) return 0;
        return mBolusTimeBlockData.size();
    }
	
	public void setBolusBlockTimeData(ArrayList<BolusTimeBlockData> bolusTimeBlockData){
		mBolusTimeBlockData = bolusTimeBlockData;
		notifyDataSetChanged();
	}
	
}
