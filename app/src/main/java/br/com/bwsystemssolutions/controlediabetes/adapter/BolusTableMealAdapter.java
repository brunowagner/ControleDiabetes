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
import br.com.bwsystemssolutions.controlediabetes.classe.BolusTableData;

public class BolusTableMealAdapter extends RecyclerView.Adapter<BolusTableMealAdapter.BolusTableMealAdapterViewHolder> {

    private ArrayList<BolusTableData> mBolusTableData;
    private Double[] mBolusArray;

    @NonNull
    @Override
    public BolusTableMealAdapter.BolusTableMealAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdFromListItem = R.layout.list_item_row_bolus_table_child;
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdFromListItem, viewGroup,shouldAttachToParentImmediately);
        return new BolusTableMealAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BolusTableMealAdapterViewHolder bolusTableMealAdapterViewHolder, int i) {
        double item = mBolusArray[i];



    }


    @Override
    public int getItemCount() {
        return 0;
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
