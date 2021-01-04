package com.amitthakare.sanskarschool;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.amitthakare.sanskarschool.Adapter.ReceiptAdapter;
import com.amitthakare.sanskarschool.Adapter.RecyclerAdapter;
import com.amitthakare.sanskarschool.Model.ModelList;
import com.amitthakare.sanskarschool.Model.ReceiptModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ViewReceipts extends AppCompatActivity {

    DrawerLayout drawerLayoutViewReceipt;
    Toolbar toolbarViewReceipt;

    RecyclerView recyclerViewReceipt;
    ReceiptAdapter receiptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipts);
        ini();
    }

    private void ini() {

        //--------general hooks---------//
        recyclerViewReceipt = findViewById(R.id.recyclerViewReceipt);
        recyclerViewReceipt.setLayoutManager(new LinearLayoutManager(this));

        //------------Hooks-------------//
        drawerLayoutViewReceipt = findViewById(R.id.drawerLayoutViewReceipt);
        toolbarViewReceipt = findViewById(R.id.navigationToolbarViewReceipt);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarViewReceipt);
        Objects.requireNonNull(getSupportActionBar()).setTitle("View Receipt");
        toolbarViewReceipt.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarViewReceipt.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ViewReceipts.this, drawerLayoutViewReceipt, toolbarViewReceipt, R.string.open, R.string.close);
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


        FirebaseRecyclerOptions<ReceiptModel> options =
                new FirebaseRecyclerOptions.Builder<ReceiptModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("ViewReceipt"),ReceiptModel.class)
                .build();

        receiptAdapter = new ReceiptAdapter(options);
        recyclerViewReceipt.setAdapter(receiptAdapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        receiptAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiptAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem item = menu.findItem(R.id.searchData);

        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setBackgroundColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String s) {

        FirebaseRecyclerOptions<ReceiptModel> options =
                new FirebaseRecyclerOptions.Builder<ReceiptModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("ViewReceipt").orderByChild("Name").startAt(s).endAt(s+"\uf8ff"),ReceiptModel.class)
                        .build();

        receiptAdapter = new ReceiptAdapter(options);
        receiptAdapter.startListening();
        recyclerViewReceipt.setAdapter(receiptAdapter);

    }

}