package com.futureservices.muneem;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMIR extends RecyclerView.Adapter<AdapterMIR.MIRViewHolder>{
    Dialog newDialog;
    public Context context;
    public AdapterMIR(Context context) {
        this.context = context;
    }
    private ArrayList<ModelItemsRecord> items;
    int rowIndex = -1;
    GlobalVariables globals = com.futureservices.muneem.GlobalVariables.getGlobalInstance();

    public AdapterMIR(ArrayList<ModelItemsRecord> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MIRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_view_item_list, parent, false);
        MIRViewHolder mir_VH = new MIRViewHolder(view);
        return mir_VH;
    }

    @Override
    public void onBindViewHolder(@NonNull final MIRViewHolder holder, final int position) {
        final ModelItemsRecord currentItem = items.get(position);
        String Date = globals.GetUIDate(currentItem.getDate());
        holder.txtDate.setText(Date);
        if(currentItem.getExpenseType() == 1)
        {
            holder.txtExpenseType.setText("Cash");
        }
        else{
            holder.txtExpenseType.setText("Account");
        }
        holder.txtItemName.setText(currentItem.getItemName());
        holder.txtItemPrice.setText(String.valueOf(currentItem.getItemPrice()));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowIndex = position;
                notifyDataSetChanged();
            }
        });
        if(rowIndex == position)
        {
            holder.linearLayout.setBackgroundResource(R.drawable.rv_background);
        }
        else
        {
            holder.linearLayout.setBackgroundResource(R.drawable.rv_selected);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtDate;
                final EditText txtItemName;
                final EditText txtItemPrice;
                final EditText txtExpenseType;
                Button btnSaveChanges;



                newDialog = new Dialog(v.getRootView().getContext());
                newDialog.setTitle("Select Quantity");
                newDialog.setContentView(R.layout.edit_item_record);

                txtDate = (EditText) newDialog.findViewById(R.id.txtDateOfExpense);
                txtItemName = (EditText) newDialog.findViewById(R.id.txtItemName);
                txtItemPrice = (EditText) newDialog.findViewById(R.id.txtItemPrice);
                txtExpenseType = (EditText) newDialog.findViewById(R.id.txtExpenseType);
                btnSaveChanges = (Button) newDialog.findViewById(R.id.btnSaveChanges);

                txtDate.setText(String.format(String.valueOf(currentItem.getDate())));
                txtItemName.setText(String.format(String.valueOf(currentItem.getItemName())));
                txtItemPrice.setText(String.format(String.valueOf(currentItem.getItemPrice())));
                txtExpenseType.setText(String.format(String.valueOf(currentItem.getExpenseType())));

                btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double oldPrice = currentItem.getItemPrice();
                        Double newPrice = 0.0;
                        int Direction;
                        ModelItemsRecord mir = new ModelItemsRecord();
                        mir.setTransactionID(currentItem.getTransactionID());
                        mir.setDate(txtDate.getText().toString());
                        mir.setItemName(txtItemName.getText().toString());
                        mir.setItemPrice(Double.parseDouble(txtItemPrice.getText().toString()));
                        mir.setExpenseType(Integer.parseInt(txtExpenseType.getText().toString()));
                        newPrice = mir.getItemPrice();
                        newPrice = newPrice - oldPrice;
                        if(newPrice < 0){Direction = 1;} else{Direction = -1;}

                        DatabaseHelper dbh = new DatabaseHelper(v.getRootView().getContext());
                        boolean success = dbh.UpdateSavedList(mir,Direction,Math.abs(newPrice));
                        if(success)
                            Toast.makeText(v.getRootView().getContext(), "Record Updated", Toast.LENGTH_SHORT).show();
                        items.set(position,mir);
                        notifyDataSetChanged();
                        newDialog.dismiss();
                    }
                });

                newDialog.show();
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Button btnDelete;

                newDialog = new Dialog(v.getRootView().getContext());
                newDialog.setTitle("Select Quantity");
                newDialog.setContentView(R.layout.delete_record);

                btnDelete = (Button) newDialog.findViewById(R.id.btnDelete);


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ModelItemsRecord mir = new ModelItemsRecord();
                        mir.setTransactionID(currentItem.getTransactionID());
                        mir.setExpenseType(currentItem.getExpenseType());
                        mir.setItemPrice(currentItem.getItemPrice());
                        DatabaseHelper dbh = new DatabaseHelper(v.getRootView().getContext());
                        boolean success = dbh.DeleteItem(mir);
                        if(success)
                            Toast.makeText(v.getRootView().getContext(), "Record Deleted", Toast.LENGTH_SHORT).show();
                        items.remove(position);
                        notifyItemRemoved(position);
                        newDialog.dismiss();
                    }
                });
                newDialog.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MIRViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate;
        TextView txtItemName;
        TextView txtItemPrice;
        TextView txtExpenseType;
        LinearLayout linearLayout;
        View mView;

        public MIRViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDateOfExpense);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            txtExpenseType = itemView.findViewById(R.id.txtExpenseType);
            linearLayout = itemView.findViewById(R.id.linearLayoutHead);
            mView = itemView;
        }
    }

}
