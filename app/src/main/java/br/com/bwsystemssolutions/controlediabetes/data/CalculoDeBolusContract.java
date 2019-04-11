package br.com.bwsystemssolutions.controlediabetes.data;

import android.provider.BaseColumns;

/**
 * Classe que define as Tabelas
 */
public class CalculoDeBolusContract {

    /**
     * Cada classe interna que define uma tabela (neste caso é a tabela de bloco de tempo)
     */
    // COMPLETED (1) Criar uma classe interna que implementa a interface 'BaseColumns'
    public static  final class TimeBlockEntry implements BaseColumns{
        //COMPLETED (2) criar as strings finais que definem as colunas
        public static final String TABLE_NAME = "timeBlock";
        public static final String COLUMN_INITIAL_TIME_NAME = "initialTime";
        public static final String COLUMN_FINAL_TIME_NAME = "finalTime";
        public static final String COLUMN_RELATION_NAME = "relation";
        public static final String COLUMN_SENSITIVITY_FACTOR_NAME = "sensitivityFactor";
        public static final String COLUMN_TARGET_NAME = "target";
    }
}
