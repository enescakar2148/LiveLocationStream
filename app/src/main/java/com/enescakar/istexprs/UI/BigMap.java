package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.enescakar.istexprs.Model.Kurye;
import com.enescakar.istexprs.Model.Locations;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Adapters.AdminRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BigMap extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private ArrayList<Locations> locations;

    private FirebaseDatabase database;
    private ArrayList<String> kuryeler;

    private DatabaseReference reference, errorLogsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_map);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        reference = database.getReference("Locations");
        locations = new ArrayList<>();
        kuryeler = new ArrayList<>();


        getKuries();
        getLiveLocations();

    }


    private void getKuries() {
        kuryeler.clear();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> kuries = (HashMap<String, Object>) snapshot.getValue();

                for (Map.Entry<String, Object> kurye : kuries.entrySet()
                ) {
                    HashMap<String, Object> kuryeDetails = (HashMap<String, Object>) kurye.getValue();
                    kuryeler.add((String) kuryeDetails.get("kuryeNo"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BigMap.this, error.getDetails(), Toast.LENGTH_SHORT).show();

            }
        };
    }
    private void getLiveLocations() {
        ValueEventListener listener;
        for (String id: kuryeler) {
             listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    System.out.println(snapshot.getValue());
                    locations.clear();

                    /*
                    for (DataSnapshot ds : snapshot.getChildren()
                    ) {
                        HashMap<String, Object> loc = (HashMap<String, Object>) ds.getValue();
                        locations.add(new Locations(loc.get("latitude").toString(), loc.get("longitude").toString()));
                    }
*/
                    mapFragment.getMapAsync(BigMap.this);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(BigMap.this, error.getDetails(), Toast.LENGTH_SHORT).show();
                }
            };
            reference.child(id).addValueEventListener(listener);

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        for (Locations location: locations
             ) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()))));
        }

    }

}