package com.futureservices.muneem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.R;

public class BalanceSheet extends AppCompatActivity {

    //region Variables
    GlobalVariables globals;
    Double          TotalMoneyAdded;
    Double          TotalExpense;
    Double          Remaining;
    Double          CashAvailable;
    Double          AccountAvailable;
    String          ReasonCash;
    String          ReasonAccount;
    //endregion

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_sheet);
        globals = GlobalVariables.getGlobalInstance();

        //region Initialising Hooks
        TextView        txtTotalStatus     = (TextView)        findViewById(R.id.txtTotalStatus);
        TextView        txtTotalExpense    = (TextView)        findViewById(R.id.txtTotalExpense);
        TextView        txtRemaining       = (TextView)        findViewById(R.id.txtRemaining);
        TextView        txtAccountStatus   = (TextView)        findViewById(R.id.txtAccountStatus);
        TextView        txtCashStatus      = (TextView)        findViewById(R.id.txtCashStatus);
        TextView        txtReasonAccount   = (TextView)        findViewById(R.id.txtReasonAccount);
        TextView        txtReasonCash      = (TextView)        findViewById(R.id.txtReasonCash);
        SwitchCompat    btnEdit            = (SwitchCompat)    findViewById(R.id.switchEdit);
        //endregion

        DatabaseHelper dbh = new DatabaseHelper(BalanceSheet.this);

        //region Initialising Variables
        TotalMoneyAdded     =   globals.getTotalMoneyAdded();
        TotalExpense        =   globals.getTotalExpense();
        Remaining           =   TotalMoneyAdded - TotalExpense;
        CashAvailable       =   globals.getCashAvailable();
        AccountAvailable    =   globals.getAccountAvailable();
        ReasonAccount       =   globals.getReasonAccount();
        ReasonCash          =   globals.getReasoncash();
        //endregion

        txtTotalExpense.setText(TotalExpense.toString());
        txtTotalStatus.setText(TotalMoneyAdded.toString());
        txtRemaining.setText(Remaining.toString());
        txtAccountStatus.setText(AccountAvailable.toString());
        txtCashStatus.setText(CashAvailable.toString());
        txtReasonAccount.setText(ReasonAccount);
        txtReasonCash.setText(ReasonCash);

        btnEdit.setOnCheckedChangeListener(new SwitchCompat.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ShowMoneyEditLayout();
            }
        });
    }

    private void ShowMoneyEditLayout() {
        Intent i = new Intent(getApplicationContext(), custom_money_edit.class);
        startActivity(i);
    }

    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }
}