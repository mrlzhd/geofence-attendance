package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private EditText et_loginIC,et_loginPass;
    private Button btn_login2;
    private CheckBox remember;
    private KProgressHUD kProgressHUD;
    private TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        et_loginIC = findViewById(R.id.et_loginIC);
        et_loginPass = findViewById(R.id.et_loginPass);
        remember = findViewById(R.id.remember_me_chkb);
        btn_login2 = findViewById(R.id.btn_login2);
        kProgressHUD = new KProgressHUD(this);
        tv_back = findViewById(R.id.tv_back);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                startActivity(intent);
                finish();
            }
        });

        Paper.init(this);

        btn_login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IC = et_loginIC.getText().toString();
                String password = et_loginPass.getText().toString();

//                Paper.book().write(Prevalent.userIcKey, IC);
//                Paper.book().write(Prevalent.userPassKey, password);

                loginUser();
            }
        });


    }

    private void loginUser() {

        String matric = et_loginIC.getText().toString();
        String pass = et_loginPass.getText().toString();

        if (TextUtils.isEmpty(matric))
        {
            Toast.makeText(this, "Please enter matric number", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }

        else
        {
            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Verifying session")
                    .setCancellable(false)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.5f)
                    .show();

            AllowAccess(matric, pass);
        }
    }

    private void AllowAccess(final String matric, final String pass) {

        if (remember.isChecked())
        {
            Paper.book().write(Prevalent.userMatricKey, matric);
            Paper.book().write(Prevalent.userPassKey, pass);
        }

        final DatabaseReference loginRef;
        loginRef = FirebaseDatabase.getInstance().getReference();

        loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Student").child(matric).exists())
                {
                    Students studentsData = snapshot.child("Student").child(matric).getValue(Students.class);

                    if (studentsData.getMatric().equals(matric))
                    {
                        if (studentsData.getPassword().equals(pass))
                        {
                            Intent goToHome = new Intent(LoginActivity.this, MainActivity.class);
                            goToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(goToHome);
                            finish();
                        }

                        else
                        {
                            kProgressHUD.dismiss();
                            Toast.makeText(LoginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                {
                    Toast.makeText(LoginActivity.this, "Account " + matric + " not register", Toast.LENGTH_SHORT).show();
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}