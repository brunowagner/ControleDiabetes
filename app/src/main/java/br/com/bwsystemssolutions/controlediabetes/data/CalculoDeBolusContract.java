package br.com.bwsystemssolutions.controlediabetes.data;

import android.provider.BaseColumns;

/**
 * Classe que define as Tabelas
 */
public class CalculoDeBolusContract {

    /**
     * Cada classe interna que define uma tabela (neste caso é a tabela de blocos de tempo)
     */
    // classe interna que implementa a interface 'BaseColumns'
    public static  final class TimeBlockEntry implements BaseColumns{
        //strings finais que definem as colunas
        public static final String TABLE_NAME = "timeBlocks";
        public static final String COLUMN_INITIAL_TIME_NAME = "initialTime";
        public static final String COLUMN_RELATION_NAME = "relation";
        public static final String COLUMN_SENSITIVITY_FACTOR_NAME = "sensitivityFactor";
        public static final String COLUMN_TARGET_NAME = "target";
    }
    
    public static  final class RecordEntry implements BaseColumns{
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_DATE_TIME_NAME = "date_time";
        public static final String COLUMN_GLUCOSE_NAME = "glucose";
        public static final String COLUMN_MEAL_NAME = "meal";
        public static final String COLUMN_MEAL_TIME_NAME = "meal_time";
        public static final String COLUMN_EVENT_NAME = "event";
        public static final String COLUMN_CARBOHYDRATE_NAME = "carbohydrate";
        public static final String COLUMN_FAST_INSULIN_NAME = "fast_insulin";
        public static final String COLUMN_BASAL_INSULIN_NAME = "basal_insulin";
        public static final String COLUMN_SICK_NAME = "sick";
        public static final String COLUMN_MEDICAMENT_NAME = "medicament";
        public static final String COLUMN_NOTE_NAME = "note";
    }
    
    public static  final class EventEntry implements BaseColumns{
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_EVENT_NAME = "event";
        public static final String COLUMN_SORT_NAME = "sort";
        public static final String COLUMN_EVENT_SOURCE = "source";
    }

    public static  final class MealEntry implements BaseColumns{
        public static final String TABLE_NAME = "meals";
        public static final String COLUMN_MEAL_NAME = "meal";
        public static final String COLUMN_SORT_NAME = "sort";
        public static final String COLUMN_MEAL_SOURCE = "source";
    }
}
