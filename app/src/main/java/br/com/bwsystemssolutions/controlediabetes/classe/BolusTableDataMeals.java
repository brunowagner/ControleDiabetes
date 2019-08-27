package br.com.bwsystemssolutions.controlediabetes.classe;

public class BolusTableDataMeals {

    private int mealId;
    private String meal;
    private double mInsulin;

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public double getmInsulin() {
        return mInsulin;
    }

    public void setmInsulin(double mInsulin) {
        this.mInsulin = mInsulin;
    }
}
