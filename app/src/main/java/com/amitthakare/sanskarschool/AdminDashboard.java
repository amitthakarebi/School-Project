package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //navigation and toolbar
    DrawerLayout drawerLayoutAdmin;
    NavigationView navigationViewAdmin;
    Toolbar toolbarAdmin;

    private Button checkPaymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        ini();
    }

    private void ini() {

        //---------General Hooks------------/
        checkPaymentBtn = findViewById(R.id.checkPaymentBtn);

        //----------Hooks--------------//
        drawerLayoutAdmin = findViewById(R.id.drawerLayoutAdmin);
        navigationViewAdmin = findViewById(R.id.navigationViewAdmin);
        toolbarAdmin = findViewById(R.id.navigationToolbarAdmin);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarAdmin);
        getSupportActionBar().setTitle("Admin Dashboard");
        toolbarAdmin.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbarAdmin.setTitleTextColor(getResources().getColor(R.color.white));

        //---------Navigation Drawer Menu-----------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AdminDashboard.this,drawerLayoutAdmin,toolbarAdmin,R.string.open,R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayoutAdmin.isDrawerVisible(GravityCompat.START))
                {
                    drawerLayoutAdmin.closeDrawer(GravityCompat.START);
                }else
                {
                    drawerLayoutAdmin.openDrawer(GravityCompat.START);
                }
            }
        });
        //drawerLayoutAdmin.addDrawerListener(toggle);
        toggle.syncState();

        //---------To Set Menu Item Clickable---------//
        navigationViewAdmin.bringToFront();
        navigationViewAdmin.setNavigationItemSelectedListener(this);

        navigationViewAdmin.setCheckedItem(R.id.nav_home);

        checkPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this,CheckPayments.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}