package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.enescakar.istexprs.Model.Kurye;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Adapters.AdminRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminHomeScreen extends AppCompatActivity {


    private DatabaseReference reference, errorLogsRef;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    private ArrayList<Kurye> kuryeler;

    private AdminRecyclerAdapter adminRecyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);


        kuryeler = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Kuryeler");

        recyclerView = findViewById(R.id.adminRecyclerView);

        getKuries();

    }


    private void getKuries() {
        kuryeler.clear();
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> kuries = (HashMap<String, Object>) snapshot.getValue();

                for (Map.Entry<String, Object> kurye : kuries.entrySet()
                ) {
                    HashMap<String, Object> kuryeDetails = (HashMap<String, Object>) kurye.getValue();
                    kuryeler.add(
                            new Kurye(kuryeDetails.get("kuryeNo").toString(), kuryeDetails.get("mail").toString(), kuryeDetails.get("plaka").toString(), kuryeDetails.get("pass").toString(), kuryeDetails.get("kuryeId").toString())
                    );
                }

                adminRecyclerAdapter = new AdminRecyclerAdapter(AdminHomeScreen.this, kuryeler);

                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeScreen.this));
                recyclerView.setAdapter(adminRecyclerAdapter);
                adminRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLogsRef = database.getReference("Error_Logs");
                HashMap<String, Object> map = new HashMap<>();
                map.put("ErrorCode", 79);
                map.put("ErrorSource", "AdminHomeScreen Class");
                map.put("ErrorTitle", "Kurye Listesi Alinmasi Sirasinda Hata");
                map.put("ErrorDetailsFromDeveloper", "Admin Panelinde kuryelerin listelenmesi icin bilgiler cekilirken hata");
                map.put("ErrorDetailsFromFirebase", error.getDetails());
                map.put("HataAlanKuryeMail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                errorLogsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("error79", "basarili");
                        } else {
                            Toast.makeText(AdminHomeScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        reference.addValueEventListener(eventListener);
    }
}