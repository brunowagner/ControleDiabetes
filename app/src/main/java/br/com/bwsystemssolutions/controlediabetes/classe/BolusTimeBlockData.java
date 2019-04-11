package br.com.bwsystemssolutions.controlediabetes.classe;

public class BolusTimeBlockData {

	public String start;
    public String end;
    public int relation;
    public int tarjet;
    public int sensibilityFactor;

	public BolusTimeBlockData(){

    }

    public BolusTimeBlockData(String start, String end, int relation, int tarjet, int sensibilityFactor) {
        this.start = start;
        this.end = end;
        this.relation = relation;
        this.tarjet = tarjet;
        this.sensibilityFactor = sensibilityFactor;
    }
}
