package com.futureservices.muneem;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Records extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //region Variables
    GlobalVariables globals;
    private RecyclerView CashrecyclerView;
    private RecyclerView AccountrecyclerView;
    private AdapterMCR adapterMCR;
    private AdapterMAR adapterMAR;
    ArrayList<ModelCashRecord> listMCR;
    ArrayList<ModelAccountRecord> listMAR;
    String Date;
    Spinner spinner;
    String day;
    String month;
    String year;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        globals = com.futureservices.muneem.GlobalVariables.getGlobalInstance();

        spinner = (Spinner) findViewById(R.id.spinnerMonth);
        CashrecyclerView = (RecyclerView) findViewById(R.id.CashRecordList) ;
        AccountrecyclerView = (RecyclerView) findViewById(R.id.AccountRecordList) ;


        Date = globals.getCurrentDate();
        String[] dateParts = Date.split("-");
        day = dateParts[2];
        month = dateParts[1];
        year = dateParts[0];

        try {
            BuildMARList(Date);
            BuildMCRList(Date);
        } catch (Exception e) {
            Toast.makeText(Records.this, String.valueOf("No Record Found"), Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(this, R.array.Months, R.layout.support_simple_spinner_dropdown_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(monthsAdapter);
        spinner.setSelection(Integer.parseInt(month) - 1);    //setting current month
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        String m = String.valueOf(position + 1);
        CleanUp();
        String SearchDate = year + "-" + m + "-01";
        try {
            BuildMARList(Date);
            BuildMCRList(Date);
        } catch (Exception e) {
            Toast.makeText(Records.this, String.valueOf("No Record Found"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void CleanUp() {
    }

    private void BuildMARList(String date) {
        DatabaseHelper dbh = new DatabaseHelper(Records.this);
        listMAR = dbh.GetAccountTransaction(date);

        if (listMAR != null) {
            adapterMAR = new AdapterMAR(listMAR);
            AccountrecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            AccountrecyclerView.setAdapter(adapterMAR);
        } else {
            Toast.makeText(Records.this, "No Account-Transactions Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void BuildMCRList(String date) {
        DatabaseHelper dbh = new DatabaseHelper(Records.this);
        listMCR = dbh.GetCashTransactions(date);

        if (listMCR != null) {
            adapterMCR = new AdapterMCR(listMCR);
            CashrecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            CashrecyclerView.setAdapter(adapterMCR);
        } else {
            Toast.makeText(Records.this, "No Cash-Transactions Found", Toast.LENGTH_SHORT).show();
        }

    }
}