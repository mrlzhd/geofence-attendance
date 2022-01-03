package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.geofencing.Model.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterClassActivity extends AppCompatActivity {

    Spinner spinner_register;
    DatabaseReference databaseReference;
    String dbName = "SubjectList";
    private Button btn_submitRegClass;

    String classSubmitName, matricDbName;
    String userClassList = "StudentClassList";

    List<String> classNames;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);

        spinner_register = findViewById(R.id.spinner_register);

        classNames = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(dbName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot:snapshot.getChildren()) {

                    String spinnerData = childSnapshot.child("name").getValue(String.class);
                    classNames.add(spinnerData);
                }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterClassActivity.this, android.R.layout.simple_spinner_item, classNames);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinner_register.setAdapter(arrayAdapter);


            }





            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        btn_submitRegClass = findViewById(R.id.btn_submitRegClass);
        
        btn_submitRegClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        
        


        
    }

    private void submitData() {

        classSubmitName = spinner_register.getSelectedItem().toString();

        sendData();



    }

    private void sendData() {
        final DatabaseReference regRef;
        regRef = FirebaseDatabase.getInstance().getReference();

        matricDbName = Prevalent.currentOnlineUser.getMatric();

        regRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot).child(userClassList).child(matricDbName).child(classSubmitName).exists())
                {
                    HashMap<String, Object> regDataMap = new HashMap<>();
                    regDataMap.put("name", classSubmitName);

                    regRef.child(userClassList).child(matricDbName).child(classSubmitName).updateChildren(regDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterClassActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                    }

                                    else
                                    {
                                        Toast.makeText(RegisterClassActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterClassActivity.this, matricDbName+ " already register to "+ classSubmitName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterClassActivity.this, "Please contact admin!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}