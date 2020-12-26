package com.futureservices.muneem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futureservices.muneem.Adapters.AdapterMIR;
import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelAccountRecord;
import com.futureservices.muneem.Models.ModelCashRecord;
import com.futureservices.muneem.Models.ModelItemsRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;

public class AddItems extends AppCompatActivity {

    //region Variables
    GlobalVariables globals;
    private     RecyclerView recyclerView;
    private AdapterMIR adapterMIR;
    String      ItemName;
    Double      ItemPrice;
    Integer     expenseType;
    Double      TotalExpenseCurrent = 0.0;
    boolean     isProceedAllowed;
    Double      expenseCapacity;
    private ArrayList<ModelItemsRecord> listMIR;
    //endregion

    //region Hooks
    Spinner monthlySelector;
    TextView        txtTotalExpense;
    EditText        txtItemName;
    EditText        txtItemPrice;
    TextView        txtBalanceLeft;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        globals = GlobalVariables.getGlobalInstance();

        //region Initialising Hooks
        txtTotalExpense =   (TextView) findViewById(R.id.txtTotalExpense);
        recyclerView    =   (RecyclerView) findViewById(R.id.listSavedItem);
        txtItemName     =   findViewById(R.id.txtItemName);
        txtItemPrice    =   findViewById(R.id.txtItemPrice);
        txtBalanceLeft  =   findViewById(R.id.txtBalanceLeft);
        listMIR         =   new ArrayList<ModelItemsRecord>();
        //endregion

        //region globals
        expenseType = globals.getExpenseType();
        expenseCapacity = globals.getExpenseCapacity();
        //endregion

        txtBalanceLeft.setText(String.valueOf(expenseCapacity.toString()));
        txtTotalExpense.setText(String.valueOf(TotalExpenseCurrent.toString()));

        Toast.makeText(AddItems.this, String.valueOf(expenseCapacity.toString()) + String.valueOf(TotalExpenseCurrent.toString()), Toast.LENGTH_SHORT).show();
    }

    private void BuildList() {
        try {
            if (!listMIR.isEmpty()) {
                adapterMIR = new AdapterMIR(listMIR);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapterMIR);
            } else {
                Toast.makeText(AddItems.this, "Add Items and Price", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(AddItems.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnAddItem(View view) {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
            ItemName = txtItemName.getText().toString();
            ItemPrice = Double.parseDouble(txtItemPrice.getText().toString());
            if (!ItemName.isEmpty() && (ItemPrice > 0.0)) {
                ItemName = txtItemName.getText().toString();
                ItemPrice = Double.parseDouble(txtItemPrice.getText().toString());

                if (setTotalExpenseCurrent(Double.parseDouble(String.valueOf(ItemPrice)), 1)) {
                    listMIR.add(new ModelItemsRecord(1, globals.getDateOfExpense(), expenseType, ItemName, ItemPrice));
                    BuildList();
                    clearEditTexts();
                } else {
                    Toast.makeText(AddItems.this, "Please Add Name & Price of Item", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddItems.this, "Please fill Name & Price of Item", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(AddItems.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

    }

    private void clearEditTexts() {
        txtItemName.setText("");
        txtItemPrice.setText("");
    }

    private boolean setTotalExpenseCurrent(double value, Integer operator) {
        if (operator == 1 && IsProceedAllowed(value)) {
            TotalExpenseCurrent += value;
            isProceedAllowed = true;
            expenseCapacity -= value;
            txtBalanceLeft.setText(String.valueOf(expenseCapacity));
            txtTotalExpense.setText(String.valueOf(TotalExpenseCurrent));
            return true;
        } else if (operator == 2) {
            TotalExpenseCurrent -= value;
            isProceedAllowed = true;
            expenseCapacity += value;
            txtBalanceLeft.setText(String.valueOf(expenseCapacity));
            txtTotalExpense.setText(String.valueOf(TotalExpenseCurrent));
            return true;
        } else {
            Toast.makeText(AddItems.this, "You have don't have enough money in your", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean IsProceedAllowed(double value) {
        if (expenseCapacity == 0.0 || expenseCapacity < value) {
            return false;
        }
        return true;
    }

    public void btnSave(View view) {
        try {
            if(listMIR != null) {
                DatabaseHelper dbh = new DatabaseHelper(AddItems.this);
                long Success = dbh.SaveItems(listMIR);
                if (Success>0) {
                    setGlobalVariables();
                    if (expenseType == 1) {
                        ModelCashRecord mcr = new ModelCashRecord(globals.getDateOfExpense(), 2, -1, TotalExpenseCurrent, globals.getCashAvailable(), "Items Added On " + globals.getDateOfExpense());
                        dbh.SaveCashTransaction(mcr);
                    }
                    else{
                        ModelAccountRecord mar = new ModelAccountRecord(globals.getDateOfExpense(), 2, -1, TotalExpenseCurrent, globals.getAccountAvailable(), "Items Added On " + globals.getDateOfExpense());
                        dbh.SaveAccountTransaction(mar);
                    }
                    CleanUp();
                }
                Toast.makeText(getApplicationContext(), "Records Added" + Success, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Add Items", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Database disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    private void CleanUp() {
        listMIR.clear();
        adapterMIR = new AdapterMIR(listMIR);
        recyclerView.setAdapter(adapterMIR);
    }

    private void setGlobalVariables() {
        if (expenseType == 1) {
            globals.setCashAvailable(expenseCapacity);
        } else {
            globals.setAccountAvailable(expenseCapacity);
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}