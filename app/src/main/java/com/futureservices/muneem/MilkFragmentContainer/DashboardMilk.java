package com.futureservices.muneem.MilkFragmentContainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelMilkRecord;
import com.futureservices.muneem.R;

public class DashboardMilk extends Fragment {
    //region Variables
    GlobalVariables globals;
    boolean MilkModeSelected = false;
    int MilkMode;
    android.widget.DatePicker datePicker;
    //endregion

    RadioGroup rg;
    RadioButton rbMorning;
    RadioButton rbEvening;
    EditText txtMilkQuantity;
    EditText txtMilkRate;
    Button btnAddMilkRecord;

    String Date;
    String day;
    String month;
    String year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dashboard_milk, container, false);
        globals = GlobalVariables.getGlobalInstance();

        //region Hooks
        rg = (RadioGroup) view.findViewById(R.id.radioGroup);
        rbMorning = (RadioButton) view.findViewById(R.id.rbMorning);
        rbEvening = (RadioButton) view.findViewById(R.id.rbEvening);
        txtMilkQuantity = (EditText) view.findViewById(R.id.txtMilkQuantity);
        txtMilkRate = (EditText) view.findViewById(R.id.txtMilkRate);
        btnAddMilkRecord = (Button) view.findViewById(R.id.btnAddMilkRecord);
        //endregion


        Date = globals.getCurrentDate();
        String[] dateParts = Date.split("-");
        day = dateParts[2];
        month = dateParts[1];
        year = dateParts[0];
        datePicker = view.findViewById(R.id.dateSelector);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) view.findViewById(checkedId);
                MilkModeSelected = true;

                if (rb.getText().equals("Morning")) {
                    MilkMode = 1;
                } else {
                    MilkMode = 2;
                }
                txtMilkQuantity.setEnabled(true);
                txtMilkQuantity.setHint("");
                txtMilkRate.setHint("");
                txtMilkRate.setEnabled(true);
            }
        });

        btnAddMilkRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = String.valueOf(datePicker.getMonth() + 1);
                year = String.valueOf(datePicker.getYear());
                day = String.valueOf(datePicker.getDayOfMonth());
                Date = year + "-" + month + "-" + day;

                if (!txtMilkQuantity.getText().toString().isEmpty() && MilkModeSelected && !txtMilkRate.getText().toString().isEmpty()) {
                    DatabaseHelper dbh = new DatabaseHelper(getContext());
                    Double MilkQuantity = Double.parseDouble(txtMilkQuantity.getText().toString());
                    Double MilkRate = Double.parseDouble(txtMilkRate.getText().toString());

                    ModelMilkRecord mmr = new ModelMilkRecord();
                    mmr.setDate(Date);
                    mmr.setMilkQuantity(MilkQuantity);
                    mmr.setMilkRate(MilkRate);

                    if (MilkMode == 1) {
                        mmr.setMilkMode(1);
                        try {
                            boolean Success = dbh.SaveMilkRecords(mmr);
                            if (Success) {
                                globals.setNewUser(false);
                                Toast.makeText(getContext(), "Morning Milk Record Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error while Saving", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mmr.setMilkMode(2);
                        try {
                            boolean Success = dbh.SaveMilkRecords(mmr);
                            if (Success) {
                                globals.setNewUser(false);
                                Toast.makeText(getContext(), "Evening Milk Record Saved Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error while Saving", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Plz select Quantity,Price and Mode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}