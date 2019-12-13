package br.com.bwsystemssolutions.controlediabetes.classe;

public class Bolus {
    private int id;
    private int glucose;
    private int meal_id;
    private double bolus;

    public static final String BUNDLE_STRING_KEY = Meal.class.toString();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public double getBolus() {
        return bolus;
    }

    public void setBolus(double bolus) {
        this.bolus = bolus;
    }
}
