package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.ResourceBundle;

import br.com.bwsystemssolutions.controlediabetes.R;

class GlucoseMealsReportAdapter extends RecyclerView.Adapter<GlucoseMealsReportAdapter.GlucoseMealsReportAdapterViewHolder> {
    private ArrayList<GlucoseMealsReport> mData;

    int mGlicemiaBaixa;
    int mGlicemiaAlta;
    int mGlicemiaNormal;

    public GlucoseMealsReportAdapter(SharedPreferences sharedPreferences, Context context){
        SharedPreferences settings = sharedPreferences;

        String sGlicemiaBaixa = settings.getString( context.getString(R.string.pref_glicemia_baixa_key), context.getString(R.string.pref_glicemia_baixa_default_value));
        String sGlicemiaAlta = settings.getString( context.getString(R.string.pref_glicemia_alta_key), context.getString(R.string.pref_glicemia_alta_default_value));
        String sGlicemiaNormal = settings.getString( context.getString(R.string.pref_glicemia_normal_key), context.getString(R.string.pref_glicemia_normal_default_value));

        mGlicemiaBaixa = Integer.parseInt(sGlicemiaBaixa);
        mGlicemiaAlta = Integer.parseInt(sGlicemiaAlta);
        mGlicemiaNormal = Integer.parseInt(sGlicemiaNormal);
    }



    @NonNull
    @Override
    public GlucoseMealsReportAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //TODO codificar onCreated
    }

    @Override
    public void onBindViewHolder(@NonNull GlucoseMealsReportAdapterViewHolder glucoseMealsReportAdapterViewHolder, int i) {
        //TODO codificar onBind
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();
    }


    // --------  Classe ViewHolder  -------------------------------------------------------
    public class GlucoseMealsReportAdapterViewHolder extends RecyclerView.ViewHolder{

        public GlucoseMealsReportAdapterViewHolder(View itemView){
            super(itemView);

            mInsulCafe     = itemView.findViewById(R.id.tv_insul_cafe);
            mInsulColacao  = itemView.findViewById(R.id.tv_insul_colacao);
            mInsulAlmoco   = itemView.findViewById(R.id.tv_insul_almoco);
            mInsulLanche   = itemView.findViewById(R.id.tv_insul_lanche);
            mInsulJantar    = itemView.findViewById(R.id.tv_insul_jantar);
            mInsulCeia   = itemView.findViewById(R.id.tv_insul_ceia);
            mInsulMadrugada  = itemView.findViewById(R.id.tv_insul_madrugada);

            mCarboCafe = itemView.findViewById(R.id.tv_carbo_cafe);
            mCarboColacao = itemView.findViewById(R.id.tv_carbo_colacao);
            mCarboAlmoco = itemView.findViewById(R.id.tv_carbo_almoco);
            mCarboLanche = itemView.findViewById(R.id.tv_carbo_lanche);
            mCarboJantar = itemView.findViewById(R.id.tv_carbo_jantar);
            mCarboCeia = itemView.findViewById(R.id.tv_carbo_ceia);
            mCarboMadrugada = itemView.findViewById(R.id.tv_carbo_madrugada);

            mGlicoseCafe  = itemView.findViewById(R.id.tv_glicose_cafe_da_manha);
            mGlicoseColacao = itemView.findViewById(R.id.tv_glicose_colacao);
            mGlicoseAlmoco = itemView.findViewById(R.id.tv_glicose_almoco);
            mGlicoseLanche = itemView.findViewById(R.id.tv_glicose_lanche);
            mGlicoseJantar = itemView.findViewById(R.id.tv_glicose_jantar);
            mGlicoseCeia = itemView.findViewById(R.id.tv_glicose_ceia);
            mGlicoseMadrugada  = itemView.findViewById(R.id.tv_glicose_madrugada);










        }

    }
}
