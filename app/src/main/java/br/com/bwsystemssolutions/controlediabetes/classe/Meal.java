package br.com.bwsystemssolutions.controlediabetes.classe;

public class Meal {
    private int id;
    private String meal;
    private String source;
    private int sort;


    public static final String BUNDLE_STRING_KEY = Meal.class.toString();
    public static final String SOURCE_FROM_APP = "app";
    public static final String SOURCE_FROM_user = "user";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
