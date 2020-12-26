package com.futureservices.muneem.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futureservices.muneem.Models.ModelAccountRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;

public class AdapterMAR extends RecyclerView.Adapter<AdapterMAR.MARViewHolder> {

    private ArrayList<ModelAccountRecord> items;
    int rowIndex = -1;

    public AdapterMAR(ArrayList<ModelAccountRecord> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AdapterMAR.MARViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_view_money_record, parent, false);
        AdapterMAR.MARViewHolder mcr_VH = new AdapterMAR.MARViewHolder(view);
        return mcr_VH;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMAR.MARViewHolder holder, int position) {
        final ModelAccountRecord currentItem = items.get(position);
        holder.txtDate.setText(currentItem.getDate());
        holder.txtAmount.setText(String.valueOf(currentItem.getAmount()));
        holder.txtAvailable.setText(currentItem.getAvailableAccount().toString());
        holder.txtSubject.setText(currentItem.getSubject());

        if(currentItem.getDirection() == -1)
        {
            holder.txtAmount.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else{
            holder.txtAmount.setBackgroundColor(Color.parseColor("#00ff00"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MARViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtAmount;
        TextView txtAvailable;
        TextView txtSubject;
        LinearLayout linearLayout;
        View mView;


        public MARViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDateOfExpense);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtAvailable = itemView.findViewById(R.id.txtAvailable);
            txtSubject = itemView.findViewById(R.id.txtSubject);

            linearLayout = itemView.findViewById(R.id.LinearLayoutList);
            mView = itemView;
        }
    }
}

