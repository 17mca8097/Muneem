package com.futureservices.muneem;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResetApplication extends AppCompatActivity {
    GlobalVariables globals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_application);
        globals = com.futureservices.muneem.GlobalVariables.getGlobalInstance();
    }

    public void btnReset(View view) {

        new AlertDialog.Builder(ResetApplication.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure ?")
                .setMessage("This will delete All records you saved ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        context.deleteDatabase("MuneemDB.db");
                        DatabaseHelper dbh = new DatabaseHelper(ResetApplication.this);
                        GlobalVariables gv = new GlobalVariables();
                        globals.setGlobalInstance(gv);
                        //dbh.Reset();
                        Toast.makeText(ResetApplication.this, "All Files deleted, use app as New", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}