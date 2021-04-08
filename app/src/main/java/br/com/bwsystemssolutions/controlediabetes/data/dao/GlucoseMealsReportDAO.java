package br.com.bwsystemssolutions.controlediabetes.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.GlucoseMealsReport;
import br.com.bwsystemssolutions.controlediabetes.classe.Record;
import br.com.bwsystemssolutions.controlediabetes.classe.Utilidades;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;
import br.com.bwsystemssolutions.controlediabetes.interfaces.BasicDAO;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;

public class GlucoseMealsReportDAO implements BasicDAO<GlucoseMealsReport> {
    private CalculoDeBolusDBHelper dbHelper;
    private Context mContext;
    //private String TABLE_NAME = CalculoDeBolusContract.RecordEntry.TABLE_NAME;
    //private String COLUMN_ID_NAME = CalculoDeBolusContract.RecordEntry._ID;

    public  GlucoseMealsReportDAO (Context context){
        dbHelper = new CalculoDeBolusDBHelper(context);
        mContext = context;
    }

    @Override
    public ArrayList<GlucoseMealsReport> fetchAll(){
        RecordDAO recordDAO = new RecordDAO(mContext);
        final ArrayList<Record> records = recordDAO.fetchWithMeals(false);
        final ArrayList<GlucoseMealsReport> glucoseMealsReports = parceRecordsToGlucoseMealsReports(records);
        return glucoseMealsReports;
    }

    public ArrayList<GlucoseMealsReport> fetchAllByLeftJoin(){
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String sqlInstruction = getLeftJoinInstruction();
        final Cursor cursor  = db.rawQuery(sqlInstruction,null);
        ArrayList<GlucoseMealsReport> glucoseMealsReports = parseToGlucoseMealsReports(cursor);
        db.close();
        return glucoseMealsReports;
    }

    //-- Parses --------------------------------------------------------------------------------

