package com.futureservices.muneem.MilkFragmentContainer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.futureservices.muneem.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MilkFragment extends AppCompatActivity {
    ChipNavigationBar cnb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_fragment);

        cnb = findViewById(R.id.bottom_nav_milk_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.Milkfrag_container,new DashboardMilk()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        cnb.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch(i){
                    case R.id.nav_Morning:
                        fragment = new MorningMilk();
                        break;
                    case R.id.nav_Evening:
                        fragment = new EveningMilk();
                        break;
                    case R.id.nav_DashBoard:
                        fragment = new DashboardMilk();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.Milkfrag_container,fragment).commit();
            }
        });
    }
}