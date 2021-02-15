package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity {

    DrawerLayout drawerLayoutResetPassword;
    Toolbar toolbarResetPassword;

    FirebaseAuth firebaseAuth;

    private EditText resetPasswordEdittext;
    private Button resetPasswordBtn;
    private TextView msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ini();
    }

    private void ini() {

        //-------------General Hooks-------------//
        firebaseAuth = FirebaseAuth.getInstance();
        resetPasswordBtn = findViewById(R.id.resetPasswordButton);
        resetPasswordEdittext = findViewById(R.id.resetPasswordEditText);
        msg = findViewById(R.id.textMsgResetPassword);

        //------------Hooks-------------//
        drawerLayoutResetPassword = findViewById(R.id.drawerLayoutResetPassword);
        toolbarResetPassword = findViewById(R.id.navigationToolbarResetPassword);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarResetPassword);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");
        toolbarResetPassword.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarResetPassword.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ResetPassword.this,drawerLayoutResetPassword,toolbarResetPassword,R.string.open,R.string.close);
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

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(resetPasswordEdittext.getText().toString()) &&  resetPasswordEdittext.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"))
                {
                    firebaseAuth.sendPasswordResetEmail(resetPasswordEdittext.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ResetPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                    msg.setVisibility(View.VISIBLE);
                                    finish();
                                }
                            });
                }else
                {
                    Toast.makeText(ResetPassword.this, "Enter", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}