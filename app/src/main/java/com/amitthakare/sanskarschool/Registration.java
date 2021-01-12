package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String classes[] = {"1st STD", "2nd STD", "3rd STD", "4th STD", "5th STD", "6th STD", "7th STD", "8th STD", "9th STD", "10th STD"};
    private Spinner classSpinner;
    private String className = "No";
    private EditText fullname, email, mobile, password, confirmPassword;
    private String gender = "No";
    private RadioGroup genderRadioGroup;
    private Button registerBtn;

    //Firebase Databae Variables
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase realtimeDatabase;

    //Alert Dialogue
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        createBuilder();
        ini();
    }

    private void ini() {

        //database initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        realtimeDatabase = FirebaseDatabase.getInstance();

        fullname = findViewById(R.id.regiFullName);
        email = findViewById(R.id.regiEmailId);
        mobile = findViewById(R.id.regiMobileNo);
        password = findViewById(R.id.regiPassword);
        confirmPassword = findViewById(R.id.regiConfirmPassword);
        genderRadioGroup = findViewById(R.id.gender);
        registerBtn = findViewById(R.id.registerBtn);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        classSpinner = findViewById(R.id.regiSpinner);
        classSpinner.setOnItemSelectedListener(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                registerUser();
            }
        });

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb != null && rb.isChecked()) {
                    gender = (String) rb.getText();
                }
            }
        });

        //Creating the ArrayAdapter instance having the class list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        classSpinner.setAdapter(aa);
    }

    private void registerUser() {
        if (!TextUtils.isEmpty(fullname.getText())) {
            if (!TextUtils.isEmpty(email.getText())) {
                if (!TextUtils.isEmpty(mobile.getText()) && mobile.length() == 10) {
                    if (!TextUtils.isEmpty(password.getText())) {
                        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                            if (!gender.equals("No")) {
                                if (!className.equals("No")) {
                                    //send data to firebase with information and create user data
                                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Map<String, Object> userdata = new HashMap<>();
                                                        userdata.put("FullName", fullname.getText().toString());
                                                        userdata.put("EmailId", email.getText().toString());
                                                        userdata.put("MobileNo", mobile.getText().toString());
                                                        userdata.put("Class", className);
                                                        userdata.put("Gender", gender);

                                                        firebaseFirestore.collection("StudentInfo")
                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                .set(userdata)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DatabaseReference myRef = realtimeDatabase.getReference("StudentInfo").child(className).child(firebaseAuth.getCurrentUser().getUid());
                                                                            myRef.setValue(fullname.getText().toString())
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {

                                                                                                FirebaseDatabase.getInstance().getReference("RemainingStudent").child(Variables.currentMonthString).child(className)
                                                                                                        .child(firebaseAuth.getCurrentUser().getUid()).child("Name").setValue(fullname.getText().toString())
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    Toast.makeText(Registration.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                                                                                                    alertDialog.cancel();
                                                                                                                    finish();
                                                                                                                } else {
                                                                                                                    Toast.makeText(Registration.this, "Contact your staff to check details! \n Remaining student data is not submitted!", Toast.LENGTH_SHORT).show();
                                                                                                                    alertDialog.cancel();
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                            } else {
                                                                                                Toast.makeText(Registration.this, "Contact your staff to check details! \n Realtime student data is not submitted!", Toast.LENGTH_SHORT).show();
                                                                                                alertDialog.cancel();
                                                                                            }
                                                                                        }
                                                                                    });

                                                                        } else {
                                                                            Toast.makeText(Registration.this, "Contact your staff to check details!\n Student information is not submitted.", Toast.LENGTH_SHORT).show();
                                                                            alertDialog.cancel();
                                                                            //firebaseAuth.getCurrentUser().delete();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        alertDialog.cancel();
                                                    }
                                                }
                                            });

                                } else {
                                    Toast.makeText(this, "Select proper class!", Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            } else {
                                Toast.makeText(this, "Select gender!", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                        } else {
                            Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    } else {
                        Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                } else {
                    Toast.makeText(this, "Enter valid mobile no!", Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            } else {
                Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        } else {
            Toast.makeText(this, "Please enter fullname!", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
        }
    }

    //below two methods is for class spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        className = classes[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createBuilder() {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.student_logo);
        builder.setTitle("Registering User");
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.loading_layout, null);
        builder.setView(dialogView);
        alertDialog = builder.create();

    }
}