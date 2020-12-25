package com.futureservices.muneem;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthlyList extends AppCompatActivity {

    //region Variables
    GlobalVariables globals;
    private RecyclerView recyclerView;
    private AdapterMIR adapterMIR;
    ArrayList<ModelItemsRecord> listMIR;
    String Date;
    String day;
    String month;
    String year;
    //endregion

    //region Hooks
    Spinner monthlySelector;
    RecyclerView listSavedItem;
    TextView txtTotalCash;
    TextView txtTotalAccount;
    TextView txtTotalExpense;
    android.widget.DatePicker datePicker;
    Button btnShowList;


    //endregion
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_list);

        globals = com.futureservices.muneem.GlobalVariables.getGlobalInstance();

        //region Initialising Hooks
        listSavedItem =  (RecyclerView) findViewById(R.id.listSavedItem);
        txtTotalCash = (TextView) findViewById(R.id.txtTotalCash);
        txtTotalAccount = (TextView) findViewById(R.id.txtTotalAccount);
        txtTotalExpense = (TextView) findViewById(R.id.txtTotalExpense);
        recyclerView = findViewById(R.id.listSavedItem);
        btnShowList = findViewById(R.id.btnShowList);

  //      spinner = (Spinner) findViewById(R.id.spinnerMonth);
//        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        //endregion
        //region BuildList

        //endregion
        Date = globals.getCurrentDate();
        String[] dateParts = Date.split("-");
        day = dateParts[2];
        month = dateParts[1];
        year = dateParts[0];
        Date = year + "-" + month + "-01";


        datePicker = findViewById(R.id.dateSelector);
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = String.valueOf(datePicker.getMonth()+1);
                year = String.valueOf(datePicker.getYear());
                Date = year + "-" + month + "-01";
                BuildList(Date);
            }
        });


        BuildList(Date);
        AdapterMIR context = new AdapterMIR(MonthlyList.this);

    }
    private void CleanUp() {
        listMIR.clear();
        adapterMIR = new AdapterMIR(listMIR);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapterMIR);
    }

    private void BuildList(String Date) {
        DatabaseHelper dbh = new DatabaseHelper(MonthlyList.this);
        try {
            listMIR = dbh.GetSavedItemList(Date);
            if (listMIR != null) {
                adapterMIR = new AdapterMIR(listMIR);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(adapterMIR);
                SetMoneyFields(listMIR);
            }
            else
            {
                Toast.makeText(MonthlyList.this, "No Records Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MonthlyList.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void SetMoneyFields(ArrayList<ModelItemsRecord> MIR) {
        Double totalCash = 0.0;
        Double totalAccount = 0.0;
        Double totalExpense = 0.0;
        for (ModelItemsRecord item : MIR) {
            if (item.getExpenseType() == 1) {
                totalCash += item.getItemPrice();
            } else {
                totalAccount += item.getItemPrice();
            }
        }
        totalExpense = totalAccount + totalCash;
        txtTotalCash.setText(String.valueOf(totalCash));
        txtTotalAccount.setText(String.valueOf(totalAccount));
        txtTotalExpense.setText(String.valueOf(totalExpense));
    }

}