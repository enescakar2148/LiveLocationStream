package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.enescakar.istexprs.Model.Locations;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Adapters.AdminRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminKuryeDetailsScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {


    /*
    TODO: TARIHE GORE KONUM LISTELEME OZELLIGI EKLENMESI LAZIM SUAN SADECE TEK BIR TARIHE GORE ALIYOR
    TODO: BU YUZDEN KONUMLARIN TARIHE GORE ALINMASI SIRASINDA KOD DEGISIKLIGI OLABILIR !

     */

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String kuryeId;
    private ArrayList<String> dateList;
    private ArrayList<Locations> locations;

    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kurye_details_screen);

        dateList = new ArrayList<>();
        locations = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Locations");

        Intent intent = getIntent();
        kuryeId = intent.getStringExtra("kuryeId");
        System.out.println(kuryeId);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        getDistance();
    }


    public void getDistance() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    //Object object = child.getKey();

                    dateList.add(child.getKey());
                }

                for (String date : dateList
                ) {
                    ValueEventListener listener1 = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()
                            ) {
                                HashMap<String, Object> loc = (HashMap<String, Object>) ds.getValue();
                                locations.add(new Locations(loc.get("latitude").toString(), loc.get("longitude").toString()));
                            }
                            mapFragment.getMapAsync(AdminKuryeDetailsScreen.this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AdminKuryeDetailsScreen.this, error.getDetails(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    reference.child(kuryeId).child(date).addValueEventListener(listener1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminKuryeDetailsScreen.this, error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        };
        reference.child(kuryeId).addValueEventListener(listener);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        System.out.println(locations.size());
        PolylineOptions options = new PolylineOptions();
        for (Locations location: locations) {

            LatLng loc = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
            googleMap.addPolyline(options.clickable(true).width(2).add(loc));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(locations.get(0).getLatitude()), Double.parseDouble(locations.get(0).getLongitude())), 10));


        }

        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {

    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {

    }
}