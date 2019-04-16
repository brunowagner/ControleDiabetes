package br.com.bwsystemssolutions.controlediabetes.classe;

import java.io.Serializable;

public class BolusTimeBlockData implements Serializable {

	public String start;
    public int relation;
    public int tarjet;
    public int sensibilityFactor;

    public static final String BUNDLE_STRING_KEY = BolusTimeBlockData.class.toString();


	public BolusTimeBlockData(){

    }

    public BolusTimeBlockData(String start, int relation, int tarjet, int sensibilityFactor) {
        this.start = start;
        this.relation = relation;
        this.tarjet = tarjet;
        this.sensibilityFactor = sensibilityFactor;
    }
}
