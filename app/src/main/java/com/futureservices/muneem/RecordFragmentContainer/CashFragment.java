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

import com.futureservices.muneem.Adapters.AdapterMCR;
import com.futureservices.muneem.HelperClasses.DatabaseHelper;
import com.futureservices.muneem.Globals.GlobalVariables;
import com.futureservices.muneem.Models.ModelCashRecord;
import com.futureservices.muneem.R;

import java.util.ArrayList;


public class CashFragment extends Fragment {
    android.widget.DatePicker datePicker;
    Button btnShowList;
    private RecyclerView CashRecyclerView;
    GlobalVariables globals;
    ArrayList<ModelCashRecord> listMCR;
    private AdapterMCR adapterMCR;

    String Date;
    String day;
    String month;
    String year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash, container, false);
        globals = GlobalVariables.getGlobalInstance();
        CashRecyclerView = view.findViewById(R.id.CashRecordList);
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
                BuildMCRList(Date);
            }
        });
        try {
            BuildMCRList(Date);
        } catch (Exception e) {
            Toast.makeText(getContext(), String.valueOf("No Record Found"), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void BuildMCRList(String date) {
        DatabaseHelper dbh = new DatabaseHelper(getContext());
        listMCR = dbh.GetCashTransactions(date);

        if (listMCR != null) {
            adapterMCR = new AdapterMCR(listMCR);
            CashRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            CashRecyclerView.setAdapter(adapterMCR);
        } else {
            Toast.makeText(getContext(), "No Cash-Transactions Found", Toast.LENGTH_SHORT).show();
        }

    }
}