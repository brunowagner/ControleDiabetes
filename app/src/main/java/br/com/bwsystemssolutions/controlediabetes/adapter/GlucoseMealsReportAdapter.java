package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.GlucoseMealsReport;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.data.dao.GlucoseMealsReportDAO;
import br.com.bwsystemssolutions.controlediabetes.data.dao.RecordDAO;

public class GlucoseMealsReportAdapter extends RecyclerView.Adapter<GlucoseMealsReportAdapter.GlucoseMealsReportAdapterViewHolder> {
    private ArrayList<GlucoseMealsReport> mData;

    private Context mContext;

    int mGlicemiaBaixa;
    int mGlicemiaAlta;
    int mGlicemiaNormal;

    public GlucoseMealsReportAdapter(SharedPreferences sharedPreferences, Context context){
        SharedPreferences settings = sharedPreferences;
        mContext=context;

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
        glucoseMealsReportAdapterViewHolder.mInsulMadrugada.setText(String.valueOf(glucoseMealsReport.getInsulMadrugada()));
        glucoseMealsReportAdapterViewHolder.mCarboCafe.setText(String.valueOf(glucoseMealsReport.getCarboCafe()));
        glucoseMealsReportAdapterViewHolder.mCarboColacao.setText(String.valueOf(glucoseMealsReport.getCarboColacao()));
        glucoseMealsReportAdapterViewHolder.mCarboAlmoco.setText(String.valueOf(glucoseMealsReport.getCarboAlmoco()));
        glucoseMealsReportAdapterViewHolder.mCarboLanche.setText(String.valueOf(glucoseMealsReport.getCarboLanche()));
        glucoseMealsReportAdapterViewHolder.mCarboJantar.setText(String.valueOf(glucoseMealsReport.getCarboJantar()));
        glucoseMealsReportAdapterViewHolder.mCarboCeia.setText(String.valueOf(glucoseMealsReport.getCarboCeia()));
        glucoseMealsReportAdapterViewHolder.mCarboMadrugada.setText(String.valueOf(glucoseMealsReport.getCarboMadrugada()));
        glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setText(String.valueOf(glucoseMealsReport.getGlicoseCafe()));
        glucoseMealsReportAdapterViewHolder.mGlicoseColacao.setText(String.valueOf(glucoseMealsReport.getGlicoseColacao()));
        glucoseMealsReportAdapterViewHolder.mGlicoseAlmoco.setText(String.valueOf(glucoseMealsReport.getGlicoseAlmoco()));
        glucoseMealsReportAdapterViewHolder.mGlicoseLanche.setText(String.valueOf(glucoseMealsReport.getGlicoseLanche()));
        glucoseMealsReportAdapterViewHolder.mGlicoseJantar.setText(String.valueOf(glucoseMealsReport.getGlicoseJantar()));
        glucoseMealsReportAdapterViewHolder.mGlicoseCeia.setText(String.valueOf(glucoseMealsReport.getGlicoseCeia()));
        glucoseMealsReportAdapterViewHolder.mGlicoseMadrugada.setText(String.valueOf(glucoseMealsReport.getGlicoseMadrugada()));

        int gCafe = glucoseMealsReport.getGlicoseCafe();
        int gColacao = glucoseMealsReport.getGlicoseColacao();
        int gAlmoco = glucoseMealsReport.getGlicoseAlmoco();
        int gLanche = glucoseMealsReport.getGlicoseLanche();
        int gJantar = glucoseMealsReport.getGlicoseJantar();
        int gCeia = glucoseMealsReport.getGlicoseCeia();
        int gMadrugada = glucoseMealsReport.getGlicoseMadrugada();

//        if (gCafe == 0){
//            glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setVisibility(View.INVISIBLE);
//        } else {
//            glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setVisibility(View.VISIBLE);
//        }

//        //Seta cor do circulo do cafe
//        if (gCafe <= mGlicemiaBaixa){
//            glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setBackgroundResource(R.drawable.circle_hipo);
//        }  else if (gCafe > mGlicemiaBaixa && gCafe < mGlicemiaAlta){
//            glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setBackgroundResource(R.drawable.circle_normal);
//        } else {
//            glucoseMealsReportAdapterViewHolder.mGlicoseCafe.setBackgroundResource(R.drawable.circle_hiper);
//        }

        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseCafe,      gCafe);
        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseColacao,   gColacao);
        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseAlmoco,    gAlmoco);
        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseLanche,    gLanche);
        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseJantar,    gJantar);
        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseCeia,      gCeia);
        setGlucoseBackground(glucoseMealsReportAdapterViewHolder.mGlicoseMadrugada, gMadrugada);

    }

    //funcao utilizada no onBind para colorir o circulo do valor da clicose.
    private void setGlucoseBackground(TextView glucoseView, int glucoseValue){
        //Seta a cor do circulo da glicose
        if (glucoseValue <= mGlicemiaBaixa){
            glucoseView.setBackgroundResource(R.drawable.circle_hipo);
        }  else if (glucoseValue > mGlicemiaBaixa && glucoseValue < mGlicemiaAlta){
            glucoseView.setBackgroundResource(R.drawable.circle_normal);
        } else {
            glucoseView.setBackgroundResource(R.drawable.circle_hiper);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();
    }

    public void refreshData(){
        GlucoseMealsReportDAO glucoseMealsReportDAO = new GlucoseMealsReportDAO(mContext);
        ArrayList<GlucoseMealsReport> glucoseMealsReports = glucoseMealsReportDAO.fetchAllByLeftJoin();
        mData = glucoseMealsReports;
    }

    public boolean deleteRecord(int mSelectedItem) {
        //TODO implementar
        return false;
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
