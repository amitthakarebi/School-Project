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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.material.snackbar.Snackbar;
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
    String TAG ="main";
    final int UPI_PAYMENT = 0;


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
                    Snackbar.make(findViewById(R.id.drawerLayoutUPIPayment),"Rs. "+engAmt+" is added for English subject.",Snackbar.LENGTH_LONG).show();
                }else
                {
                    subjects.remove("English");
                    totalAmount-= engAmt;
                    upiAmount.setText(totalAmount+"");
                    Snackbar.make(findViewById(R.id.drawerLayoutUPIPayment),"Rs. "+engAmt+" is removed for English subject.",Snackbar.LENGTH_LONG).show();
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
                    Snackbar.make(findViewById(R.id.drawerLayoutUPIPayment),"Rs. "+mathsAmt+" is added for Mathematics subject.",Snackbar.LENGTH_LONG).show();
                }else
                {
                    subjects.remove("Mathematics");
                    totalAmount-= mathsAmt;
                    upiAmount.setText(totalAmount+"");
                    Snackbar.make(findViewById(R.id.drawerLayoutUPIPayment),"Rs. "+mathsAmt+" is removed for Mathematics subject.",Snackbar.LENGTH_LONG).show();
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
                    Snackbar.make(findViewById(R.id.drawerLayoutUPIPayment),"Rs. "+sciAmt+" is added for Science subject.",Snackbar.LENGTH_LONG).show();
                }else
                {
                    subjects.remove("Science");
                    totalAmount-= sciAmt;
                    upiAmount.setText(totalAmount+"");
                    Snackbar.make(findViewById(R.id.drawerLayoutUPIPayment),"Rs. "+sciAmt+" is removed for Science subject.",Snackbar.LENGTH_LONG).show();
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
                //uri =getUpiUri(PAYERNAME,UPIID,MSGNOTE,SENDAMOUNT);
                //payWithGpay(GPAY_PACKAGE_NAME);
                //sendDataToFirebaseDatabase();

                payUsingUpi(upiName.getText().toString(), upiId.getText().toString(), upiTransactionNote.getText().toString(), upiAmount.getText().toString());
            }

        }else
        {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }

    }




    void payUsingUpi(  String name,String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", "25584786")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(UpiPayment.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }

    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(UpiPayment.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                //Toast.makeText(UpiPayment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                alertDialog.show();
                sendDataToFirebaseDatabase();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(UpiPayment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(UpiPayment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(UpiPayment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    private void sendDataToFirebaseDatabase() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss", Locale.getDefault());;
        final String date1 = dateFormat.format(calendar.getTime());


        userdata.put("Name", upiName.getText().toString().toUpperCase());
        userdata.put("Sub", "[" + subjects.toString() + "]");
        userdata.put("Date", date);
        userdata.put("Amount", upiAmount.getText().toString());
        userdata.put("TransactionNote", upiTransactionNote.getText().toString());
        firebaseDatabase.getReference("AdminPaidStudent").child(Variables.currentMonthString).child(className).child(date1).setValue(userdata)
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
                                                FirebaseDatabase.getInstance().getReference("RemainingStudent").child(Variables.currentMonthString).child(className).child(firebaseAuth.getCurrentUser().getUid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
                                                                    Toast.makeText(UpiPayment.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
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