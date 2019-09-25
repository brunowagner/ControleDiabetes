package br.com.bwsystemssolutions.controlediabetes.classe;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements Serializable {

    private int id;
    private Date date;
    private int glucose;
    private String event;
    private String meal;
    private String mealTime;
    private int carbohydrate;
    private double fastInsulin;
    private double basalInsulin;
    private boolean sick;
    private boolean medicament;
    private String note;
    
    public static final String BUNDLE_STRING_KEY = Record.class.toString();
    
    public Record(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateFromStringDateSqlite(String dateString, SimpleDateFormat format) {

        try {
            Date parsed = format.parse(dateString);
            this.date = parsed;
        } catch (ParseException e) {
            //TODO: codifical algo para que o app não trave.
            e.printStackTrace();
        }

    }

    public void setDateFromStringDateSqlite(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        setDateFromStringDateSqlite(dateString, sdf);
    }


    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getFastInsulin() {
        return fastInsulin;
    }

    public void setFastInsulin(double fastInsulin) {
        this.fastInsulin = fastInsulin;
    }

    public double getBasalInsulin() {
        return basalInsulin;
    }

    public void setBasalInsulin(double basalInsulin) {
        this.basalInsulin = basalInsulin;
    }

    public boolean isSick() {
        return sick;
    }

    public void setSick(boolean sick) {
        this.sick = sick;
    }

    public boolean isMedicament() {
        return medicament;
    }

    public void setMedicament(boolean medicament) {
        this.medicament = medicament;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWeekDay() {
        Date date = getDate();
        if (date == null ){
            return null;
        }
        //Locale brasil = new Locale("pt","BR");
        //SimpleDateFormat sdf = new SimpleDateFormat("EE", brasil);
        SimpleDateFormat sdf = new SimpleDateFormat("EE");

        return sdf.format(date);
    }

    public String getDateTimeWeekDay(){
        Date date = getDate();
        if (date == null ){
            return null;
        }
        //Locale brasil = new Locale("pt","BR");
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm EE", brasil);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm EE");

        return sdf.format(date);
    }

    public String getDateWeekDayString(){
        Date date = getDate();
        if (date == null ){
            return null;
        }
        //Locale brasil = new Locale("pt","BR");
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm EE", brasil);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy    EE");

        return sdf.format(date);
    }

    public String getTime(){
        Date date = getDate();
        if (date == null ){
            return null;
        }
        //Locale brasil = new Locale("pt","BR");
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm EE", brasil);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(date);
    }
}
