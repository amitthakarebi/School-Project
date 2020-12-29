package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PayFees extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayoutPayFees;

    CardView googlepay, phonepe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);
        ini();
    }

    private void ini() {

        toolbar = findViewById(R.id.navigationToolbarPayFees);
        drawerLayoutPayFees = findViewById(R.id.drawerLayoutPayFees);

        googlepay = findViewById(R.id.googleplay);
        phonepe = findViewById(R.id.phonepe);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pay Fees");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PayFees.this,drawerLayoutPayFees,toolbar,R.string.open,R.string.close);
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

        googlepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayFees.this,UpiPayment.class);
                startActivity(intent);
            }
        });

        phonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayFees.this,UpiPayment.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}