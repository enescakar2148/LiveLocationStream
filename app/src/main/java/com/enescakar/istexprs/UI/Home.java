package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.enescakar.istexprs.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    private static final int GPS_TIME_INTERVAL = 1000 * 60 ; // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000; // set the distance value in meter
    private static final int HANDLER_DELAY = 1000 * 15;
    private static final int START_HANDLER_DELAY = 0;


    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;

    private FirebaseDatabase database;
    private DatabaseReference reference, errorLogsRef;
    private String uuid;
    private String date;
    private TextView dateText, kuryeNoText, plakaText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Locations");

        date = String.valueOf(LocalDate.now());

        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dateText = findViewById(R.id.date);
        kuryeNoText = findViewById(R.id.kuryeNo);
        plakaText = findViewById(R.id.plaka);

        dateText.setText(date);

        DatabaseReference reference = database.getReference("Kuryeler");
        reference.child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                    kuryeNoText.setText(map.get("kuryeNo") + " Numarali Kurye");
                    plakaText.setText(String.valueOf(map.get("plaka")));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                errorLogsRef = database.getReference("Error_Logs");
                HashMap<String, Object> map = new HashMap<>();
                map.put("ErrorCode", 98);
                map.put("ErrorSource", "Home Class");
                map.put("ErrorTitle", "Kurye Bilgileri Alinmasi Sirasinda Hata");
                map.put("ErrorDetailsFromDeveloper", "Kurye anasayfasinda gosterilmek uzere kurye bilgileri alinmasi sirasinda hata cikti");
                map.put("ErrorDetailsFromFirebase", error.getDetails());
                map.put("HataAlanKuryeMail", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                errorLogsRef.child(uuid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("error98", "basarili");
                        } else {
                            Toast.makeText(Home.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                requestLocation();
                handler.postDelayed(this, HANDLER_DELAY);
            }
        }, START_HANDLER_DELAY);
    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {
        System.out.println(provider);

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        System.out.println(provider);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println(status);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println(location.getLatitude());
        Toast.makeText(Home.this, "Konum Basariyla Alindi...", Toast.LENGTH_SHORT).show();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        String recordTimestapm = String.valueOf(Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())).getTime());

        reference.child(uuid).child("currentLocation").setValue(latLng).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                    System.out.println(task.getException().getLocalizedMessage());
                    Toast.makeText(Home.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        reference.child(uuid).child(date).child(recordTimestapm).setValue(latLng)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ;
                        } else {
                            System.out.println(task.getException().getLocalizedMessage());
                            Toast.makeText(Home.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        locationManager.removeUpdates(Home.this);
    }

    private void requestLocation() {
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    requestLocation();
                    handler.postDelayed(this, HANDLER_DELAY);
                }
            }, START_HANDLER_DELAY);
        } else {
            finish();
        }

    }

    public void kuryeLogOut(View view) {
        Toast.makeText(this, "Cikis Yapildi...", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Home.this, LoginScreen.class));
        finish();
    }
}
