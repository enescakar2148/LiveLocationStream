package com.enescakar.istexprs.System;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.enescakar.istexprs.Model.User;
import com.enescakar.istexprs.UI.AdminHomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Auth {
    private User mUser;
    private FirebaseAuth mAuth;

    public Auth(User user, FirebaseAuth auth) {
        mUser = user;
        mAuth = auth;
    }


    public boolean register(Context context) {
        if (mUser.getPass().matches(mUser.getPassAgain())){
            //register
            return true;
        }
        Toast.makeText(context, "Sifreler eslesmedi!", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean login() {
        return false;
    }


    public void adminLogin(Activity activity) {
        mAuth.signInWithEmailAndPassword(mUser.getMail().toLowerCase(Locale.ROOT), mUser.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                activity.startActivity(new Intent(activity, AdminHomeScreen.class));
                activity.finish();
                Toast.makeText(activity, "Basarili", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
