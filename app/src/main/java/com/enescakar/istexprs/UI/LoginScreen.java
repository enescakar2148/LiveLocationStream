package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.enescakar.istexprs.Model.User;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginScreen extends AppCompatActivity {
    private final String adminUserName = "fatihistexp@mail.com";
    private final String adminPass = "fatih1234fa";

    private TextInputEditText mailText;
    private TextInputEditText passText;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mailText = findViewById(R.id.loginMailText);
        passText = findViewById(R.id.loginPassText);

        mAuth = FirebaseAuth.getInstance();
    }


    public void loginBtn(View v) {
        if (!mailText.getText().toString().matches("") ||
                !passText.getText().toString().matches("") &&
                        mailText.getText().toString().matches(adminUserName) &&
                        passText.getText().toString().matches(adminPass)) {

            mAuth.signInWithEmailAndPassword(mailText.getText().toString().toLowerCase(Locale.ROOT), passText.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(LoginScreen.this, AdminHomeScreen.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginScreen.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (mailText.getText().toString().matches("") ||
                passText.getText().toString().matches("")) {

        } else {
            Toast.makeText(this, "Lutfen alanlarin bos olmadigindan veya kullanici adi/sifresinin dogrulugundan emin olun!", Toast.LENGTH_LONG).show();
        }
    }

    public void loginToRegisterPage(View v) {
        startActivity(new Intent(this, RegisterScreen.class));
    }
}