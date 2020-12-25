package com.futureservices.muneem.FragContainer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.futureservices.muneem.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class RecordFragmented extends AppCompatActivity {

    ChipNavigationBar cnb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_records_fragmented);

        cnb = findViewById(R.id.bottom_nav_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new CashFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        cnb.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch(i){
                    case R.id.nav_CashRecord:
                        fragment = new CashFragment();
                        break;
                    case R.id.nav_AccountRecord:
                        fragment = new AccountFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragment).commit();
            }
        });
    }
}