    private ArrayList<GlucoseMealsReport> parseToGlucoseMealsReports(Cursor cursor) {
        ArrayList<GlucoseMealsReport> glucoseMealsReports = new ArrayList<>();

        while(cursor.moveToNext()){
            GlucoseMealsReport glucoseMealsReport = new GlucoseMealsReport();
            glucoseMealsReport.setData(Utilidades.convertStringToDate(cursor.getString(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DATE_NAME))));
            glucoseMealsReport.setCarboCafe(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_BREAKFAST_CARBO_NAME)));
            glucoseMealsReport.setGlicoseCafe(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_BREAKFAST_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulCafe(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_BREAKFAST_INSUL_NAME)));
            glucoseMealsReport.setCarboColacao(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_BRUNCH_CARBO_NAME)));
            glucoseMealsReport.setGlicoseColacao(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_BRUNCH_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulColacao(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_BRUNCH_INSUL_NAME)));
            glucoseMealsReport.setCarboAlmoco(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_LUNCH_CARBO_NAME)));
            glucoseMealsReport.setGlicoseAlmoco(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_LUNCH_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulAlmoco(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_LUNCH_INSUL_NAME)));
            glucoseMealsReport.setCarboLanche(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_TEA_CARBO_NAME)));
            glucoseMealsReport.setGlicoseLanche(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_TEA_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulLanche(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_TEA_INSUL_NAME)));
            glucoseMealsReport.setCarboJantar(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DINNER_CARBO_NAME)));
            glucoseMealsReport.setGlicoseJantar(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DINNER_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulJantar(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DINNER_INSUL_NAME)));
            glucoseMealsReport.setCarboCeia(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_SUPPER_CARBO_NAME)));
            glucoseMealsReport.setGlicoseCeia(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_SUPPER_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulCeia(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_SUPPER_INSUL_NAME)));
            glucoseMealsReport.setCarboMadrugada(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DAWN_CARBO_NAME)));
            glucoseMealsReport.setGlicoseMadrugada(cursor.getInt(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DAWN_GLUCOSE_NAME)));
            glucoseMealsReport.setInsulMadrugada(cursor.getDouble(cursor.getColumnIndex(CalculoDeBolusContract.GlucoseMealsReportEntry.COLUMN_DAWN_INSUL_NAME)));
            glucoseMealsReports.add(glucoseMealsReport);
        }
        cursor.close();
        return glucoseMealsReports;
    }


    private String getLeftJoinInstruction() {
        final String colunas = "r.date, cafe, carboCafe, insulCafe, colacao, carboColacao,insulColacao,almoco,carboAlmoco,insulAlmoco,lanche,carboLanche,insulLanche,jantar,carboJantar,insulJantar,ceia,carboCeia,insulCeia,madrugada,carboMadrugada,insulMadrugada";
        final String tabelaDaEsquerda = "(select _id, strftime('%d-%m-%Y',date_time) as date, count(date_time) from records group by strftime('%m-%d-%Y',date_time)) r";
        final String tabelaCafe = "(select strftime('%d-%m-%Y',date_time) as date, glucose as cafe, carbohydrate as carboCafe, fast_insulin as insulCafe from records where meal like 'café da manhã' group by strftime('%m-%d-%Y',date_time)) cafe";
        final String tabelaColacao = "(select strftime('%d-%m-%Y',date_time) as date, glucose as colacao, carbohydrate as carboColacao, fast_insulin as insulColacao from records where meal like 'colação' group by strftime('%m-%d-%Y',date_time)) col";
        final String tabelaAlmoco = "(select strftime('%d-%m-%Y',date_time) as date, glucose as almoco, carbohydrate as carboAlmoco, fast_insulin as insulAlmoco from records where meal like 'almoço' group by strftime('%m-%d-%Y',date_time)) alm";
        final String tabelaLanche = "(select strftime('%d-%m-%Y',date_time) as date, glucose as lanche, carbohydrate as carboLanche, fast_insulin as insulLanche from records where meal like 'lanche da tarde' group by strftime('%m-%d-%Y',date_time)) lan";
        final String tabelaJantar = "(select strftime('%d-%m-%Y',date_time) as date, glucose as jantar, carbohydrate as carboJantar, fast_insulin as insulJantar from records where meal like 'jantar' group by strftime('%m-%d-%Y',date_time)) jan";
        final String tabelaCeia = "(select strftime('%d-%m-%Y',date_time) as date, glucose as ceia, carbohydrate as carboCeia, fast_insulin as insulCeia from records where meal like 'ceia' group by strftime('%m-%d-%Y',date_time)) ceia";
        final String tabelaMadrugada = "(select strftime('%d-%m-%Y',date_time) as date, glucose as madrugada, carbohydrate as carboMadrugada, fast_insulin as insulMadrugada from records where meal like 'madrugada' group by strftime('%m-%d-%Y',date_time)) mad";

        return "SELECT " + colunas + " FROM " + tabelaDaEsquerda +
                " LEFT JOIN " + tabelaCafe + " on r.date = cafe.date " +
                " LEFT JOIN " + tabelaColacao + " on r.date = col.date " +
                " LEFT JOIN " + tabelaAlmoco + " on r.date = alm.date " +
                " LEFT JOIN " + tabelaLanche + " on r.date = lan.date " +
                " LEFT JOIN " + tabelaJantar + " on r.date = jan.date " +
                " LEFT JOIN " + tabelaCeia + " on r.date = ceia.date " +
                " LEFT JOIN " + tabelaMadrugada + " on r.date = mad.date ";
    }


    private ArrayList<GlucoseMealsReport> parceRecordsToGlucoseMealsReports(ArrayList<Record> records) {
        ArrayList<GlucoseMealsReport> glucoseMealsReports = new ArrayList<>();

        GlucoseMealsReport glucoseMealsReport=null;

        String dataAnterior="";
        for (Record r : records) {
            if (!dataAnterior.equals(Utilidades.convertDateToString(r.getDate(), "dd/mm/YYY"))){
                glucoseMealsReport = new GlucoseMealsReport();
                glucoseMealsReports.add(glucoseMealsReport);
            }

            glucoseMealsReport.setData(r.getDate());
            setGlucoseCarboAndInsulin(r, glucoseMealsReport);

            dataAnterior = Utilidades.convertDateToString(r.getDate(), "dd/mm/YYY");
        }
        return glucoseMealsReports;
    }

    private void setGlucoseCarboAndInsulin(Record r, GlucoseMealsReport g){
        if (r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_breakfast))){
            g.setCarboCafe(r.getCarbohydrate());
            g.setGlicoseCafe(r.getGlucose());
            g.setInsulCafe(r.getFastInsulin());
        }else if(r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_brunch))) {
            g.setCarboColacao(r.getCarbohydrate());
            g.setGlicoseColacao(r.getGlucose());
            g.setInsulColacao(r.getFastInsulin());
        }else if(r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_Lunch))){
            g.setCarboAlmoco(r.getCarbohydrate());
            g.setGlicoseAlmoco(r.getGlucose());
            g.setInsulAlmoco(r.getFastInsulin());
        }else if(r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_tea))){
            g.setCarboLanche(r.getCarbohydrate());
            g.setGlicoseLanche(r.getGlucose());
            g.setInsulLanche(r.getFastInsulin());
        }else if(r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_dinner))){
            g.setCarboJantar(r.getCarbohydrate());
            g.setGlicoseJantar(r.getGlucose());
            g.setInsulJantar(r.getFastInsulin());
        }else if(r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_supper))){
            g.setCarboCeia(r.getCarbohydrate());
            g.setGlicoseCeia(r.getGlucose());
            g.setInsulCeia(r.getFastInsulin());
        }else if(r.getMeal().equals(mContext.getResources().getString(R.string.meals_name_dawn))){
            g.setCarboMadrugada(r.getCarbohydrate());
            g.setGlicoseMadrugada(r.getGlucose());
            g.setInsulMadrugada(r.getFastInsulin());
        }
    }

    @Override
    public boolean add(GlucoseMealsReport object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean update(GlucoseMealsReport object) {
        return false;
    }


}
