package br.com.bwsystemssolutions.controlediabetes.classe;

import java.io.Serializable;

public class Bolus implements Serializable {
    private int id;
    private int glucose;
    private int meal_id;
    private String meal;
    private double bolus;

    public static final String BUNDLE_STRING_KEY = Bolus.class.toString();

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

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public double getBolus() {
        return bolus;
    }

    public void setBolus(double bolus) {
        this.bolus = bolus;
    }
}
