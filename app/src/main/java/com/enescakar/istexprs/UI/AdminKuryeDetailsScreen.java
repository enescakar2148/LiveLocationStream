package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.enescakar.istexprs.Model.Locations;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Adapters.KuryeLocationCardRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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

public class AdminKuryeDetailsScreen extends AppCompatActivity{


    private FirebaseDatabase database;
    private DatabaseReference reference, errorLogsRef;
    private String kuryeId;
    private ArrayList<String> dateList;

    private RecyclerView recyclerView;
    private KuryeLocationCardRecyclerAdapter kuryeLocationCardRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kurye_details_screen);

        dateList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Locations");

        Intent intent = getIntent();
        kuryeId = intent.getStringExtra("kuryeId");


        recyclerView = findViewById(R.id.kuryeLocationDateRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        getDates();

    }


    public void getDates() {
        ValueEventListener listener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    if (!child.getKey().matches("currentLocation")){


                        dateList.add(child.getKey());
                    }
                }

                kuryeLocationCardRecyclerAdapter = new KuryeLocationCardRecyclerAdapter(AdminKuryeDetailsScreen.this, dateList, kuryeId);

                recyclerView.setAdapter(kuryeLocationCardRecyclerAdapter);
                kuryeLocationCardRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLogsRef = database.getReference("Error_Logs");
                HashMap<String, Object> map = new HashMap<>();
                map.put("ErrorCode", 95);
                map.put("ErrorSource", "AdminKuryeDetails Class");
                map.put("ErrorTitle", "Faaliyet Tarihlerinin alinmasi Sirasinda Hata");
                map.put("ErrorDetailsFromDeveloper", "Kuryenin Faaliyet Tarihlerinin Alinmasinda Hata Alindi");
                map.put("ErrorDetailsFromFirebase", error.getDetails());
                map.put("HataAlanKuryeMail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                errorLogsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("error95", "basarili");
                        } else {
                            Toast.makeText(AdminKuryeDetailsScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(AdminKuryeDetailsScreen.this, error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        };
        reference.child(kuryeId).addValueEventListener(listener);
    }
}