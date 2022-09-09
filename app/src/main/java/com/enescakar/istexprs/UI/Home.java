package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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

public class Home extends AppCompatActivity implements LocationListener{
    LocationManager locationManager;
    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 5; // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000; // set the distance value in meter
    private static final int HANDLER_DELAY = 1000  * 3;
    private static final int START_HANDLER_DELAY = 0;


    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;
    Handler handler = new Handler();

    FirebaseDatabase database;
    DatabaseReference reference;
    String uuid;
    String date;
    private Button button;

    boolean handlerIsStart = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Locations");
        button = findViewById(R.id.button);

        date = String.valueOf(LocalDate.now());

        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //getDistance();


    }

    public void kuryeStartStop(View view) {

        if (handlerIsStart) {
            handler.removeCallbacksAndMessages(null);
            handlerIsStart = !handlerIsStart;
            button.setText("Start");
        } else {
            handler.postDelayed(new Runnable() {
                public void run() {
                    requestLocation();
                    handler.postDelayed(this, HANDLER_DELAY);
                }
            }, START_HANDLER_DELAY);
            handlerIsStart = !handlerIsStart;
            button.setText("Stop");

        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(Home.this, "Got Coordinates: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        String recordTimestapm = String.valueOf(Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())).getTime());

        reference.child(uuid).child(date).child(recordTimestapm).setValue(latLng)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Recording");
                        } else {
                            System.out.println(task.getException().getLocalizedMessage());
                            Toast.makeText(Home.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // System.out.println(location.getLatitude());
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

            if (handlerIsStart) {
                handler.removeCallbacksAndMessages(null);
                handlerIsStart = !handlerIsStart;
                button.setText("Start");


            } else {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        requestLocation();
                        handler.postDelayed(this, HANDLER_DELAY);
                    }
                }, START_HANDLER_DELAY);
                handlerIsStart = !handlerIsStart;
                button.setText("Stop");
            }
        } else {
            finish();
        }

    }
}
