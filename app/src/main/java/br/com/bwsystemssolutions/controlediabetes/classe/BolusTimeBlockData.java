package br.com.bwsystemssolutions.controlediabetes.classe;

public class BolusTimeBlockData {

	public String start;
    public int relation;
    public int tarjet;
    public int sensibilityFactor;

	public BolusTimeBlockData(){

    }

    public BolusTimeBlockData(String start, int relation, int tarjet, int sensibilityFactor) {
        this.start = start;
        this.relation = relation;
        this.tarjet = tarjet;
        this.sensibilityFactor = sensibilityFactor;
    }
}
