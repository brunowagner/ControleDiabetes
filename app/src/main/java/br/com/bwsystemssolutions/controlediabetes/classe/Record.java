package br.com.bwsystemssolutions.controlediabetes.classe;

import java.io.Serializable;

public class Record implements Serializable {

	  public int id;
    public Date date;
    public String weekDay;
    public int glucose;
    public String event;
    public int carbohydrate;
    public double fastInsulin;
    public double basalInsulin;
    public boolean sick;
    public boolean medicament;
    public String note;
    
    public Record(){
    }

}
