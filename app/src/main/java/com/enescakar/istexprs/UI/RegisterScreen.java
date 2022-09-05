package com.enescakar.istexprs.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.enescakar.istexprs.Model.User;
import com.enescakar.istexprs.R;
import com.enescakar.istexprs.System.Auth;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterScreen extends AppCompatActivity {

    private TextInputEditText pass, passAgain, plaka, kuryeNo, eMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        pass = findViewById(R.id.registerPassText);
        passAgain = findViewById(R.id.registerPassTextAgain);
        plaka = findViewById(R.id.registerPlaka);
        kuryeNo = findViewById(R.id.registerKuryeNo);
        eMail = findViewById(R.id.registerMailText);
    }

    public void registerToLogin(View view){
        finish();
    }

    public void registerBtn(View view){
        if (isEmptyText(eMail.getText().toString(), pass.getText().toString(), passAgain.getText().toString(), plaka.getText().toString(), kuryeNo.getText().toString())){
            Toast.makeText(this, "Lutfen Bos Alan Birakmayin", Toast.LENGTH_SHORT).show();
        } else {
            Auth auth = new Auth(new User(
                    plaka.getText().toString(),
                    eMail.getText().toString(),
                    pass.getText().toString(),
                    passAgain.getText().toString(),
                    kuryeNo.getText().toString()
            ), FirebaseAuth.getInstance());

            if (auth.register(this)){
                startActivity(new Intent(RegisterScreen.this, Home.class));
            }
        }

    }
    private boolean isEmptyText (String mail, String pass, String passAgain, String plaka, String kuryeNo){
        if (mail.matches("") || pass.matches("") || passAgain.matches("") || plaka.matches("") || kuryeNo.matches("")){
            return true;
        }
        return false;
    }
}
