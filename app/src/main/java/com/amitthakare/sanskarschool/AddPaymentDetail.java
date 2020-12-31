package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddPaymentDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayoutAddPaymentDetails;
    Toolbar toolbarAddPaymentDetails;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    Map<String, String> userdata = new HashMap<>();

    private Button addDetailBtn;
    private EditText addDetailName, addDetailAmount, addDetailDate, addDetailTransactionNote, addDetailSubject;
    private TextView successMsg;

    private String classesAddPayment[] = {"1st STD", "2nd STD", "3rd STD", "4th STD", "5th STD", "6th STD", "7th STD", "8th STD", "9th STD", "10th STD"};
    private Spinner classSpinnerAddPayment;
    private String classNameAddPayment = "No";

    //Alert Dialogue
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private boolean alreadyPresent = false;
    private String dName = "", dAmount = "", dDate = "", dSub = "", dTrans = "";
    private String uid = "";

    int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_detail);
        createBuilder();
        ini();
    }

    private void ini() {

        drawerLayoutAddPaymentDetails = findViewById(R.id.drawerLayoutAddPaymentDetails);
        toolbarAddPaymentDetails = findViewById(R.id.navigationToolbarAddPaymentDetail);

        addDetailName = findViewById(R.id.addDetailsStudentName);
        addDetailAmount = findViewById(R.id.addDetailsPaidAmount);
        addDetailDate = findViewById(R.id.addDetailsPaidDate);
        addDetailTransactionNote = findViewById(R.id.addDetailsNote);
        addDetailSubject = findViewById(R.id.addDetailsSubject);
        addDetailBtn = findViewById(R.id.addDetailBtn);
        successMsg = findViewById(R.id.successMsg);
        classSpinnerAddPayment = findViewById(R.id.classSpinnerAddPayment);
        classSpinnerAddPayment.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarAddPaymentDetails);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Payment Detail");
        toolbarAddPaymentDetails.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarAddPaymentDetails.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AddPaymentDetail.this, drawerLayoutAddPaymentDetails, toolbarAddPaymentDetails, R.string.open, R.string.close);
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

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classesAddPayment);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classSpinnerAddPayment.setAdapter(arrayAdapter);


        addDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialog.show();
                if (!classNameAddPayment.equals("No")) {
                    if (!TextUtils.isEmpty(addDetailName.getText().toString())) {
                        if (!TextUtils.isEmpty(addDetailAmount.getText().toString())) {
                            if (!TextUtils.isEmpty(addDetailDate.getText().toString())) {
                                if (!TextUtils.isEmpty(addDetailSubject.getText().toString())) {
                                    if (!TextUtils.isEmpty(addDetailTransactionNote.getText().toString())) {

                                        checkAlreadyPresentInFirebase();

                                    } else {
                                        Toast.makeText(AddPaymentDetail.this, "Enter Transaction Note!", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                } else {
                                    Toast.makeText(AddPaymentDetail.this, "Enter Subject!", Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            } else {
                                Toast.makeText(AddPaymentDetail.this, "Enter Proper Date!", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        } else {
                            Toast.makeText(AddPaymentDetail.this, "Enter Amount!", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    } else {
                        Toast.makeText(AddPaymentDetail.this, "Enter Name of Student!", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                } else {
                    Toast.makeText(AddPaymentDetail.this, "Select Class!", Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        classNameAddPayment = classesAddPayment[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createBuilder() {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.student_logo);
        builder.setTitle("Adding Details...");
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.loading_layout, null);
        builder.setView(dialogView);
        alertDialog = builder.create();

    }


    private void checkAlreadyPresentInFirebase() {

        firebaseDatabase.getReference("StudentInfo").child(classNameAddPayment)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.getValue().toString().equals(addDetailName.getText().toString())) {
                                alreadyPresent = true;
                                uid = snapshot1.getKey();
                                Log.e("Presence", "Already Present");
                                Log.e("Uid", uid);
                                break;
                            }
                        }
                        if (alreadyPresent) {
                            retrieveDataFromFirebase(uid);
                        } else {
                            sendDataToFirebaseDatabase();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void retrieveDataFromFirebase(String uid) {

        firebaseDatabase.getReference("PaidStudent").child(classNameAddPayment).child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.getKey().equals("Name")) {
                                dName = snapshot1.getValue().toString();
                                Log.e("Name", dName);
                            } else if (snapshot1.getKey().equals("Amount")) {
                                dAmount = snapshot1.getValue().toString();
                                Log.e("Amount", dAmount);
                            } else if (snapshot1.getKey().equals("Date")) {
                                dDate = snapshot1.getValue().toString();
                                Log.e("Date", dDate);
                            } else if (snapshot1.getKey().equals("Sub")) {
                                dSub = snapshot1.getValue().toString();
                                Log.e("Sub", dSub);
                            } else if (snapshot1.getKey().equals("TransactionNote")) {
                                dTrans = snapshot1.getValue().toString();
                                Log.e("TransactionNote", dTrans);
                            }
                        }
                        if (i==1)
                        {
                            i++;
                            sendDataToFirebaseDatabase(dAmount,dTrans,dSub,dDate,dName);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendDataToFirebaseDatabase(String dAmount, String dTrans, String dSub, String dDate, String dName) {

        userdata.put("Name", addDetailName.getText().toString());
        userdata.put("Sub", "[" + dSub + ", " + addDetailSubject.getText().toString() + "]");
        userdata.put("Date", dDate + ", " + addDetailDate.getText().toString());
        userdata.put("Amount", dAmount + ", " + addDetailAmount.getText().toString());
        userdata.put("TransactionNote", dTrans + ", " + addDetailTransactionNote.getText().toString());
        firebaseDatabase.getReference("PaidStudent").child(classNameAddPayment).child(uid).setValue(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("WithArgument :", userdata.toString());
                            Toast.makeText(AddPaymentDetail.this, "Success!", Toast.LENGTH_SHORT).show();
                            successMsg.setText("Successfully Added!");
                            successMsg.setVisibility(View.VISIBLE);
                            alertDialog.cancel();
                            finish();
                        } else {
                            Toast.makeText(AddPaymentDetail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            successMsg.setText("Error : " + task.getException().getMessage());
                            successMsg.setVisibility(View.VISIBLE);
                            alertDialog.cancel();
                        }
                    }
                });
    }

    private void sendDataToFirebaseDatabase() {
        userdata.put("Name", addDetailName.getText().toString());
        userdata.put("Sub", "[" + addDetailSubject.getText().toString() + "]");
        userdata.put("Date", addDetailDate.getText().toString());
        userdata.put("Amount", addDetailAmount.getText().toString());
        userdata.put("TransactionNote", addDetailTransactionNote.getText().toString());
        firebaseDatabase.getReference("PaidStudent").child(classNameAddPayment).child(firebaseAuth.getCurrentUser().getUid()).setValue(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("withoutArgument :", userdata.toString());
                            Toast.makeText(AddPaymentDetail.this, "Success!", Toast.LENGTH_SHORT).show();
                            successMsg.setText("Successfully Added!");
                            successMsg.setVisibility(View.VISIBLE);
                            alertDialog.cancel();
                            finish();
                        } else {
                            Toast.makeText(AddPaymentDetail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            successMsg.setText("Error : " + task.getException().getMessage());
                            successMsg.setVisibility(View.VISIBLE);
                            alertDialog.cancel();
                        }
                    }
                });
    }
}