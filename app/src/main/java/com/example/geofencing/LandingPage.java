package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.geofencing.Model.Prevalent;
import com.example.geofencing.Model.Students;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import io.paperdb.Paper;

public class LandingPage extends AppCompatActivity {

    private Button btn_login, btn_firstlogin;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        getSupportActionBar().setTitle("School Lah");
        getSupportActionBar().hide();

        btn_login = findViewById(R.id.btn_login);
        btn_firstlogin = findViewById(R.id.btn_firstlogin);

        kProgressHUD = new KProgressHUD(this);

        Paper.init(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(LandingPage.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        btn_firstlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent first = new Intent(LandingPage.this, RegisterActivity.class);
                startActivity(first);
                finish();
            }
        });

        String userMatricKey = Paper.book().read(Prevalent.userMatricKey);
        String userPassKey = Paper.book().read(Prevalent.userPassKey);

        if (userMatricKey != "" && userPassKey != "")
        {
            if (!TextUtils.isEmpty(userMatricKey) && !TextUtils.isEmpty(userPassKey))
            {
                AllowAccess(userMatricKey, userPassKey);

                kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Verifying session")
                        .setCancellable(false)
                        .setAnimationSpeed(1)
                        .setDimAmount(0.5f)
                        .show();
            }
        }

    }

    private void AllowAccess(final String matric, final String pass) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Student").child(matric).exists())
                {
                    Students studentsData = snapshot.child("Student").child(matric).getValue(Students.class);

                    if (studentsData.getMatric().equals(matric))
                    {
                        if (studentsData.getPassword().equals(pass))
                        {
                            Intent goToHome = new Intent(LandingPage.this, MainActivity.class);
                            goToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(goToHome);
                            Prevalent.currentOnlineUser = studentsData;
                            kProgressHUD.dismiss();
                            finish();

                        }

                        else
                        {
                            kProgressHUD.dismiss();
                            Toast.makeText(LandingPage.this, "wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                {
                    Toast.makeText(LandingPage.this, "Account " + matric + " not register", Toast.LENGTH_SHORT).show();
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}