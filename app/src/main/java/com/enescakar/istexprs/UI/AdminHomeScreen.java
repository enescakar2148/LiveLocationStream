package com.enescakar.istexprs.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Adapters.AdminRecyclerAdapter;

public class AdminHomeScreen extends AppCompatActivity {

    private AdminRecyclerAdapter adminRecyclerAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);


        recyclerView = findViewById(R.id.adminRecyclerView);
        adminRecyclerAdapter = new AdminRecyclerAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adminRecyclerAdapter);

    }
}