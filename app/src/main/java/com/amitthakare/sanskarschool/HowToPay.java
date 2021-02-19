package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class HowToPay extends AppCompatActivity {

    DrawerLayout drawerLayoutHowToPay;
    Toolbar toolbarHowToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_pay);
        ini();
    }

    private void ini() {
        drawerLayoutHowToPay = findViewById(R.id.drawerLayoutHowToPay);
        toolbarHowToPay = findViewById(R.id.navigationToolbarHowToPay);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarHowToPay);
        Objects.requireNonNull(getSupportActionBar()).setTitle("How To Pay");
        toolbarHowToPay.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarHowToPay.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HowToPay.this,drawerLayoutHowToPay,toolbarHowToPay,R.string.open,R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //drawerLayoutStudent.addDrawerListener(toggle);
        toggle.syncState();
    }
}