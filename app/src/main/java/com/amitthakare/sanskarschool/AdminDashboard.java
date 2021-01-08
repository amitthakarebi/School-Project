package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //navigation and toolbar
    DrawerLayout drawerLayoutAdmin;
    NavigationView navigationViewAdmin;
    Toolbar toolbarAdmin;

    private FirebaseAuth firebaseAuth;

    private Button checkPaymentBtn,addPaymentDetailBtn,viewReceiptBtn,addNewFeesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        ini();
    }

    private void ini() {

        //---------General Hooks------------/
        checkPaymentBtn = findViewById(R.id.checkPaymentBtn);
        addPaymentDetailBtn = findViewById(R.id.addPaymentDetailBtn);
        viewReceiptBtn = findViewById(R.id.viewReceipt);
        addNewFeesData = findViewById(R.id.addNewFeesData);

        firebaseAuth = FirebaseAuth.getInstance();

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

        addPaymentDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this,AddPaymentDetail.class);
                startActivity(intent);
            }
        });

        viewReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this,ViewReceipts.class);
                startActivity(intent);
            }
        });

        addNewFeesData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this,AddNewFeesData.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
        // Set the message show for the Alert time
        builder.setMessage("Click Yes to Logout!");

        // Set Alert Title
        builder.setTitle("Do you want to Logout?");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                //finishAffinity();
                                //finish();
                                //System.exit(0);
                                firebaseAuth.signOut();
                                Intent intent = new Intent(AdminDashboard.this,Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}