package br.com.bwsystemssolutions.controlediabetes.classe;

import java.io.Serializable;
import java.util.ArrayList;

public class BolusTable3Data implements Serializable {

    public static final String BUNDLE_STRING_KEY = Record.class.toString();

    private ArrayList<Integer> ids;
    private int id;
    private int glucose;
//    private ArrayList<BolusTableDataMeals> bolusTableDataMeals;

    private Bolus bolus1CafeDaManha;
    private Bolus bolus2Colacao;
    private Bolus bolus3Almoco;
    private Bolus bolus4Lanche;
    private Bolus bolus5Jantar;
    private Bolus bolus6Ceia;
    private Bolus bolus7Madrugada;

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
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

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public void addId(int id) {
        if (this.ids == null) {
            this.ids = new ArrayList<>();
        }
        this.ids.add(id);
    }

    public Bolus getBolus1CafeDaManha() {
        return bolus1CafeDaManha;
    }

    public void setBolus1CafeDaManha(Bolus bolus1CafeDaManha) {
        this.bolus1CafeDaManha = bolus1CafeDaManha;
    }

    public Bolus getBolus2Colacao() {
        return bolus2Colacao;
    }

    public void setBolus2Colacao(Bolus bolus2Colacao) {
        this.bolus2Colacao = bolus2Colacao;
    }

    public Bolus getBolus3Almoco() {
        return bolus3Almoco;
    }

    public void setBolus3Almoco(Bolus bolus3Almoco) {
        this.bolus3Almoco = bolus3Almoco;
    }

    public Bolus getBolus4Lanche() {
        return bolus4Lanche;
    }

    public void setBolus4Lanche(Bolus bolus4Lanche) {
        this.bolus4Lanche = bolus4Lanche;
    }

    public Bolus getBolus5Jantar() {
        return bolus5Jantar;
    }

    public void setBolus5Jantar(Bolus bolus5Jantar) {
        this.bolus5Jantar = bolus5Jantar;
    }

    public Bolus getBolus6Ceia() {
        return bolus6Ceia;
    }

    public void setBolus6Ceia(Bolus bolus6Ceia) {
        this.bolus6Ceia = bolus6Ceia;
    }

    public Bolus getBolus7Madrugada() {
        return bolus7Madrugada;
    }

    public void setBolus7Madrugada(Bolus bolus7Madrugada) {
        this.bolus7Madrugada = bolus7Madrugada;
    }

    public ArrayList<Bolus> getBolusArrayList(){
        ArrayList<Bolus> bolusArrayList = new ArrayList<>();

        bolusArrayList.add(bolus1CafeDaManha);
        bolusArrayList.add(bolus2Colacao);
        bolusArrayList.add(bolus3Almoco);
        bolusArrayList.add(bolus4Lanche);
        bolusArrayList.add(bolus5Jantar);
        bolusArrayList.add(bolus6Ceia);
        bolusArrayList.add(bolus7Madrugada);
        return bolusArrayList;
    }




    //Queries


}
