package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminLogin extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView loginForgotPasswordAdmin;

    private EditText adminEmail, adminPassword;
    private Button adminSigninBtn;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        createBuilder();
        ini();

    }

    private void ini() {

        firebaseAuth = FirebaseAuth.getInstance();

        adminEmail = findViewById(R.id.loginAdminEmail);
        adminPassword = findViewById(R.id.loginAdminPassword);
        adminSigninBtn = findViewById(R.id.loginAdminBtn);
        loginForgotPasswordAdmin = findViewById(R.id.loginForgotPasswordAdmin);

        adminSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                if (adminEmail.getText().toString().toLowerCase().equals("amit.thakarebi@gmail.com"))
                {
                    firebaseAuth.signInWithEmailAndPassword(adminEmail.getText().toString(),adminPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        Intent intent = new Intent(AdminLogin.this,AdminDashboard.class);
                                        startActivity(intent);
                                        Toast.makeText(AdminLogin.this, "Successfully Signed In!", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                        finish();
                                    }else
                                    {
                                        Toast.makeText(AdminLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                }
                            });
                }
            }
        });

        loginForgotPasswordAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogin.this,ResetPassword.class);
                startActivity(intent);
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
}