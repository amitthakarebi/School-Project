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

import com.amitthakare.sanskarschool.Adapter.RecyclerAdapter2;
import com.amitthakare.sanskarschool.Model.ModelList;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ListOfStudents2 extends AppCompatActivity {

    DrawerLayout drawerLayoutListStudent;
    Toolbar toolbarListStudents;

    RecyclerView recyclerView;
    RecyclerAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_students2);
        ini();
    }

    private void ini() {

        recyclerView = findViewById(R.id.studentListRecyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayoutListStudent = findViewById(R.id.drawerLayoutListStudents2);
        toolbarListStudents = findViewById(R.id.navigationToolbarListStudents2);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarListStudents);
        Objects.requireNonNull(getSupportActionBar()).setTitle("List Of Students");
        toolbarListStudents.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarListStudents.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ListOfStudents2.this,drawerLayoutListStudent,toolbarListStudents,R.string.open,R.string.close);
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("RemainingStudent").child(Variables.ListMonth).child(Variables.ListClass), ModelList.class)
                        .build();

        adapter = new RecyclerAdapter2(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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

        FirebaseRecyclerOptions<ModelList> options =
                new FirebaseRecyclerOptions.Builder<ModelList>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("RemainingStudent").child(Variables.ListMonth).child(Variables.ListClass).orderByChild("Name").startAt(s).endAt(s+"\uf8ff"), ModelList.class)
                        .build();

        adapter = new RecyclerAdapter2(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}