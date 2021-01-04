package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    DrawerLayout drawerLayoutUpdateClass;
    Toolbar toolbarUpdateClass;

    TextView currentClass;
    Button updateBtn;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private String classesUpdateClass[] = {"1st STD", "2nd STD", "3rd STD", "4th STD", "5th STD", "6th STD", "7th STD", "8th STD", "9th STD", "10th STD"};
    private Spinner classSpinnerUpdateClass;
    private String classNameUpdateClass = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_class);
        createBuilder();
        ini();
    }

    private void ini() {

        //-------general hooks---------//
        classSpinnerUpdateClass = findViewById(R.id.updateClassSpinner);
        classSpinnerUpdateClass.setOnItemSelectedListener(this);

        currentClass = findViewById(R.id.currentClassTextUpdateClass);
        currentClass.setText("Current Class : "+Variables.CLASS);

        updateBtn = findViewById(R.id.updateClassBtn);

        //-----------hooks----------------//
        drawerLayoutUpdateClass = findViewById(R.id.drawerLayoutUpdateClass);
        toolbarUpdateClass = findViewById(R.id.navigationToolbarUpdateClass);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarUpdateClass);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Upi Payment");
        toolbarUpdateClass.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarUpdateClass.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UpdateClass.this,drawerLayoutUpdateClass,toolbarUpdateClass,R.string.open,R.string.close);
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

        //Creating the ArrayAdapter instance having the Class list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classesUpdateClass);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classSpinnerUpdateClass.setAdapter(arrayAdapter);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();

                FirebaseFirestore.getInstance().collection("StudentInfo").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update("Class",classNameUpdateClass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Variables.CLASS = classNameUpdateClass;
                                    Toast.makeText(UpdateClass.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                    finish();
                                }else
                                {
                                    Toast.makeText(UpdateClass.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        classNameUpdateClass = classesUpdateClass[position];
        Toast.makeText(this, classNameUpdateClass, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createBuilder() {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.student_logo);
        builder.setTitle("Updating Class....");
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.loading_layout,null);
        builder.setView(dialogView);
        alertDialog = builder.create();

    }
}