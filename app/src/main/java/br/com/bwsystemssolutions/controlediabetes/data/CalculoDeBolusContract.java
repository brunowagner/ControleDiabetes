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
        public static final String COLUMN_SOURCE_NAME = "source";
        public static final String SOURCE_VALUE_APP = "app";
        public static final String SOURCE_VALUE_USER = "user";
    }

    public static  final class MealEntry implements BaseColumns{
        public static final String TABLE_NAME = "meals";
        public static final String COLUMN_MEAL_NAME = "meal";
        public static final String COLUMN_SORT_NAME = "sort";
        public static final String COLUMN_SOURCE_NAME = "source";
        public static final String SOURCE_VALUE_APP = "app";
    }

    public static  final class GlucoseEntry implements BaseColumns{
        public static final String TABLE_NAME = "glucoses";
        public static final String COLUMN_GLUCOSE_NAME = "glucose";
    }

    public static  final class BolusEntry implements BaseColumns{
        public static final String TABLE_NAME = "bolus";
        public static final String COLUMN_GLUCOSE_NAME = "glucose";
        public static final String COLUMN_MEAL_ID_NAME = "meal_id";
        public static final String COLUMN_BOLUS_NAME = "bolus";
    }

    //BolusTable2 sendo usado no lugar
//    public  static final class BolusTableEntry implements BaseColumns{
//        public static final String COLUMN_GLUCOSE_ID_NAME = "glucose_id";
//        public static final String COLUMN_GLUCOSE_NAME = "glucose";
//        public static final String COLUMN_MEAL_ID_NAME = "meal_id";
//        public static final String COLUMN_MEAL_NAME = "meal";
//        public static final String COLUMN_INSULIN_NAME = "insulin";
//    }
//
//    public static final class BolusTableQuery{
//
//        public static final String FETCH_ALL_DATA =
//                "SELECT " +
//                        GlucoseEntry.TABLE_NAME + "." + GlucoseEntry._ID + " AS " + BolusTableEntry.COLUMN_GLUCOSE_ID_NAME + ", " +
//                        GlucoseEntry.TABLE_NAME + "." + GlucoseEntry.COLUMN_GLUCOSE_NAME + " AS " + BolusTableEntry.COLUMN_GLUCOSE_NAME + ", " +
//                        MealEntry.TABLE_NAME + "." + MealEntry._ID + " AS " + BolusTableEntry.COLUMN_MEAL_ID_NAME + ", " +
//                        MealEntry.TABLE_NAME + "." + MealEntry.COLUMN_MEAL_NAME + " AS " + BolusTableEntry.COLUMN_MEAL_NAME + ", " +
//                        BolusEntry.TABLE_NAME + "." + BolusEntry.COLUMN_BOLUS_NAME + " AS " + BolusTableEntry.COLUMN_INSULIN_NAME + " " +
//
//                "FROM " +
//                        GlucoseEntry.TABLE_NAME + " " +
//
////                "INNER JOIN " +
////                        BolusEntry.TABLE_NAME + " on " +
////                        BolusEntry.TABLE_NAME + "." + BolusEntry.COLUMN_GLUCOSE_ID_NAME + " = " +
////                        GlucoseEntry.TABLE_NAME + "." + GlucoseEntry._ID + " " +
//
//                "INNER JOIN " +
//                        MealEntry.TABLE_NAME + " on " +
//                        MealEntry.TABLE_NAME + "." + MealEntry._ID + " = " +
//                        BolusEntry.TABLE_NAME + "." + BolusEntry.COLUMN_MEAL_ID_NAME + " " +
//
//                "ORDER BY " +
//                        BolusTableEntry.COLUMN_GLUCOSE_ID_NAME + ", " +
//                        BolusTableEntry.COLUMN_MEAL_ID_NAME + ";";


//        String sqlQuery =
//                "select " +
//                        "glucose._id, glucose.glucose, meals._id,  meals.meal, bolusTable.bolus " +
//                        "from " +
//                        "glucose " +
//                        "inner join bolusTable on bolusTable.glucose_id = glucose._id " +
//                        "inner join meals on meals._id = bolusTable.meal_id " +
//                        "order by " +
//                        "glucose_id,meal_id;";
//    }

    public  static final class BolusTable2Entry implements BaseColumns{
        public static final String TABLE_NAME = "bolusTable2";
        public static final String COLUMN_GLUCOSE_NAME = "glucose";
        public static final String COLUMN_BREAKFAST_NAME = "breakfast"; //café da manhã
        public static final String COLUMN_BRUNCH_NAME = "brunch"; // colação
        public static final String COLUMN_LUNCH_NAME = "lunch"; //almoço
        public static final String COLUMN_TEA_NAME = "tea"; //lanche da tarde
        public static final String COLUMN_DINNER_NAME = "dinner"; //jantar
        public static final String COLUMN_SUPPER_NAME = "supper"; //ceia
        public static final String COLUMN_DAWN_NAME = "dawn"; //madrugada
    }
}
