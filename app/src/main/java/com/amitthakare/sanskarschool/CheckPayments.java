package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;

public class CheckPayments extends AppCompatActivity{

    private String classesCP[] = {"1st STD","2nd STD","3rd STD","4th STD","5th STD","6th STD","7th STD","8th STD","9th STD","10th STD"};
    private Spinner classSpinnerCP;

    private String monthsCP[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private Spinner monthSpinnerCP;

    DrawerLayout drawerLayoutCheckPayments;
    Toolbar toolbarCheckPayments;

    private Button paidStudentBtn, remainingStudentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payments);
        ini();
    }

    private void ini() {

        //------------Hooks-------------//
        paidStudentBtn = findViewById(R.id.paidStudentBtn);
        remainingStudentBtn = findViewById(R.id.remainStudentBtn);
        drawerLayoutCheckPayments = findViewById(R.id.drawerLayoutCheckPayments);
        toolbarCheckPayments = findViewById(R.id.navigationToolbarCheckPayments);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarCheckPayments);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Upi Payment");
        toolbarCheckPayments.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarCheckPayments.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(CheckPayments.this,drawerLayoutCheckPayments,toolbarCheckPayments,R.string.open,R.string.close);
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

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        classSpinnerCP = findViewById(R.id.classSpinner);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,classesCP);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classSpinnerCP.setAdapter(aa);
        classSpinnerCP.setOnItemSelectedListener(new ClassNameSpinnerClass());


        monthSpinnerCP = findViewById(R.id.monthSpinner);
        monthSpinnerCP.setOnItemSelectedListener(new MonthSpinnerClass());

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a = new ArrayAdapter(this,android.R.layout.simple_spinner_item,monthsCP);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        monthSpinnerCP.setAdapter(a);
        int month = Variables.currentMonth - 1;
        monthSpinnerCP.setSelection(month);


        paidStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckPayments.this,ListOfStudents.class);
                startActivity(intent);
            }
        });

        remainingStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckPayments.this,ListOfStudents2.class);
                startActivity(intent);
            }
        });

    }

    class ClassNameSpinnerClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Variables.ListClass = classesCP[position];
            Toast.makeText(CheckPayments.this, Variables.ListClass, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class MonthSpinnerClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Variables.ListMonth = monthsCP[position];
            Toast.makeText(CheckPayments.this, Variables.ListMonth, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}