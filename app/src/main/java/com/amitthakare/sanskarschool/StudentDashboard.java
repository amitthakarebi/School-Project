package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class StudentDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //firebase variables
    private FirebaseAuth firebaseAuth;

    //navigation and toolbar
    DrawerLayout drawerLayoutStudent;
    NavigationView navigationViewStudent;
    Toolbar toolbarStudent;

    CardView payfees,addreceipt,paidfeesdetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        ini();

    }


    private void ini() {

        //----------General Hooks--------------//
        firebaseAuth = FirebaseAuth.getInstance();

        //----------Hooks--------------//
        drawerLayoutStudent = findViewById(R.id.drawerLayoutStudent);
        navigationViewStudent = findViewById(R.id.navigationViewStudent);
        toolbarStudent = findViewById(R.id.navigationToolbarStudent);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarStudent);
        getSupportActionBar().setTitle("Student Dashboard");
        toolbarStudent.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbarStudent.setTitleTextColor(getResources().getColor(R.color.white));

        //---------Navigation Drawer Menu-----------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(StudentDashboard.this,drawerLayoutStudent,toolbarStudent,R.string.open,R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayoutStudent.isDrawerVisible(GravityCompat.START))
                {
                    drawerLayoutStudent.closeDrawer(GravityCompat.START);
                }else
                {
                    drawerLayoutStudent.openDrawer(GravityCompat.START);
                }
            }
        });
        //drawerLayoutStudent.addDrawerListener(toggle);
        toggle.syncState();

        //---------To Set Menu Item Clickable---------//
        navigationViewStudent.bringToFront();
        navigationViewStudent.setNavigationItemSelectedListener(this);

        navigationViewStudent.setCheckedItem(R.id.nav_home);

        payfees = findViewById(R.id.payFeesCV);
        payfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboard.this,PayFees.class);
                startActivity(intent);
            }
        });

        addreceipt = findViewById(R.id.addReceiptCardView);
        addreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboard.this,AddReceipt.class);
                startActivity(intent);
            }
        });

        paidfeesdetails = findViewById(R.id.paidfeesdetailCardView);
        paidfeesdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboard.this,PaidFeesDetail.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentDashboard.this);
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
                                Intent intent = new Intent(StudentDashboard.this,Login.class);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_home :
                break;
            case R.id.nav_update_class :
                Toast.makeText(this, "Update Class", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "Nav Update", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }
}