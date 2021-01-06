package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private Button newwStudentRegistrationBtn, loginStudentBtn,teachersLogin;
    private EditText loginStudentEmail, loginStudentPassword;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    //database variables
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createBuilder();
        ini();
        askAllPermission();
    }

    private void askAllPermission() {

        ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.CAMERA
        ,Manifest.permission.INTERNET,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE}, PackageManager.PERMISSION_GRANTED);

    }

    private void ini() {
        newwStudentRegistrationBtn = findViewById(R.id.studentRegistrationMainBtn);
        loginStudentBtn = findViewById(R.id.loginStudentBtn);
        loginStudentEmail = findViewById(R.id.loginStudentEmail);
        teachersLogin = findViewById(R.id.teachersLoginMainBtn);
        loginStudentPassword = findViewById(R.id.loginStudentPassword);
        //database initialize
        firebaseAuth = FirebaseAuth.getInstance();

        newwStudentRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });

        teachersLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,AdminLogin.class);
                startActivity(intent);
            }
        });

        loginStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                firebaseAuth.signInWithEmailAndPassword(loginStudentEmail.getText().toString(),loginStudentPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    getUserData();
                                }else
                                {
                                    Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            }
                        });
            }
        });
    }

    private void createBuilder() {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.student_logo);
        builder.setTitle("Checking User");
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.loading_layout,null);
        builder.setView(dialogView);
        alertDialog = builder.create();

    }

    private void getUserData() {

        FirebaseFirestore.getInstance().collection("StudentInfo").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null)
                        {
                            Variables.STUDENT_NAME = value.get("FullName").toString();
                            Variables.EMAIL_ID = value.get("EmailId").toString();
                            Variables.CLASS = value.get("Class").toString();
                            Variables.MOBILE = value.get("MobileNo").toString();
                            Variables.GENDER = value.get("Gender").toString();
                            Intent intent = new Intent(Login.this,StudentDashboard.class);
                            startActivity(intent);
                            alertDialog.cancel();
                            finish();
                        }else
                        {
                            Toast.makeText(Login.this, "No Data!", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        // Set the message show for the Alert time
        builder.setMessage("Click Yes to exit!");

        // Set Alert Title
        builder.setTitle("Do you want to Exit?");

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
                                finish();
                                System.exit(0);
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