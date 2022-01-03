package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_firstIC, et_firstPass;
    private Button btn_changefirst;
    private String parentDbName = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_firstIC = findViewById(R.id.et_firstIC);
        et_firstPass = findViewById(R.id.et_firstPass);
        btn_changefirst = findViewById(R.id.btn_changefirst);

        btn_changefirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });
    }

    private void proceed() {

        String matric = et_firstIC.getText().toString();
        String password = et_firstPass.getText().toString();

        if (TextUtils.isEmpty(matric)) {

            Toast.makeText(this, "Please enter Matric Number", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

        }

        else {
            allowToChangePass(matric, password);
        }


    }

    private void allowToChangePass(String matric, String password) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child(parentDbName).child(matric).exists()))
                {
                    HashMap<String, Object> studentDataMap = new HashMap<>();
                    studentDataMap.put("matric", matric);
                    studentDataMap.put("password", password);

                    rootRef.child(parentDbName).child(matric).updateChildren(studentDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "Please Login!", Toast.LENGTH_SHORT).show();

                                        Intent goLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(goLogin);
                                    }

                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Please check your network", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, ""+matric+" already exist!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Please contact admin!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}