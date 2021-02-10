package br.com.bwsystemssolutions.controlediabetes.interfaces;

import android.content.ContentValues;

import java.util.ArrayList;

public interface BasicDAO<T> {
    public ArrayList<T> fetchAll();
    public boolean add(T object);
    public boolean delete(int id);
    public boolean update(T object);
}
