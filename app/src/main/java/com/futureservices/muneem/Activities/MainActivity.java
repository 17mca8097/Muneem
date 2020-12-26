package com.futureservices.muneem.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.futureservices.muneem.HelperClasses.Date_ExpenseType_Helper;
import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.R;
import com.futureservices.muneem.RecordFragmentContainer.RecordFragmented;
import com.futureservices.muneem.MilkFragmentContainer.MilkFragment;

public class MainActivity extends AppCompatActivity {

    //region Variables
    GlobalVariables globals;
    Dialog newDialog;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globals = GlobalVariables.getGlobalInstance();

        if (globals.isFirstTime()) {
            CheckIfNewUser();
            globals.setFirstTime(false);
        }
    }

    //region Checking New user
    private void CheckIfNewUser() {
        DatabaseHelper dbh = new DatabaseHelper(MainActivity.this);
        try {
            globals.setNewUser(dbh.CheckIfNewUser());

            dbh.GetTotalExpense();          //setting TotalExpense Value in Global variables
            dbh.GetCashAvailable();         //setting CashAvailable Value in Global variables
            dbh.GetAccountAvailable();      //setting AccountAvailable Value in Global variables

            if (globals.isNewUser()) {
                ActivityPopUp("Welcome", "Start Managing Money, Add Money Record??", "com.futureservices.muneem.Activities.BalanceSheet");
            } else {
                ActivityPopUp("Welcome Back", "Continue Adding Items", "com.futureservices.muneem.HelperClasses.Date_ExpenseType_Helper");
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error In Running Application, Plz Contact Developer", Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
            System.exit(0);
        }
    }
    //endregion

    //region open Activity base on IfNewUser
    private void ActivityPopUp(String Title, String Message, final String Activity) {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setTitle(Title);
        ad.setMessage(Message);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent i = null;
                try {
                    i = new Intent(getBaseContext(), Class.forName(Activity));
                    startActivity(i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    dialog.cancel();
                }
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();
            }
        });
        AlertDialog alert = ad.create();
        alert.show();
    }
    //endregion

    //region Start Different Activities

    public void BalanceSheet(View view) {                               //Open BalanceSheet Class
        Intent i = new Intent(getBaseContext(), BalanceSheet.class);
        startActivity(i);
    }

    public void ShowMonthlyRecord(View view) {                          //Open Monthly Record Screen
        if (globals.isNewUser()) {
            ActivityPopUp("No Record", "Please Add Money Record & then Items", "com.futureservices.muneem.Activities.BalanceSheet");
        } else {
            Intent i = new Intent(getBaseContext(), MonthlyList.class);
            //i.putExtra("DateExpense", DateExpense);
            startActivity(i);
        }
    }

    public void AddItems(View view) {                                   //Open Screen for Adding Items
        if (globals.isNewUser()) {
            ActivityPopUp("Zero Money", "Please Add Money Record & then add Items", "com.futureservices.muneem.Activities.BalanceSheet");
        } else {
            Intent i = new Intent(getBaseContext(), Date_ExpenseType_Helper.class);
            startActivity(i);
        }
    }

    public void Reset(View view) {                                      //open Reset Screen
        if (globals.isNewUser()) {
            ActivityPopUp("New User", "Nothing to Reset", "com.futureservices.muneem.Activities.MainActivity");
        } else {
            Intent i = new Intent(getBaseContext(), ResetApplication.class);
            startActivity(i);
        }
    }

    public void ManageMilkRecord(View view) {                           //Opem Milk Management Screen
        Intent i = new Intent(getBaseContext(), MilkFragment.class);
        startActivity(i);
    }

    public void AboutUs(View view) {                                    //Open About Us Screen
        Intent i = new Intent(getBaseContext(), AboutUs.class);
        startActivity(i);
    }

    public void ShowMoneyRecords(View view) {                           //Open Transaction Record Screen
        if (globals.isNewUser()) {
            ActivityPopUp("New User", "Nothing to Show", "com.futureservices.muneem.Activities.MainActivity");
        } else {
            Intent i = new Intent(getBaseContext(), RecordFragmented.class);
            startActivity(i);
        }
    }

    public void onBackPressed() {
        finishAffinity();
        this.finish();
    }
    //endregion
}