package com.amitthakare.sanskarschool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class UpiPayment extends AppCompatActivity {

    DrawerLayout drawerLayoutUpi;
    Toolbar toolbarUpi;

    CheckBox englishCheck,marathiCheck,hindiCheck,historyCheck,physicsCheck,chemistryCheck,sanskrutCheck,mathsCheck,scienceCheck;
    EditText upiAmount,upiName,upiTransactionNote,upiId;
    Button payNowBtn;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date;

    int totalAmount=0;
    ArrayList<String> subjects = new ArrayList<>();

    //Database values variable
    String className,studName;

    String uid;
    boolean isAlreadyPresent = false;
    int i=0;

    //Alert Dialogue
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    Map<String,String> userdata;
    int amt;

    //Google Pay Variables
    public static final String GPAY_PACKAGE_NAME ="com.google.android.apps.nbu.paisa.user";  // Google Pay Package so that we can trigger the google pay app
    private TextView msg;
    private Uri uri; // it is use for location of the particular content in this case the Google Pay address
    private static  String PAYERNAME, UPIID, MSGNOTE, SENDAMOUNT, STATUS;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private String dName,dAmount,dDate,dSub,dTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_payment);
        createBuilder();
        ini();
        getFirebaseData();
    }

    private void getFirebaseData() {
        firestore.collection("StudentInfo").document(firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null)
                        {
                            studName = value.get("FullName").toString();
                            className = value.get("Class").toString();
                            upiName.setText(studName);
                        }
                    }
                });
    }

    private void ini() {

        toolbarUpi = findViewById(R.id.navigationToolbarUpi);
        drawerLayoutUpi = findViewById(R.id.drawerLayoutUPIPayment);

        msg = findViewById(R.id.msgToUser);

        //---------------General Initialization---------//
        englishCheck = findViewById(R.id.englishCheck);
        marathiCheck = findViewById(R.id.marathiCheck);
        hindiCheck = findViewById(R.id.hindiCheck);
        historyCheck = findViewById(R.id.historyCheck);
        physicsCheck = findViewById(R.id.physicsCheck);
        chemistryCheck = findViewById(R.id.chemistryCheck);
        sanskrutCheck = findViewById(R.id.sanskrutCheck);
        mathsCheck = findViewById(R.id.mathCheck);
        scienceCheck = findViewById(R.id.scienceCheck);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = simpleDateFormat.format(calendar.getTime());


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        upiAmount = findViewById(R.id.upiAmount);
        upiName = findViewById(R.id.upiName);
        upiTransactionNote = findViewById(R.id.upiTransactionNote);
        upiId = findViewById(R.id.upiId);

        payNowBtn = findViewById(R.id.upiPayNowBtn);
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pay method
                payNowCheck();
            }
        });

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarUpi);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Upi Payment");
        toolbarUpi.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarUpi.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UpiPayment.this,drawerLayoutUpi,toolbarUpi,R.string.open,R.string.close);
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

        englishCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("English");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("English");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        marathiCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Marathi");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Marathi");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        hindiCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Hindi");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Hindi");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        historyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("History");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("History");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        physicsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Physics");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Physics");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        chemistryCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Chemistry");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Chemistry");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        sanskrutCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Sanskrut");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Sanskrut");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        mathsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Mathematics");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Mathematics");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

        scienceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    subjects.add("Science");
                    totalAmount += 1;
                    upiAmount.setText(totalAmount+"");
                }else
                {
                    subjects.remove("Science");
                    totalAmount-= 1;
                    upiAmount.setText(totalAmount+"");
                }
            }
        });

    }

    private void payNowCheck() {

        // To generate when user pay for two subjects differently then we can use the random number to differentiate the subject.
        //Random random = new Random();
        //int randomNumber = random.nextInt();
        //Toast.makeText(this, randomNumber+"", Toast.LENGTH_SHORT).show();

        userdata = new HashMap<>();

        if (upiAmount.getText().toString().equals(""))
        {
            amt = 0;
        }else
        {
            amt = Integer.parseInt(upiAmount.getText().toString());
        }
        if (upiTransactionNote!=null)
        {
            userdata.put("TransactionNote",upiTransactionNote.getText().toString());
        }

        if (!TextUtils.isEmpty(upiAmount.getText().toString()) && amt != 0)
        {

            PAYERNAME = studName;
            UPIID = upiId.getText().toString();
            SENDAMOUNT = String.valueOf(totalAmount);
            MSGNOTE = upiTransactionNote.getText().toString();

            if (!PAYERNAME.equals("") && !UPIID.equals("") && !MSGNOTE.equals("") && !SENDAMOUNT.equals(""))
            {
                uri =getUpiUri(PAYERNAME,UPIID,MSGNOTE,SENDAMOUNT);
                //payWithGpay(GPAY_PACKAGE_NAME);
                sendDataToFirebaseDatabase();
            }

        }else
        {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void payWithGpay(String gpayPackageName) {

        if(isAppInstalled(this,gpayPackageName)) // herre we called the isAppInstalled method which checks the google pay is available on the device or not.
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri); // here we set the uri means google pay app location into uri so that the intent will call the google pay app
            intent.setPackage(gpayPackageName);
            startActivityForResult(intent,0); // here we send the intent to result activity with request code so that we differentiate the result.
        }else
        {
            Toast.makeText(UpiPayment.this,"Google Pay is not installed. Please install and try again.",Toast.LENGTH_LONG).show();
        }

    }

    private Uri getUpiUri(String payername, String upiid, String msgnote, String sendamount) {

        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiid)
                .appendQueryParameter("pn",payername)
                .appendQueryParameter("tn",msgnote)
                .appendQueryParameter("am",sendamount)
                .appendQueryParameter("cu","INR")
                .build();

    }

    public static boolean isAppInstalled(Context context, String packageName) { // it will check if app is install or not
        try{
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null)
        {
            STATUS =data.getStringExtra("Status");
        }

        if (RESULT_OK == resultCode && STATUS.equals("SUCCESS"))
        {

            // here we need to check if user has already registered any courses and for that we need to check if already present method
            // after that retrieve date from firebase and store in the variable
            // after that call to send data to firebase method with all parameters
            // and if user has not subscribe any subject then it will directly get called to simple method and data will be registered.
            //Toast.makeText(UpiPayment.this, "Transaction Successful.", Toast.LENGTH_SHORT).show();
            alertDialog.show();
           sendDataToFirebaseDatabase();

        }else
        {
            Toast.makeText(UpiPayment.this, "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show();
        }

    }


    private void sendDataToFirebaseDatabase() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss", Locale.getDefault());;
        final String date1 = dateFormat.format(calendar.getTime());


        userdata.put("Name", upiName.getText().toString());
        userdata.put("Sub", "[" + subjects.toString() + "]");
        userdata.put("Date", date);
        userdata.put("Amount", upiAmount.getText().toString());
        userdata.put("TransactionNote", upiTransactionNote.getText().toString());
        firebaseDatabase.getReference("AdminPaidStudent").child(className).child(date1).setValue(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            firebaseDatabase.getReference("PaidStudent").child(firebaseAuth.getCurrentUser().getUid()).child(date1).setValue(userdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Log.e("withoutArgument :", userdata.toString());
                                                Toast.makeText(UpiPayment.this, "Success!", Toast.LENGTH_SHORT).show();
                                                msg.setText("Successfully Added!");
                                                msg.setVisibility(View.VISIBLE);
                                                alertDialog.cancel();
                                                finish();
                                            }else
                                            {
                                                Toast.makeText(UpiPayment.this, "Error! Contact Your Teacher.", Toast.LENGTH_SHORT).show();
                                                msg.setText("Error : Contact Your Teacher.");
                                                msg.setVisibility(View.VISIBLE);
                                                alertDialog.cancel();
                                            }
                                        }
                                    });


                        } else {
                            Toast.makeText(UpiPayment.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            msg.setText("Error : " + task.getException().getMessage());
                            msg.setVisibility(View.VISIBLE);
                            alertDialog.cancel();
                        }
                    }
                });
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

}