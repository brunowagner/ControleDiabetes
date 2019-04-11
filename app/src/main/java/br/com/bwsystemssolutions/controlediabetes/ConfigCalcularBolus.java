package br.com.bwsystemssolutions.controlediabetes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract;
import br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusDBHelper;

import static br.com.bwsystemssolutions.controlediabetes.data.CalculoDeBolusContract.*;

public class ConfigCalcularBolus extends AppCompatActivity {

    SQLiteDatabase mDb;
    RecyclerView mDadosParaCalcuroReyclerView;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_calcular_bolus);

        mDadosParaCalcuroReyclerView = (RecyclerView) findViewById(R.id.rv_dados_para_calculo);

        // Configurando o banco de dados
        CalculoDeBolusDBHelper dbHelper = new CalculoDeBolusDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        mCursor = getAllData();
    }

    private Cursor getAllData() {
        return mDb.query(TimeBlockEntry.TABLE_NAME,
                null, null, null, null,null,
                TimeBlockEntry.COLUMN_INITIAL_TIME_NAME);
    }
    
}
