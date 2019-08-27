package br.com.bwsystemssolutions.controlediabetes.classe;

import java.util.ArrayList;

public class BolusTableData {

    private int glucoseId;
    private int glucose;
    private ArrayList<BolusTableDataMeals> bolusTableDataMeals;

    public int getGlucoseId() {
        return glucoseId;
    }

    public void setGlucoseId(int glucoseId) {
        this.glucoseId = glucoseId;
    }

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public ArrayList<BolusTableDataMeals> getBolusTableDataMeals() {
        return bolusTableDataMeals;
    }

    public void setBolusTableDataMeals(ArrayList<BolusTableDataMeals> bolusTableDataMeals) {
        this.bolusTableDataMeals = bolusTableDataMeals;
    }
}
