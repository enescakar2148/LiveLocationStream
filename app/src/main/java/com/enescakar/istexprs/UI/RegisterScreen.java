package com.enescakar.istexprs.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.enescakar.istexprs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class RegisterScreen extends AppCompatActivity {

    private TextInputEditText isim, soyIsim, pass, passAgain, plaka, kuryeNo, eMail, tc, motor, adress, motorRenk, telefon, ikincilTelefon, ehliyetNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        pass = findViewById(R.id.registerPassText);
        passAgain = findViewById(R.id.registerPassTextAgain);
        plaka = findViewById(R.id.registerPlaka);
        kuryeNo = findViewById(R.id.registerKuryeNo);
        eMail = findViewById(R.id.registerMailText);
        tc = findViewById(R.id.tc);
        motor = findViewById(R.id.motorMarkaModel);
        motorRenk = findViewById(R.id.motorRenk);
        adress = findViewById(R.id.adress);
        telefon = findViewById(R.id.phone);
        ikincilTelefon = findViewById(R.id.phone2);
        ehliyetNo = findViewById(R.id.ehliyetNo);
        isim = findViewById(R.id.isim);
        soyIsim = findViewById(R.id.soyIsim);


    }

    public void registerToLogin(View view){
        finish();
    }

    public void registerBtn(View view){
        if (isEmptyText(isim.getText().toString(),soyIsim.getText().toString(), ehliyetNo.getText().toString(), adress.getText().toString(), telefon.getText().toString(), ikincilTelefon.getText().toString(), motorRenk.getText().toString(), eMail.getText().toString(), motor.getText().toString(), tc.getText().toString()
                , pass.getText().toString(), passAgain.getText().toString(), plaka.getText().toString(), kuryeNo.getText().toString())){
            Toast.makeText(this, "Lutfen Bos Alan Birakmayin", Toast.LENGTH_SHORT).show();
        } else {
             FirebaseAuth auth = FirebaseAuth.getInstance();
             auth.createUserWithEmailAndPassword(eMail.getText().toString().toLowerCase(Locale.ROOT), pass.getText().toString())
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()){
                                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                                 DatabaseReference reference = database.getReference("Kuryeler");

                                 HashMap<String, Object> map = new HashMap<>();
                                 map.put("mail", eMail.getText().toString());
                                 map.put("pass", pass.getText().toString());
                                 map.put("plaka", plaka.getText().toString().toUpperCase());
                                 map.put("motorMarkaModel", motor.getText().toString());
                                 map.put("tc", tc.getText().toString());
                                 map.put("kuryeNo", kuryeNo.getText().toString());
                                 map.put("telefon", telefon.getText().toString());
                                 map.put("ikincilTelefon", ikincilTelefon.getText().toString());
                                 map.put("motorRenk", motorRenk.getText().toString());
                                 map.put("adress", adress.getText().toString());
                                 map.put("surucuBelgesiNo", ehliyetNo.getText().toString());
                                 map.put("isim", isim.getText().toString());
                                 map.put("soyIsim", soyIsim.getText().toString());

                                 map.put("kuryeId", auth.getCurrentUser().getUid());


                                 reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map)
                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if (task.isSuccessful()){
                                                     startActivity(new Intent(RegisterScreen.this, Home.class));
                                                     finish();
                                                 }
                                             }
                                         });
                             } else {
                                 Toast.makeText(RegisterScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                             }
                         }
                     });


        }

    }
    private boolean isEmptyText (String isim, String soyIsim, String ehliyetNo, String mail, String pass, String tc, String motor, String passAgain, String plaka, String kuryeNo, String adress, String phone, String phone2, String motorRenk){
        if (isim.matches("") || soyIsim.matches("") || ehliyetNo.matches("") || adress.matches("") || phone.matches("") || phone2.matches("") || motorRenk.matches("") || motor.matches("") || tc.matches("") || mail.matches("") || pass.matches("") || passAgain.matches("") || plaka.matches("") || kuryeNo.matches("")){
            return true;
        }
        return false;
    }
}
