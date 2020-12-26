package com.futureservices.muneem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelMilkRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;

public class AdapterMMR extends RecyclerView.Adapter<AdapterMMR.MMRViewHolder> {

    private ArrayList<ModelMilkRecord> items;
    int rowIndex = -1;
    GlobalVariables globals = GlobalVariables.getGlobalInstance();
    public AdapterMMR(ArrayList<ModelMilkRecord> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MMRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.milt_record_list, parent, false);
        AdapterMMR.MMRViewHolder mmr_VH = new AdapterMMR.MMRViewHolder(view);
        return mmr_VH;
    }

    @Override
    public void onBindViewHolder(@NonNull MMRViewHolder holder, int position) {
        final ModelMilkRecord currentItem = items.get(position);
        String Date = globals.GetUIDate(currentItem.getDate());
        holder.txtDate.setText(Date);
        holder.txtMilkQuantity.setText(String.valueOf(currentItem.getMilkQuantity()));
        holder.txtMilkPrice.setText(String.valueOf(currentItem.getMilkRate()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MMRViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate;
        TextView txtMilkQuantity;
        TextView txtMilkPrice;

        public MMRViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtCurrentDate);
            txtMilkQuantity = itemView.findViewById(R.id.txtMilkQuantity);
            txtMilkPrice = itemView.findViewById(R.id.txtMilkPrice);
        }
    }
}
