package com.futureservices.muneem.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelCashRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;

public class AdapterMCR extends RecyclerView.Adapter<AdapterMCR.MCRViewHolder> {

    private ArrayList<ModelCashRecord> items;
    int rowIndex = -1;
    GlobalVariables globals = GlobalVariables.getGlobalInstance();
    public AdapterMCR(ArrayList<ModelCashRecord> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MCRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_view_money_record, parent, false);
        AdapterMCR.MCRViewHolder mcr_VH = new AdapterMCR.MCRViewHolder(view);
        return mcr_VH;
    }

    @Override
    public void onBindViewHolder(@NonNull MCRViewHolder holder, int position) {
        final ModelCashRecord currentItem = items.get(position);
        String Date = globals.GetUIDate(currentItem.getDate());
        holder.txtDate.setText(Date);
        holder.txtAmount.setText(String.valueOf(currentItem.getAmount()));
        holder.txtAvailable.setText(currentItem.getAvailableCash().toString());
        holder.txtSubject.setText(currentItem.getSubject());

        //region SetDirectionColor
        if(currentItem.getDirection() == -1)
        {
            holder.txtAmount.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        else{
            holder.txtAmount.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        //endregion
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MCRViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtAmount;
        TextView txtAvailable;
        TextView txtSubject;
        LinearLayout linearLayout;
        View mView;


        public MCRViewHolder(@NonNull View itemView) {
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
