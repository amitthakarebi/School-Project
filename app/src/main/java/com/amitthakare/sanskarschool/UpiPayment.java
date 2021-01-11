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
import android.telephony.SmsManager;
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

    CheckBox englishCheck,mathsCheck,scienceCheck;
    EditText upiAmount,upiName,upiTransactionNote,upiId;
    int engAmt, sciAmt, mathsAmt;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_payment);
        createBuilder();
        ini();

        studName = Variables.STUDENT_NAME;
        className = Variables.CLASS;
        upiName.setText(studName);
    }

    private void ini() {

        toolbarUpi = findViewById(R.id.navigationToolbarUpi);
        drawerLayoutUpi = findViewById(R.id.drawerLayoutUPIPayment);

        msg = findViewById(R.id.msgToUser);

        //---------------General Initialization---------//
        englishCheck = findViewById(R.id.englishCheck);
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
                    engAmt = Integer.parseInt(Variables.English_Fees);
                    subjects.add("English");
                    totalAmount += engAmt;
                    upiAmount.setText(totalAmount+"");
                    Toast.makeText(UpiPayment.this, "Rs. "+engAmt+" is added for English subject.", Toast.LENGTH_SHORT).show();
                }else
                {
                    subjects.remove("English");
                    totalAmount-= engAmt;
                    upiAmount.setText(totalAmount+"");
                    Toast.makeText(UpiPayment.this, "Rs. "+engAmt+" is removed for English subject.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mathsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    mathsAmt = Integer.parseInt(Variables.Mathematics_Fees);
                    subjects.add("Mathematics");
                    totalAmount += mathsAmt;
                    upiAmount.setText(totalAmount+"");
                    Toast.makeText(UpiPayment.this, "Rs. "+mathsAmt+" is added for Mathematics subject.", Toast.LENGTH_SHORT).show();
                }else
                {
                    subjects.remove("Mathematics");
                    totalAmount-= mathsAmt;
                    upiAmount.setText(totalAmount+"");
                    Toast.makeText(UpiPayment.this, "Rs. "+mathsAmt+" is removed for Mathematics subject.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scienceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    sciAmt = Integer.parseInt(Variables.Science_Fees);
                    subjects.add("Science");
                    totalAmount += sciAmt;
                    upiAmount.setText(totalAmount+"");
                    Toast.makeText(UpiPayment.this, "Rs. "+sciAmt+" is added for Science subject.", Toast.LENGTH_SHORT).show();
                }else
                {
                    subjects.remove("Science");
                    totalAmount-= sciAmt;
                    upiAmount.setText(totalAmount+"");
                    Toast.makeText(UpiPayment.this, "Rs. "+sciAmt+" is removed for Science subject.", Toast.LENGTH_SHORT).show();
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
                                                FirebaseDatabase.getInstance().getReference("RemainingStudent").child(className).child(firebaseAuth.getCurrentUser().getUid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
                                                                    Toast.makeText(UpiPayment.this, "Success!", Toast.LENGTH_SHORT).show();
                                                                    sendSmS(upiName.getText().toString(),subjects.toString(),date,upiAmount.getText().toString());
                                                                    msg.setText("Successfully Added!");
                                                                    msg.setVisibility(View.VISIBLE);
                                                                    alertDialog.cancel();
                                                                    finish();
                                                                }else
                                                                {
                                                                    Toast.makeText(UpiPayment.this, "Error! Contact Your Teacher.", Toast.LENGTH_SHORT).show();
                                                                    msg.setText("Data sent to admin and student but not remove from Remaining Student.");
                                                                    msg.setVisibility(View.VISIBLE);
                                                                    alertDialog.cancel();
                                                                }
                                                            }
                                                        });
                                            }else
                                            {
                                                Toast.makeText(UpiPayment.this, "Error! Contact Your Teacher.", Toast.LENGTH_SHORT).show();
                                                msg.setText("Data sent to admin but not to student.");
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

    private void sendSmS(String name, String subject, String date, String amount) {

        if (subject!=null) {
            if (subject.startsWith("["))
            {
                subject = subject.replace("[","");
                subject = subject.replace("]","");
            }
        }

        String Msg = "Student Name : "+name+"\nSubjects : "+subject+"\nDate : "+date+"\nAmount : "+amount;
        Toast.makeText(this, Msg, Toast.LENGTH_SHORT).show();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+917767987378",null,Msg,null,null);

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