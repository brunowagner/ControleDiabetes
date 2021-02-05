package br.com.bwsystemssolutions.controlediabetes.classe;

public class Bolus2 {
    private int id;
    private int glucose;
    private int meal_id;
    private String meal;
    private double bolus;

    public static final String BUNDLE_STRING_KEY = Bolus2.class.toString();


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

    private Bolus2(Builder builder) {
        glucose = builder.glucose;
        meal_id = builder.meal_id;
        meal = builder.meal;
        bolus = builder.bolus;
    }


    public static class Builder {
        private int glucose;
        private int meal_id;
        private String meal;
        private double bolus;

        public Builder(){

        }

        public  Builder glucose(int glucose){
            this.glucose = glucose;
            return this;
        }

        public  Builder meal_id(int meal_id){
            this.meal_id = meal_id;
            return this;
        }

        public  Builder meal(String meal){
            this.meal = meal;
            return this;
        }

        public  Builder bolus(Double bolus){
            this.bolus = bolus;
            return this;
        }

        public Bolus2 build(){
            return new Bolus2(this);
        }



     }







}
