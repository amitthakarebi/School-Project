package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class PaidFeesDetail extends AppCompatActivity {

    DrawerLayout drawerLayoutPaidFeesDetail;
    Toolbar toolbarPaidFeesDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_fees_detail);

        ini();
    }

    private void ini() {

        //------------Hooks-------------//
        drawerLayoutPaidFeesDetail = findViewById(R.id.drawerLayoutPaidFeesDetail);
        toolbarPaidFeesDetail = findViewById(R.id.navigationToolbarPaidFeesDetail);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarPaidFeesDetail);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Paid Fees Details");
        toolbarPaidFeesDetail.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarPaidFeesDetail.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PaidFeesDetail.this,drawerLayoutPaidFeesDetail,toolbarPaidFeesDetail,R.string.open,R.string.close);
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