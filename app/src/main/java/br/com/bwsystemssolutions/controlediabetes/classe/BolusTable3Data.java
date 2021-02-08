package br.com.bwsystemssolutions.controlediabetes.classe;

import java.io.Serializable;
import java.util.ArrayList;

public class BolusTable3Data implements Serializable {

    public static final String BUNDLE_STRING_KEY = BolusTable3Data.class.toString();
    public static final int MEAL_ID_CAFE_DA_MANHA = 1;
    public static final int MEAL_ID_COLACAO = 2;
    public static final int MEAL_ID_ALMOCO = 3;
    public static final int MEAL_ID_LANCHE = 4;
    public static final int MEAL_ID_JANTAR = 5;
    public static final int MEAL_ID_CEIA = 6;
    public static final int MEAL_ID_MADRUGADA = 7;

    private ArrayList<Integer> bolusIds;
    private int id;
    private int glucose;
//    private ArrayList<BolusTableDataMeals> bolusTableDataMeals;

    private Double bolus1CafeDaManha;
    private Double bolus2Colacao;
    private Double bolus3Almoco;
    private Double bolus4Lanche;
    private Double bolus5Jantar;
    private Double bolus6Ceia;
    private Double bolus7Madrugada;
    private ArrayList<Bolus> bolusArrayList;

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
        this.id = glucose;
    }

//    public ArrayList<BolusTableDataMeals> getBolusTableDataMeals() {
//        return bolusTableDataMeals;
//    }
//
//    public void setBolusTableDataMeals(ArrayList<BolusTableDataMeals> bolusTableDataMeals) {
//        this.bolusTableDataMeals = bolusTableDataMeals;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getBolusIds() {
        return bolusIds;
    }

    public void setBolusIds(ArrayList<Integer> bolusIds) {
        this.bolusIds = bolusIds;
    }

    public void addBolusIds(int id) {
        if (this.bolusIds == null) {
            this.bolusIds = new ArrayList<>();
        }
        this.bolusIds.add(id);
    }

    public Double getBolus1CafeDaManha() {
        return bolus1CafeDaManha;
    }

    public void setBolus1CafeDaManha(Double bolus1CafeDaManha) {
        this.bolus1CafeDaManha = bolus1CafeDaManha;
    }

    public Double getBolus2Colacao() {
        return bolus2Colacao;
    }

    public void setBolus2Colacao(Double bolus2Colacao) {
        this.bolus2Colacao = bolus2Colacao;
    }

    public Double getBolus3Almoco() {
        return bolus3Almoco;
    }

    public void setBolus3Almoco(Double bolus3Almoco) {
        this.bolus3Almoco = bolus3Almoco;
    }

    public Double getBolus4Lanche() {
        return bolus4Lanche;
    }

    public void setBolus4Lanche(Double bolus4Lanche) {
        this.bolus4Lanche = bolus4Lanche;
    }

    public Double getBolus5Jantar() {
        return bolus5Jantar;
    }

    public void setBolus5Jantar(Double bolus5Jantar) {
        this.bolus5Jantar = bolus5Jantar;
    }

    public Double getBolus6Ceia() {
        return bolus6Ceia;
    }

    public void setBolus6Ceia(Double bolus6Ceia) {
        this.bolus6Ceia = bolus6Ceia;
    }

    public Double getBolus7Madrugada() {
        return bolus7Madrugada;
    }

    public void setBolus7Madrugada(Double bolus7Madrugada) {
        this.bolus7Madrugada = bolus7Madrugada;
    }

    public ArrayList<Bolus> getBolusArrayList(){
        return bolusArrayList;
    }

    public void setBolusArrayList(ArrayList<Bolus> bolusArrayList) {
        this.bolusArrayList = bolusArrayList;

            for (Bolus b : bolusArrayList){

                this.setId(b.getGlucose());
                this.addBolusIds(b.getId());
                this.setGlucose(b.getGlucose());

                switch (b.getMeal_id()){
                    case 1: this.setBolus1CafeDaManha(b.getBolus());
                        break;
                    case 2: this.setBolus2Colacao(b.getBolus());
                        break;
                    case 3: this.setBolus3Almoco(b.getBolus());
                        break;
                    case 4: this.setBolus4Lanche(b.getBolus());
                        break;
                    case 5: this.setBolus5Jantar(b.getBolus());
                        break;
                    case 6: this.setBolus6Ceia(b.getBolus());
                        break;
                    case 7: this.setBolus7Madrugada(b.getBolus());
                    default:
                }
            }
    }
}
