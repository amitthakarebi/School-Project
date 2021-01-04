package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.amitthakare.sanskarschool.Adapter.RecyclerAdapter;
import com.amitthakare.sanskarschool.Model.ModelList;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PaidFeesDetail extends AppCompatActivity {

    DrawerLayout drawerLayoutPaidFeesDetail;
    Toolbar toolbarPaidFeesDetail;

    RecyclerView recyclerViewPFD;
    RecyclerAdapter adapterPFD;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_fees_detail);

        ini();
    }

    private void ini() {

        recyclerViewPFD = findViewById(R.id.paidfeesdetailRecyclerView);
        recyclerViewPFD.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance();


        //------------Hooks-------------//
        drawerLayoutPaidFeesDetail = findViewById(R.id.drawerLayoutPaidFeesDetail);
        toolbarPaidFeesDetail = findViewById(R.id.navigationToolbarPaidFeesDetail);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarPaidFeesDetail);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Paid Fees Details");
        toolbarPaidFeesDetail.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarPaidFeesDetail.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PaidFeesDetail.this,drawerLayoutPaidFeesDetail,toolbarPaidFeesDetail,R.string.open,R.string.close);
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

        FirebaseRecyclerOptions<ModelList> options =
                new FirebaseRecyclerOptions.Builder<ModelList>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PaidStudent").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), ModelList.class)
                        .build();

        adapterPFD = new RecyclerAdapter(options);
        recyclerViewPFD.setAdapter(adapterPFD);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterPFD.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterPFD.stopListening();
    }
}