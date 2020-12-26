package com.futureservices.muneem.MilkFragmentContainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futureservices.muneem.Adapters.AdapterMMR;
import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelMilkRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;

public class EveningMilk extends Fragment {
    android.widget.DatePicker datePicker;
    Button btnShowList;
    private RecyclerView EveningMilkRecyclerView;
    GlobalVariables globals;
    ArrayList<ModelMilkRecord> listMMR;
    private AdapterMMR adapterMMR;

    String Date;
    String day;
    String month;
    String year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evening_milk, container, false);

        globals = GlobalVariables.getGlobalInstance();
        EveningMilkRecyclerView = view.findViewById(R.id.EveningMilkList);
        btnShowList = view.findViewById(R.id.btnShowList);

        Date = globals.getCurrentDate();
        String[] dateParts = Date.split("-");
        day = dateParts[2];
        month = dateParts[1];
        year = dateParts[0];
        datePicker = view.findViewById(R.id.dateSelector);
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = String.valueOf(datePicker.getMonth()+1);
                year = String.valueOf(datePicker.getYear());
                Date = year + "-" + month + "-01";
                BuildMMRList(Date);
            }
        });
        try {
            BuildMMRList(Date);
        } catch (Exception e) {
            Toast.makeText(getContext(), String.valueOf("No Record Found"), Toast.LENGTH_SHORT).show();
        }

        return view;
    }
    private void BuildMMRList(String date) {
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        listMMR = dbh.GetSavedMorningMilkRecord(date,2);

        if (listMMR != null) {
            adapterMMR = new AdapterMMR(listMMR);
            EveningMilkRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            EveningMilkRecyclerView.setAdapter(adapterMMR);
        } else {
            Toast.makeText(getContext(), "No Evening-Milk-Record Found", Toast.LENGTH_SHORT).show();
        }

    }
}