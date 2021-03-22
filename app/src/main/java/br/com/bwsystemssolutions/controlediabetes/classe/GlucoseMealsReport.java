package br.com.bwsystemssolutions.controlediabetes.classe;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlucoseMealsReport {
    private double insulCafe;
    private double insulColacao;
    private double insulAlmoco;
    private double insulLanche;
    private double insulJantar;
    private double insulCeia;
    private double insulMadrugada;
    private int carboCafe;
    private int carboColacao;
    private int carboAlmoco;
    private int carboLanche;
    private int carboJantar;
    private int carboCeia;
    private int carboMadrugada;
    private int glicoseCafe;
    private int glicoseColacao;
    private int glicoseAlmoco;
    private int glicoseLanche;
    private int glicoseJantar;
    private int glicoseCeia;
    private int glicoseMadrugada;
    private Date data;

    public double getInsulCafe() {
        return insulCafe;
    }

    public void setInsulCafe(double insulCafe) {
        this.insulCafe = insulCafe;
    }

    public double getInsulColacao() {
        return insulColacao;
    }

    public void setInsulColacao(double insulColacao) {
        this.insulColacao = insulColacao;
    }

    public double getInsulAlmoco() {
        return insulAlmoco;
    }

    public void setInsulAlmoco(double insulAlmoco) {
        this.insulAlmoco = insulAlmoco;
    }

    public double getInsulLanche() {
        return insulLanche;
    }

    public void setInsulLanche(double insulLanche) {
        this.insulLanche = insulLanche;
    }

    public double getInsulJantar() {
        return insulJantar;
    }

    public void setInsulJantar(double insulJantar) {
        this.insulJantar = insulJantar;
    }

    public double getInsulCeia() {
        return insulCeia;
    }

    public void setInsulCeia(double insulCeia) {
        this.insulCeia = insulCeia;
    }

    public double getInsulMadrugada() {
        return insulMadrugada;
    }

    public void setInsulMadrugada(double insulMadrugada) {
        this.insulMadrugada = insulMadrugada;
    }

    public int getCarboCafe() {
        return carboCafe;
    }

    public void setCarboCafe(int carboCafe) {
        this.carboCafe = carboCafe;
    }

    public int getCarboColacao() {
        return carboColacao;
    }

    public void setCarboColacao(int carboColacao) {
        this.carboColacao = carboColacao;
    }

    public int getCarboAlmoco() {
        return carboAlmoco;
    }

    public void setCarboAlmoco(int carboAlmoco) {
        this.carboAlmoco = carboAlmoco;
    }

    public int getCarboLanche() {
        return carboLanche;
    }

    public void setCarboLanche(int carboLanche) {
        this.carboLanche = carboLanche;
    }

    public int getCarboJantar() {
        return carboJantar;
    }

    public void setCarboJantar(int carboJantar) {
        this.carboJantar = carboJantar;
    }

    public int getCarboCeia() {
        return carboCeia;
    }

    public void setCarboCeia(int carboCeia) {
        this.carboCeia = carboCeia;
    }

    public int getCarboMadrugada() {
        return carboMadrugada;
    }

    public void setCarboMadrugada(int carboMadrugada) {
        this.carboMadrugada = carboMadrugada;
    }

    public int getGlicoseCafe() {
        return glicoseCafe;
    }

    public void setGlicoseCafe(int glicoseCafe) {
        this.glicoseCafe = glicoseCafe;
    }

    public int getGlicoseColacao() {
        return glicoseColacao;
    }

    public void setGlicoseColacao(int glicoseColacao) {
        this.glicoseColacao = glicoseColacao;
    }

    public int getGlicoseAlmoco() {
        return glicoseAlmoco;
    }

    public void setGlicoseAlmoco(int glicoseAlmoco) {
        this.glicoseAlmoco = glicoseAlmoco;
    }

    public int getGlicoseLanche() {
        return glicoseLanche;
    }

    public void setGlicoseLanche(int glicoseLanche) {
        this.glicoseLanche = glicoseLanche;
    }

    public int getGlicoseJantar() {
        return glicoseJantar;
    }

    public void setGlicoseJantar(int glicoseJantar) {
        this.glicoseJantar = glicoseJantar;
    }

    public int getGlicoseCeia() {
        return glicoseCeia;
    }

    public void setGlicoseCeia(int glicoseCeia) {
        this.glicoseCeia = glicoseCeia;
    }

    public int getGlicoseMadrugada() {
        return glicoseMadrugada;
    }

    public void setGlicoseMadrugada(int glicoseMadrugada) {
        this.glicoseMadrugada = glicoseMadrugada;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return Date String with the format 'EE', where EE is de weekday.
     */
    public String getWeekDayString(){
        Date date = getData();
        if (date == null ){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EE");

        return sdf.format(date);
    }

    /**
     * @return Date String with the format 'dd/MM/yyyy'.
     */
    public String getDateString(){
        Date date = getData();
        if (date == null ){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);
    }


}
