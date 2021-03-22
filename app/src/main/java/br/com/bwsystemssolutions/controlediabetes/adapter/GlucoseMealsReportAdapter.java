package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.GlucoseMealsReport;

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
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdFromListItem = R.layout.glucose_meals_report_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
        return new GlucoseMealsReportAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GlucoseMealsReportAdapterViewHolder glucoseMealsReportAdapterViewHolder, int position) {
        GlucoseMealsReport glucoseMealsReport = mData.get(position);

        glucoseMealsReportAdapterViewHolder.itemView.setTag(glucoseMealsReport.getData());
        glucoseMealsReportAdapterViewHolder.mDia.setText(glucoseMealsReport.getDateString());
        glucoseMealsReportAdapterViewHolder.mDiaDaSemana.setText(glucoseMealsReport.getWeekDayString());

        glucoseMealsReportAdapterViewHolder.mInsulCafe.setText(String.valueOf(glucoseMealsReport.getInsulCafe()));
        glucoseMealsReportAdapterViewHolder.mInsulColacao.setText(String.valueOf(glucoseMealsReport.getInsulColacao()));
        glucoseMealsReportAdapterViewHolder.mInsulAlmoco.setText(String.valueOf(glucoseMealsReport.getInsulAlmoco()));
        glucoseMealsReportAdapterViewHolder.mInsulLanche.setText(String.valueOf(glucoseMealsReport.getInsulLanche()));
        glucoseMealsReportAdapterViewHolder.mInsulJantar.setText(String.valueOf(glucoseMealsReport.getInsulJantar()));
        glucoseMealsReportAdapterViewHolder.mInsulCeia.setText(String.valueOf(glucoseMealsReport.getInsulCeia()));
        glucoseMealsReportAdapterViewHolder.mInsulMadrugada.setText(String.valueOf(glucoseMealsReport.getInsulMadrugada());
        glucoseMealsReportAdapterViewHolder.mCarboCafe.setText(glucoseMealsReport.getCarboCafe());
        glucoseMealsReportAdapterViewHolder.mCarboColacao.setText(glucoseMealsReport.getCarboColacao());
        glucoseMealsReportAdapterViewHolder.mCarboAlmoco.setText(glucoseMealsReport.getCarboAlmoco());
        glucoseMealsReportAdapterViewHolder.mCarboLanche.setText(glucoseMealsReport.getCarboLanche());
        glucoseMealsReportAdapterViewHolder.mCarboJantar.setText(glucoseMealsReport.getCarboJantar());
        glucoseMealsReportAdapterViewHolder.mCarboCeia.setText(glucoseMealsReport.getCarboCeia());
        glucoseMealsReportAdapterViewHolder.mCarboMadrugada.setText(glucoseMealsReport.getCarboMadrugada());
        glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setText(glucoseMealsReport.getGlicoseCafe());
        glucoseMealsReportAdapterViewHolder.mGlicoseColacao.setText(glucoseMealsReport.getGlicoseColacao());
        glucoseMealsReportAdapterViewHolder.mGlicoseAlmoco.setText(glucoseMealsReport.getGlicoseAlmoco());
        glucoseMealsReportAdapterViewHolder.mGlicoseLanche.setText(glucoseMealsReport.getGlicoseLanche());
        glucoseMealsReportAdapterViewHolder.mGlicoseJantar.setText(glucoseMealsReport.getGlicoseJantar());
        glucoseMealsReportAdapterViewHolder.mGlicoseCeia.setText(glucoseMealsReport.getGlicoseCeia());
        glucoseMealsReportAdapterViewHolder.mGlicoseMadrugada.setText(glucoseMealsReport.getGlicoseMadrugada());

        int g = glucoseMealsReport.getGlicoseCafe();

        if (g == 0){
            glucoseMealsReportAdapterViewHolder.mGlicemiaTextView.setVisibility(View.INVISIBLE);
        } else {
            registrosAdapterViewHolder.mGlicemiaTextView.setVisibility(View.VISIBLE);
        }

        //if (g <= 65){
        if (g <= mGlicemiaBaixa){
            glucoseMealsReportAdapterViewHolder.mGlicemiaTextView.setBackgroundResource(R.drawable.circle_hipo);
        }  else if (g > mGlicemiaBaixa && g < mGlicemiaAlta){
            registrosAdapterViewHolder.mGlicemiaTextView.setBackgroundResource(R.drawable.circle_normal);
        } else {
            registrosAdapterViewHolder.mGlicemiaTextView.setBackgroundResource(R.drawable.circle_hiper);
        }


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();
    }


    // --------  Classe ViewHolder  -------------------------------------------------------
    public class GlucoseMealsReportAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView mInsulCafe;
        public final TextView mInsulColacao;
        public final TextView mInsulAlmoco;
        public final TextView mInsulLanche;
        public final TextView mInsulJantar;
        public final TextView mInsulCeia;
        public final TextView mInsulMadrugada;
        public final TextView mCarboCafe;
        public final TextView mCarboColacao;
        public final TextView mCarboAlmoco;
        public final TextView mCarboLanche;
        public final TextView mCarboJantar;
        public final TextView mCarboCeia;
        public final TextView mCarboMadrugada;
        public final TextView mGlicoseCafe;
        public final TextView mGlicoseColacao;
        public final TextView mGlicoseAlmoco;
        public final TextView mGlicoseLanche;
        public final TextView mGlicoseJantar;
        public final TextView mGlicoseCeia;
        public final TextView mGlicoseMadrugada;
        public final TextView mDia;
        public final TextView mDiaDaSemana;

        public GlucoseMealsReportAdapterViewHolder(View itemView){
            super(itemView);

            mDia = itemView.findViewById(R.id.tv_data);
            mDiaDaSemana = itemView.findViewById(R.id.tv_data_dia_semana);

            mInsulCafe = itemView.findViewById(R.id.tv_insul_cafe);
            mInsulColacao = itemView.findViewById(R.id.tv_insul_colacao);
            mInsulAlmoco = itemView.findViewById(R.id.tv_insul_almoco);
            mInsulLanche = itemView.findViewById(R.id.tv_insul_lanche);
            mInsulJantar = itemView.findViewById(R.id.tv_insul_jantar);
            mInsulCeia = itemView.findViewById(R.id.tv_insul_ceia);
            mInsulMadrugada = itemView.findViewById(R.id.tv_insul_madrugada);

            mCarboCafe = itemView.findViewById(R.id.tv_carbo_cafe);
            mCarboColacao = itemView.findViewById(R.id.tv_carbo_colacao);
            mCarboAlmoco = itemView.findViewById(R.id.tv_carbo_almoco);
            mCarboLanche = itemView.findViewById(R.id.tv_carbo_lanche);
            mCarboJantar = itemView.findViewById(R.id.tv_carbo_jantar);
            mCarboCeia = itemView.findViewById(R.id.tv_carbo_ceia);
            mCarboMadrugada = itemView.findViewById(R.id.tv_carbo_madrugada);

            mGlicoseCafe = itemView.findViewById(R.id.tv_glicose_cafe_da_manha);
            mGlicoseColacao = itemView.findViewById(R.id.tv_glicose_colacao);
            mGlicoseAlmoco = itemView.findViewById(R.id.tv_glicose_almoco);
            mGlicoseLanche = itemView.findViewById(R.id.tv_glicose_lanche);
            mGlicoseJantar = itemView.findViewById(R.id.tv_glicose_jantar);
            mGlicoseCeia = itemView.findViewById(R.id.tv_glicose_ceia);
            mGlicoseMadrugada = itemView.findViewById(R.id.tv_glicose_madrugada);


        }

    }
}
