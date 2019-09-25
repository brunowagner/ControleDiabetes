package br.com.bwsystemssolutions.controlediabetes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.bwsystemssolutions.controlediabetes.R;
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableDataMeals;

public class BolusTableMealAdapter extends RecyclerView.Adapter<BolusTableMealAdapter.BolusTableMealAdapterViewHolder> {

    private ArrayList<BolusTableDataMeals> mBolusTableDataMeals;

    public BolusTableMealAdapter(ArrayList<BolusTableDataMeals> mBolusTableDataMeals) {
        this.mBolusTableDataMeals = mBolusTableDataMeals;
    }

    @NonNull
    @Override
    public BolusTableMealAdapter.BolusTableMealAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdFromListItem = R.layout.list_item_row_bolus_table_child;
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdFromListItem, viewGroup, shouldAttachToParentImmediately);
        return new BolusTableMealAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BolusTableMealAdapterViewHolder bolusTableMealAdapterViewHolder, int i) {
        BolusTableDataMeals bolusTableDataMeals = mBolusTableDataMeals.get(i);
        bolusTableMealAdapterViewHolder.mMealTextView.setText(bolusTableDataMeals.getMeal());
        bolusTableMealAdapterViewHolder.mInsulin.setText(String.valueOf(bolusTableDataMeals.getmInsulin()));
    }


    @Override
    public int getItemCount() {
        return mBolusTableDataMeals.size();
    }










    public class BolusTableMealAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView mMealTextView;
        private TextView mInsulin;

        public BolusTableMealAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mMealTextView = itemView.findViewById(R.id.tv_meal);
            mInsulin = itemView.findViewById(R.id.tv_insulin);
        }
    }
}
