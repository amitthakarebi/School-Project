package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewFeesData extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DrawerLayout drawerLayoutAddNewFeesData;
    Toolbar toolbarAddNewFeesData;

    private String classesAddNewFeesData[] = {"1st STD", "2nd STD", "3rd STD", "4th STD", "5th STD", "6th STD", "7th STD", "8th STD", "9th STD", "10th STD"};
    private Spinner classSpinnerAddNewFeesData;
    private String classNameAddNewFeesData = "No";

    private String eng,sci,math;
    private EditText anfdEnglish, anfdScience, anfdMathematics;
    private Button updateFeesDataBtn;

    //Alert Dialogue
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_fees_data);
        createBuilder();
        ini();
    }

    private void ini() {

        //------------general hooks----------//
        anfdEnglish = findViewById(R.id.anfdEnglish);
        anfdScience = findViewById(R.id.anfdScience);
        anfdMathematics = findViewById(R.id.anfdMathematics);
        classSpinnerAddNewFeesData = findViewById(R.id.addNewFeesDataSpinner);
        classSpinnerAddNewFeesData.setOnItemSelectedListener(this);
        updateFeesDataBtn = findViewById(R.id.updateFeesDataBtn);

        //Creating the ArrayAdapter instance having the class list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,classesAddNewFeesData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classSpinnerAddNewFeesData.setAdapter(aa);

        //------------Hooks-------------//
        drawerLayoutAddNewFeesData = findViewById(R.id.drawerLayoutAddNewFeesData);
        toolbarAddNewFeesData = findViewById(R.id.navigationToolbarAddNewFeesData);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarAddNewFeesData);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add New Fees Data");
        toolbarAddNewFeesData.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarAddNewFeesData.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AddNewFeesData.this, drawerLayoutAddNewFeesData, toolbarAddNewFeesData, R.string.open, R.string.close);
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

        updateFeesDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                if (!TextUtils.isEmpty(anfdEnglish.getText().toString()))
                {
                    if (!TextUtils.isEmpty(anfdScience.getText().toString()))
                    {
                        if (!TextUtils.isEmpty(anfdMathematics.getText().toString()))
                        {
                            if (!anfdEnglish.getText().toString().equals("0") && !anfdScience.getText().toString().equals("0") &&
                                    !anfdMathematics.getText().toString().equals("0"))
                            {
                                updateFeesData();
                            }else
                            {
                                Toast.makeText(AddNewFeesData.this, "Fees cannot be 0!", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        }else
                        {
                            Toast.makeText(AddNewFeesData.this, "Fees cannot be blank", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    }else
                    {
                        Toast.makeText(AddNewFeesData.this, "Fees cannot be blank", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                }else
                {
                    Toast.makeText(AddNewFeesData.this, "Fees cannot be blank", Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        classNameAddNewFeesData = classesAddNewFeesData[position];
        getFirebaseData(classesAddNewFeesData[position]);
    }

    private void getFirebaseData(String classesAddNewFeesData) {

        FirebaseFirestore.getInstance().collection("FeesData").document(classesAddNewFeesData)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null)
                        {
                            eng = value.getString("English");
                            sci = value.getString("Science");
                            math = value.getString("Mathematics");

                            anfdEnglish.setText(eng);
                            anfdScience.setText(sci);
                            anfdMathematics.setText(math);
                        }
                    }
                });

    }

    private void updateFeesData()
    {
        Map<String,Object> feesData = new HashMap<>();
        feesData.put("English",anfdEnglish.getText().toString());
        feesData.put("Science",anfdScience.getText().toString());
        feesData.put("Mathematics",anfdMathematics.getText().toString());
        FirebaseFirestore.getInstance().collection("FeesData").document(classNameAddNewFeesData)
                .update(feesData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AddNewFeesData.this, "Fees Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }else
                        {
                            Toast.makeText(AddNewFeesData.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    }
                });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createBuilder() {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.student_logo);
        builder.setTitle("Updating Fees Data...");
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.loading_layout,null);
        builder.setView(dialogView);
        alertDialog = builder.create();

    }
}