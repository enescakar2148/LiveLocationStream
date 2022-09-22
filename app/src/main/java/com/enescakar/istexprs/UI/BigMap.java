package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.BitmapFactory;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    private FirebaseDatabase database;
    private ArrayList<Kurye> kuryeler;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_map);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.bigMapFragment);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Locations");
        kuryeler = new ArrayList<>();


        getKuries();

    }


    private void getKuries() {
        kuryeler.clear();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> kuries = (HashMap<String, Object>) snapshot.getValue();

                kuryeler.clear();
                for (Map.Entry<String, Object> kurye : kuries.entrySet()
                ) {
                    HashMap<String, Object> kuryeDetails = (HashMap<String, Object>) kurye.getValue();

                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {

                                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                                if (map.get("currentLocation") != null){
                                    HashMap<String, Object> map2 = (HashMap<String, Object>) map.get("currentLocation");
                                    LatLng latLng = new LatLng((Double) map2.get("latitude"), (Double) map2.get("longitude"));
                                    kuryeler.add(new Kurye((String) kuryeDetails.get("kuryeNo"), (String) kuryeDetails.get("mail"), (String) kuryeDetails.get("plaka"), (String) kuryeDetails.get("pass"), (String) kuryeDetails.get("kuryeId"), latLng, kuryeDetails.get("isim").toString(), kuryeDetails.get("soyIsim").toString()));

                                } else{

                                }
                            }
                            mapFragment.getMapAsync(BigMap.this);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(BigMap.this, error.getDetails(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    reference.child(kuryeDetails.get("kuryeId").toString()).addValueEventListener(listener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BigMap.this, error.getDetails(), Toast.LENGTH_SHORT).show();

            }
        };
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kuryeler");
        reference.addValueEventListener(eventListener);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        for (Kurye kurye: kuryeler
             ) {
            googleMap.addMarker(new MarkerOptions().title(kurye.getIsim() + " " + kurye.getSoyIsim() + " (" + kurye.getKuryeNo() + ")").position(new LatLng(kurye.getCurrentLocation().latitude, kurye.getCurrentLocation().longitude))).showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(kurye.getCurrentLocation().latitude, kurye.getCurrentLocation().longitude), 6));
        }
    }

}