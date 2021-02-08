package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class DeleteData extends AppCompatActivity{

    DrawerLayout drawerLayoutDeleteData;
    Toolbar toolbarDeleteData;

    private String classesDeleteData[] = {"1st STD","2nd STD","3rd STD","4th STD","5th STD","6th STD","7th STD","8th STD","9th STD","10th STD"};
    private String monthsDeleteData[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    private Spinner classWiseMonthSpinner, classWiseClassSpinner, monthWiseClassSpinner;

    private String classWiseMonthName , classWiseClassName, monthWiseClassName;
    private Button deleteClassWiseDataBtn, deleteMonthWiseDataBtn ,deleteAllClassDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);
        ini();
    }

    private void ini() {

        //-----------General Hooks------------//

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        classWiseMonthSpinner = findViewById(R.id.classWiseMonthSpinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,monthsDeleteData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classWiseMonthSpinner.setAdapter(aa);
        classWiseMonthSpinner.setOnItemSelectedListener(new ClassWiseMonth());

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        classWiseClassSpinner = findViewById(R.id.classWiseClassSpinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter a = new ArrayAdapter(this,android.R.layout.simple_spinner_item,classesDeleteData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classWiseClassSpinner.setAdapter(a);
        classWiseClassSpinner.setOnItemSelectedListener(new ClassWiseClass());

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        monthWiseClassSpinner = findViewById(R.id.monthWiseClassSpinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,classesDeleteData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        monthWiseClassSpinner.setAdapter(a);
        monthWiseClassSpinner.setOnItemSelectedListener(new MonthWiseClass());


        deleteAllClassDataBtn = findViewById(R.id.deleteDataAllBtn);
        deleteClassWiseDataBtn = findViewById(R.id.deleteDataClassBtn);
        deleteMonthWiseDataBtn = findViewById(R.id.deleteDataMonthBtn);

        //------------Hooks-------------//
        drawerLayoutDeleteData = findViewById(R.id.drawerLayoutDeleteData);
        toolbarDeleteData = findViewById(R.id.navigationToolbarDeleteData);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarDeleteData);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Delete Data");
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

        //delete selected class data
        deleteClassWiseDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClassWiseData();
            }
        });

        //delete monthwise data
        deleteMonthWiseDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMonthWiseData();
            }
        });

        //delete all class data
        deleteAllClassDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().removeValue();
            }
        });

    }

    //----------------Month Wise-------------------------------------//

    private void deleteMonthWiseData()
    {
        FirebaseDatabase.getInstance().getReference("AdminPaidStudent").child(monthWiseClassName).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(DeleteData.this, "Data Deleted!", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(DeleteData.this, "Data Not Deleted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    class MonthWiseClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            monthWiseClassName = classesDeleteData[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    //----------------Class Wise-------------------------------------//

    private void deleteClassWiseData() {

        FirebaseDatabase.getInstance().getReference("AdminPaidStudent").child(classWiseMonthName).child(classWiseClassName).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(DeleteData.this, "Data Deleted!", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(DeleteData.this, "Data Not Deleted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    class ClassWiseMonth implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            classWiseMonthName = monthsDeleteData[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class ClassWiseClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            classWiseClassName = classesDeleteData[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}