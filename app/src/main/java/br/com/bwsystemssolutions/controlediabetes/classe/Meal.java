package br.com.bwsystemssolutions.controlediabetes.classe;

public class Meal {
    public static final String TABLE_NAME = "meals";
    public static final String COLUMN_MEAL_NAME = "meal";
    public static final String COLUMN_MEAL_SORT = "sort";
    public static final String COLUMN_MEAL_SOURCE = "source";


    private int id;
    private String meal;
    private String source;
    private String sortment;


    public static final String BUNDLE_STRING_KEY = Meal.class.toString();
    public static final String SOURCE_FROM_APP = "app";
    public static final String SOURCE_FROM_user = "user";
}
