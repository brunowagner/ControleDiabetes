package br.com.bwsystemssolutions.controlediabetes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class BolusTimeBlockAdapter extends RecyclerView.Adapter<BolusTimeBlockAdapter.BolusTimeBlockAdapterViewHolder> {
	
	BolusTimeBlockData[] mBolusTimeBlockData;
	//TODO criar uma classe de nome BolusTimeBlockData contendo as propriedades abaixo:
	/*
		Date inicio;
		Date fim;
		int relacao;
		int alvo;
		int fatorDeSensibilidade;
	*/
	
	public void BolusTimeBlockAdapter(){
	}
	
	public class BolusTimeBlockAdapterViewHolder extends RecyclerView.ViewHolder(){
		public final TextView mBlocoDeTempoTextView;
		public final TextView mRelacaoTextView;
		public final TextView mFatorDeSensibilidadeTextView;
		public final TextView mAlvoTextView;
		
		public void BolusTimeBlockAdapterViewHolder(View view){
			super(view);
			mBlocoDeTempoTextView = (TextView) view.findViewById(R.id.tv_bloco_de_tempo);
			TextView mRelacaoTextView = (TextView) view.findViewById(R.id.tv_relacao);
			TextView mFatorDeSensibilidadeTextView = (TextView) view.findViewById(R.id.tv_fator_sensibilidade);
			TextView mAlvoTextView = (TextView) view.findViewById(R.id.tv_alvo);
		}
		
		public void setData(BolusTimeBlockData){
			mBlocoDeTempoTextView.setText(BolusTimeBlockData.inicio + "-" BolusTimeBlockData.fim);
			mRelacaoTextView.setText(BolusTimeBlockData.relacao);
			mFatorDeSensibilidadeTextView.setText(BolusTimeBlockData.fatorDeSensibilidade);
			mAlvoTextView.setText(BolusTimeBlockData.alvo);
		}
	}

	@Override
    public BolusTimeBlockAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		Context context = viewGroup.getContext();
		int layoutIdFromListItem = R.layout.calculate_list_item;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflater(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
		return new BolusTimeBlockAdapterViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder (BolusTimeBlockAdapterViewHolder bolusTimeBlockAdapterViewHolder, int position){
		BolusTimeBlockData timeBlockData =  mBolusTimeBlockData[position];
		BolusTimeBlockAdapterViewHolder.setData(timeBlockData);
	}
	
	@Override
    public int getItemCount() {
        if (null == mBolusTimeBlockData) return 0;
        return mBolusTimeBlockData.length;
    }
	
	public void setBolusBlockTimeData(BolusTimeBlockData[] bolusTimeBlockData){
		mBolusTimeBlockData = bolusTimeBlockData;
		notifyDataSetChanged();
	}
	
}
