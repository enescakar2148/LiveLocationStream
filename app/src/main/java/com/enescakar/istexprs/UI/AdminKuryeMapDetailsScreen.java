package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.enescakar.istexprs.Model.Locations;
import com.enescakar.istexprs.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminKuryeMapDetailsScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {


    private ArrayList<Locations> locations;

    private SupportMapFragment mapFragment;

    private FirebaseDatabase database;
    private DatabaseReference reference, errorLogsRef;
    private String date, kuryeId;
    private double distance = 0;
    private TextView totalDistance, loadingHarita;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kurye_map_details_screen);

        database = FirebaseDatabase.getInstance();
        locations = new ArrayList<>();
        reference = database.getReference("Locations");
        totalDistance = findViewById(R.id.totalKmText);
        loadingHarita = findViewById(R.id.loadingHarita);
        progressBar = findViewById(R.id.progressBar);

        loadingHarita.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        findViewById(R.id.map).setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        kuryeId = intent.getStringExtra("kuryeId");

        getDistance();
    }

    private void getDistance() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                locations.clear();

                for (DataSnapshot ds : snapshot.getChildren()
                ) {
                    HashMap<String, Object> loc = (HashMap<String, Object>) ds.getValue();
                    locations.add(new Locations(Double.parseDouble(loc.get("latitude").toString()), Double.parseDouble(loc.get("longitude").toString())));
                }

                for (int i =0; i<locations.size(); i++) {
                    if (locations.size()<=i+1){
                        break;
                    } else {
                        LatLng pointA = new LatLng(Double.parseDouble(locations.get(i).getLatitude()), Double.parseDouble(locations.get(i).getLongitude()));
                        LatLng pointB = new LatLng(Double.parseDouble(locations.get(i+1).getLatitude()), Double.parseDouble(locations.get(i+1).getLongitude()));
                        distance = distance + SphericalUtil.computeDistanceBetween(pointA, pointB);
                    }
                }
                totalDistance.setText("Toplam Km : " + Math.round((distance) /1000));
                mapFragment.getMapAsync(AdminKuryeMapDetailsScreen.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                errorLogsRef = database.getReference("Error_Logs");
                HashMap<String, Object> map = new HashMap<>();
                map.put("ErrorCode", 114);
                map.put("ErrorSource", "AdminKuryeDetails Class");
                map.put("ErrorTitle", "Lokasyonlarin Alinmasi Sirasinda Hata");
                map.put("ErrorDetailsFromDeveloper", "Kuryenin gittigi lokasyonlarin alinmasi sirasinda hata alindi");
                map.put("ErrorDetailsFromFirebase", error.getDetails());
                map.put("HataAlanKuryeMail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                errorLogsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("error114", "basarili");
                        } else {
                            Toast.makeText(AdminKuryeMapDetailsScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(AdminKuryeMapDetailsScreen.this, error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        };
        reference.child(kuryeId).child(date).addValueEventListener(listener);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        PolylineOptions options = new PolylineOptions();
        for (Locations location : locations) {

            LatLng loc = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
            googleMap.addPolyline(options.clickable(true).width(2).add(loc));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(locations.get(0).getLatitude()), Double.parseDouble(locations.get(0).getLongitude())), 10));

        }
        findViewById(R.id.map).setVisibility(View.VISIBLE);

        loadingHarita.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
        googleMap.addMarker(new MarkerOptions().title("Baslangic Konumu").position(new LatLng(Double.parseDouble(locations.get(0).getLatitude()), Double.parseDouble(locations.get(0).getLongitude()))));
        googleMap.addMarker(new MarkerOptions().title("En Son Konum").position(new LatLng(Double.parseDouble(locations.get(locations.size()-1).getLatitude()), Double.parseDouble(locations.get(locations.size()-1).getLongitude()))));

    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {

    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {

    }

}