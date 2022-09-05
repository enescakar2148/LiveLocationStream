package com.enescakar.istexprs.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.enescakar.istexprs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {

            if (mUser.getEmail().matches("fatihistexp@mail.com")) {
                new CountDownTimer(1000, 3000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                        startActivity(new Intent(SplashScreen.this, AdminHomeScreen.class));
                        finish();
                    }
                }.start();
            } else {
                new CountDownTimer(1000, 3000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                        startActivity(new Intent(SplashScreen.this, Home.class));
                        finish();
                    }
                }.start();
            }

        } else {
            new CountDownTimer(1000, 3000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class
                    ));
                    finish();
                }
            }.start();
        }

    }
}