package com.futureservices.muneem.RecordFragmentContainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futureservices.muneem.Adapters.AdapterMAR;
import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelAccountRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    android.widget.DatePicker datePicker;
    Button btnShowList;
    private RecyclerView AccountRecyclerView;
    GlobalVariables globals;
    ArrayList<ModelAccountRecord> listMAR;
    private AdapterMAR adapterMAR;

    String Date;
    String day;
    String month;
    String year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        globals = GlobalVariables.getGlobalInstance();
        AccountRecyclerView = view.findViewById(R.id.AccountRecordList);
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
                BuildMARList(Date);
            }
        });
        try {
            BuildMARList(Date);
        } catch (Exception e) {
            Toast.makeText(getContext(), String.valueOf("No Record Found"), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void BuildMARList(String date) {
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        listMAR = dbh.GetAccountTransaction(date);

        if (listMAR != null) {
            adapterMAR = new AdapterMAR(listMAR);
            AccountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            AccountRecyclerView.setAdapter(adapterMAR);
        } else {
            Toast.makeText(getContext(), "No Account-Transactions Found", Toast.LENGTH_SHORT).show();
        }

    }
}