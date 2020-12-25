package com.futureservices.muneem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class ManageMilk extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    GlobalVariables globals;
    Dialog newDialog;
    TextView txtMilkQuantity;
    EditText txtMilkRate;
    Button btnSaveMilkRecord;
    Button btnCurrentDate;
    CalendarView calender;
    String Date;
    AdapterMMR adapterMMR;
    RecyclerView recyclerView;
    Spinner spinner;
    String day;
    String month;
    String year;
    ArrayList<ModelMilkRecord> ListMMR;
    Double MilkQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_milk);
        globals = com.futureservices.muneem.GlobalVariables.getGlobalInstance();

        txtMilkQuantity  = findViewById(R.id.txtMilkQuantity);
        txtMilkRate = findViewById(R.id.txtMilkRate);
        btnSaveMilkRecord = findViewById(R.id.btnSaveMilkRecord);
        btnCurrentDate = findViewById(R.id.btnCurrentDate);
        recyclerView = findViewById(R.id.listMilk);
        spinner = (Spinner) findViewById(R.id.spinnerMonth);

        txtMilkRate.setText(String.valueOf(globals.getMilkRate()));

        btnCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.futureservices.muneem.DatePicker();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        Date = globals.getCurrentDate();
        String[] dateParts = Date.split("-");
        day = dateParts[2];
        month = dateParts[1];
        year = dateParts[0];

        btnCurrentDate.setText(day + "/" + month);

        ArrayAdapter<CharSequence> monthsAdapter = ArrayAdapter.createFromResource(this, R.array.Months, R.layout.support_simple_spinner_dropdown_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(monthsAdapter);
        spinner.setSelection(Integer.parseInt(month) - 1);    //setting current month
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            month = month + 1;
            Date = year + "-" + (month) + "-" + dayOfMonth;
            btnCurrentDate.setText(dayOfMonth + "/" + month);
            spinner.setSelection(month - 1);
        } catch (Exception e) {
            Toast.makeText(ManageMilk.this, "Error In Running Application, Plz Contact Developer", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            String text = parent.getItemAtPosition(position).toString();
            String m = String.valueOf(position + 1);
            CleanUp();
            Date = year + "-" + m + "-01";
            RefreshList(Date);
        } catch (Exception e) {
            Toast.makeText(ManageMilk.this, "Error In Running Application, Plz Contact Developer", Toast.LENGTH_SHORT).show();
        }
    }

    public void addQuantity(View view) {
        Integer Quantity ;
        switch(view.getId())
        {
            case R.id.txtOneLitre:
                MilkQuantity = 1.0;
                break;

            case R.id.txtOneAndHalfLitre:
                MilkQuantity = 1.5;
                break;

            case R.id.txtTwoLitre:
                MilkQuantity = 2.0;
                break;

            case R.id.txtTwoAndHalfLitre:
                MilkQuantity = 2.5;
                break;

            case R.id.txtThreeLitre:
                MilkQuantity = 3.0;
                break;

            case R.id.txtThreeAndHalfLitre:
                MilkQuantity = 3.5;
                break;

            case R.id.txtFourLitre:
                MilkQuantity = 4.0;
                break;

            case R.id.txtFourAndHalfLitre:
                MilkQuantity = 4.5;
                break;
            case R.id.txtFiveLitre:
                MilkQuantity = 5.0;
                break;

            case R.id.txtFiveAndHalfLitre:
                MilkQuantity = 5.5;
                break;

            case R.id.txtSixLitre:
                MilkQuantity = 6.0;
                break;

            case R.id.txtSixAndHalfLitre:
                MilkQuantity = 6.5;
                break;
            case R.id.txtSevenLitre:
                MilkQuantity = 7.0;
                break;

            case R.id.txtSevenAndHalfLitre:
                MilkQuantity = 7.5;
                break;

            case R.id.txtEightLitre:
                MilkQuantity = 8.0;
                break;

            case R.id.txtEightAndHalfLitre:
                MilkQuantity = 8.5;
                break;
        }
        disposeDialog();
    }

    public void btnAddMilkQuantity(View view) {

        newDialog = new Dialog(this);
        newDialog.setTitle("Select Quantity");
        newDialog.setContentView(R.layout.input_milk_quntity);
        disposeDialog();
        newDialog.show();
    }

    private void disposeDialog() {
        newDialog.dismiss();
        txtMilkQuantity.setText(String.valueOf(MilkQuantity));
        btnSaveMilkRecord.setEnabled(true);
    }

    public void saveMilkRecord(View view) {
        DatabaseHelper dbh = new DatabaseHelper(ManageMilk.this);
        ModelMilkRecord mmr = new ModelMilkRecord();
        mmr.setDate(Date);
        mmr.setMilkQuantity(Double.parseDouble(txtMilkQuantity.getText().toString()));
        mmr.setMilkRate(Double.parseDouble(txtMilkRate.getText().toString()));

        boolean Success = dbh.SaveMilkRecords(mmr);
        if(Success) {
            RefreshList(Date);
            globals.setMilkRate(mmr.getMilkRate());
        }
    }

    private void RefreshList(String searchDate) {
        DatabaseHelper dbh = new DatabaseHelper(ManageMilk.this);
        ListMMR = dbh.GetSavedMilkRecord(searchDate);
        try {
            if (!ListMMR.isEmpty()) {
                adapterMMR = new AdapterMMR(ListMMR);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapterMMR);
            } else {
                Toast.makeText(ManageMilk.this, "No Records Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(ManageMilk.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void CleanUp() {
        if(ListMMR!=  null) {
            ListMMR.clear();
            adapterMMR = new AdapterMMR(ListMMR);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapterMMR);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void CalculateTotalCost(View view) {
        TextView txtTotalCost = findViewById(R.id.txtTotalCost);

        Double TotalQuantity = 0.0;
        Double MilkRate = 0.0;
        Double TotalCost = 0.0;
        int count  =0;

        if(ListMMR.size() > 0) {
            for (ModelMilkRecord item : ListMMR) {
                count += 1;
                TotalQuantity = item.getMilkQuantity();
                MilkRate = item.getMilkRate();
                TotalCost += MilkRate * TotalQuantity;
            }
            TotalCost = TotalQuantity * MilkRate;
            txtTotalCost.setText(TotalQuantity + " Liter Milk for Total cost :" + TotalCost + " in " + count + "Days");
        }
        else{
            Toast.makeText(ManageMilk.this, "Can't Calculate ,No Records Found", Toast.LENGTH_SHORT).show();
        }
    }
}