package com.futureservices.muneem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Models.ModelAccountRecord;
import com.futureservices.muneem.Models.ModelCashRecord;
import com.futureservices.muneem.R;

public class custom_money_edit extends AppCompatActivity {

    //region Variables
    GlobalVariables globals;
    boolean expenseTypeSelected = false;
    int expenseType;
    //endregion

    RadioGroup rg;
    RadioButton rbCash;
    RadioButton rbAccount;
    EditText txtEditMoney;
    EditText txtReason;
    Button btnCredit;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custo_money_edit);
        globals = GlobalVariables.getGlobalInstance();

        //region Hooks
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rbCash = (RadioButton) findViewById(R.id.rbCash);
        rbAccount = (RadioButton) findViewById(R.id.rbAccount);
        txtEditMoney = (EditText) findViewById(R.id.txtEditMoney);
        txtReason = (EditText) findViewById(R.id.txtReason);
        btnCredit = (Button) findViewById(R.id.btnCredit);
        btnClose = (Button) findViewById(R.id.btnClose);
        //endregion

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                expenseTypeSelected = true;

                if (rb.getText().equals("Account")) {
                    expenseType = 2;
                } else {
                    expenseType = 1;
                }
                txtEditMoney.setEnabled(true);
                txtReason.setEnabled(true);
            }
        });
    }

    public void btnCredit(View view) {
        if (!txtEditMoney.getText().toString().isEmpty() && expenseTypeSelected) {

            Double Amount = Double.parseDouble(txtEditMoney.getText().toString());
            String Reason = String.valueOf(txtReason.getText());
            Double AvailableCash = globals.getCashAvailable() + Amount;
            Double AvailableAccount = globals.getAccountAvailable() + Amount;
            try {
                if (Amount > 0.0 && !Reason.isEmpty()) {
                    if (expenseType == 1) {
                        ModelCashRecord mcr = new ModelCashRecord();
                        mcr.setMode(1);
                        mcr.setDirection(1);
                        mcr.setAmount(Amount);
                        mcr.setAvailableCash(AvailableCash);
                        mcr.setSubject(Reason);
                        DatabaseHelper dbh = new DatabaseHelper(custom_money_edit.this);

                        try {
                            boolean Success = dbh.SaveCashTransaction(mcr);
                            if (Success) {
                                globals.setNewUser(false);
                                Toast.makeText(custom_money_edit.this, "Cash Transaction Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(custom_money_edit.this, "Error while Saving", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(custom_money_edit.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ModelAccountRecord mar = new ModelAccountRecord();
                        mar.setDate(globals.getCurrentDate());
                        mar.setMode(1);
                        mar.setDirection(1);
                        mar.setAmount(Amount);
                        mar.setAvailableAccount(AvailableAccount);
                        mar.setSubject(Reason);
                        DatabaseHelper dbh = new DatabaseHelper(custom_money_edit.this);

                        try {
                            boolean Success = dbh.SaveAccountTransaction(mar);
                            if (Success) {
                                globals.setNewUser(false);
                                Toast.makeText(custom_money_edit.this, "Account Transaction Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(custom_money_edit.this, "Error while Saving", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(custom_money_edit.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(custom_money_edit.this, "Please Enter Amount and Reason Of Change", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(custom_money_edit.this, String.valueOf(e), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(custom_money_edit.this, "Plz Choose Cash or Account", Toast.LENGTH_SHORT).show();
        }

        txtReason.setText("");

    }

    public void btnDebit(View view) {
        Double Amount = Double.parseDouble(txtEditMoney.getText().toString());
        String Reason = String.valueOf(txtReason.getText());

        if (Amount > 0.0 && !Reason.isEmpty()) {
            if (expenseType == 1) {
                if (globals.getCashAvailable() > Amount) {
                    Double AvailableCash = globals.getCashAvailable() - Amount;
                    ModelCashRecord mcr = new ModelCashRecord();
                    mcr.setMode(1);
                    mcr.setDirection(-1);
                    mcr.setAmount(Amount);
                    mcr.setAvailableCash(AvailableCash);
                    mcr.setSubject(Reason);

                    try {
                        DatabaseHelper dbh = new DatabaseHelper(custom_money_edit.this);
                        boolean Success = dbh.SaveCashTransaction(mcr);
                        if (Success) {
                            Toast.makeText(custom_money_edit.this, "Cash Transaction Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(custom_money_edit.this, "Error while Saving", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(custom_money_edit.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(custom_money_edit.this, "Insufficient Fund", Toast.LENGTH_SHORT).show();
                }
            } else if (globals.getAccountAvailable() > Amount) {
                Double AvailableAccount = globals.getAccountAvailable() - Amount;
                ModelAccountRecord mar = new ModelAccountRecord();
                mar.setMode(2);
                mar.setDirection(-1);
                mar.setAmount(Amount);
                mar.setAvailableAccount(AvailableAccount);
                mar.setSubject(Reason);

                try {
                    DatabaseHelper dbh = new DatabaseHelper(custom_money_edit.this);
                    boolean Success = dbh.SaveAccountTransaction(mar);
                    if (Success) {
                        Toast.makeText(custom_money_edit.this, "Account Transaction Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(custom_money_edit.this, "Error while Saving", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(custom_money_edit.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(custom_money_edit.this, "Insufficient Fund", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(custom_money_edit.this, "Please Enter Amount and Reason Of Change", Toast.LENGTH_SHORT).show();
        }

        txtReason.setText("");
    }

    public void btnClose(View view) {
        Intent i = new Intent(getApplicationContext(), BalanceSheet.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), BalanceSheet.class);
        startActivity(i);
    }
}