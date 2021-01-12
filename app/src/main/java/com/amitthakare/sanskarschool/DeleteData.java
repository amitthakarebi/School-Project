package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class DeleteData extends AppCompatActivity {

    DrawerLayout drawerLayoutDeleteData;
    Toolbar toolbarDeleteData;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);
        ini();
    }

    private void ini() {

        //------------Hooks-------------//
        drawerLayoutDeleteData = findViewById(R.id.drawerLayoutDeleteData);
        toolbarDeleteData = findViewById(R.id.navigationToolbarDeleteData);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarDeleteData);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Receipt");
        toolbarDeleteData.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarDeleteData.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DeleteData.this, drawerLayoutDeleteData, toolbarDeleteData, R.string.open, R.string.close);
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