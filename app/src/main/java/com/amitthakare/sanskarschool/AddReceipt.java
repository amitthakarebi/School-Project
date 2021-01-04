package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddReceipt extends AppCompatActivity {

    DrawerLayout drawerLayoutAddReceipt;
    Toolbar toolbarAddReceipt;

    Button addReceiptBtn;
    TextView receiptMsg;
    private String currentPhotoPath;

    //database
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;

    private String imageUploadUri;

    //Alert Dialogue
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);
        createBuilder();
        ini();
    }

    private void ini() {

        //------------Hooks-------------//
        drawerLayoutAddReceipt = findViewById(R.id.drawerLayoutAddReceipt);
        toolbarAddReceipt = findViewById(R.id.navigationToolbarAddReceipt);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarAddReceipt);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Receipt");
        toolbarAddReceipt.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarAddReceipt.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AddReceipt.this, drawerLayoutAddReceipt, toolbarAddReceipt, R.string.open, R.string.close);
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

        receiptMsg = findViewById(R.id.addReceiptMsg);
        addReceiptBtn = findViewById(R.id.addReceiptBtn);
        addReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(AddReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    alertDialog.show();
                    String fileName = "photo";
                    File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    //File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    File imageFile = new File(storageDirectory + File.separator + fileName + ".jpg");
                    currentPhotoPath = imageFile.getAbsolutePath();
                    Log.e("filename", imageFile.toString());
                    Uri imageUri = FileProvider.getUriForFile(AddReceipt.this, "com.amitthakare.sanskarschool", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 2000);

                } else {
                    ActivityCompat.requestPermissions(AddReceipt.this, new String[]{Manifest.permission.CAMERA}, 100);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                Bitmap imageBitmap = bitmap.copy(bitmap.getConfig(), true);

                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                if (tempUri != null) {
                    saveImgToFirebase(tempUri);
                }

            }else
            {
                Toast.makeText(this, "failed!", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        }else
        {
            Toast.makeText(this, "failed!", Toast.LENGTH_SHORT).show();
            alertDialog.cancel();
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void saveImgToFirebase(Uri imageUri) {

        final int random_int = (int) (Math.random() * (999999999 - 1000000 + 1) + 1000000);


        // upload image to firebase storage
        //below we define StorageReference that will help to store the image into Firebase Storage section
        //storageReference get the root directory.
        //storageReference.child("User/"+currentUser.getUid()+"aadhar.jpg") will create a structure like
        // User folder => Folder name UID  (to differentiate user ) => aadhar.jpg
        final StorageReference fileRef = storageReference.child("User/" + currentUser.getUid() + "/" + random_int + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //when image get uploaded then toast will occurs
                        imageUploadUri = uri.toString();


                        Map<String,String> userData = new HashMap<>();
                        userData.put("Name",Variables.STUDENT_NAME);
                        userData.put("Img",imageUploadUri);
                        FirebaseDatabase.getInstance().getReference("ViewReceipt").push().setValue(userData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(AddReceipt.this, "Image Uploaded!", Toast.LENGTH_LONG).show();
                                            alertDialog.cancel();
                                            finish();
                                            //uploadTextView.setText("Image Selected");
                                        }else
                                        {
                                            Toast.makeText(AddReceipt.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddReceipt.this, "Failed!", Toast.LENGTH_LONG).show();
                alertDialog.cancel();
            }
        });
    }

    private void createBuilder() {

        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.student_logo);
        builder.setTitle("Uploading Receipt...");
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.loading_layout,null);
        builder.setView(dialogView);
        alertDialog = builder.create();

    }

}