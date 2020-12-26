package com.futureservices.muneem.HelperClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.futureservices.muneem.Activities.AddItems;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.R;

public class Date_ExpenseType_Helper extends AppCompatActivity {
    GlobalVariables globals;
    String DateExpense;
    boolean expenseTypeSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        globals = GlobalVariables.getGlobalInstance();
        CalendarView calender = (CalendarView) findViewById(R.id.calendarView);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        ImageView btnNext = (ImageView) findViewById(R.id.btnNext);

        DateExpense = globals.getCurrentDate();
        //region setting ExpenseDate and ExpenseType
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                DateExpense = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                expenseTypeSelected = true;
                if (rb.getText().equals("Account")) {
                    globals.setExpenseType(2);
                    globals.setExpenseCapacity(globals.getAccountAvailable());
                } else {
                    globals.setExpenseType(1);
                    globals.setExpenseCapacity(globals.getCashAvailable());
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globals.setDateOfExpense(DateExpense);
                if (expenseTypeSelected && globals.getExpenseCapacity() > 0) {
                    Intent i = new Intent(getBaseContext(), AddItems.class);
                    startActivity(i);
                } else {
                    if(!expenseTypeSelected)
                    Toast.makeText(Date_ExpenseType_Helper.this, "please select Expense type", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Date_ExpenseType_Helper.this, "please add Money Status", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}