package com.enescakar.istexprs.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Adapters.AdminRecyclerAdapter;

public class AdminKuryeDetailsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kurye_details_screen);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}