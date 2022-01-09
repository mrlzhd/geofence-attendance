package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geofencing.Model.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassListDetails extends AppCompatActivity {

    private TextView tv_classNameDetails, tv_latDetails, tv_longDetails, tv_statusDetails;
    private Button btn_gotoMapDetails;
    private DatabaseReference getReff;
    String parentDbName = "SubjectList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list_details);


        tv_classNameDetails = findViewById(R.id.tv_classNameDetails);
        tv_latDetails = findViewById(R.id.tv_latDetails);
        tv_longDetails = findViewById(R.id.tv_longDetails);
        btn_gotoMapDetails = findViewById(R.id.btn_gotoMapDetails);

        btn_gotoMapDetails.setVisibility(View.INVISIBLE);


        String currentUser = Prevalent.currentOnlineUser.getMatric();



        tv_statusDetails = findViewById(R.id.tv_statusDetails);

        String getName = getIntent().getStringExtra("className");

        tv_classNameDetails.setText(getName);

        getReff = FirebaseDatabase.getInstance().getReference(parentDbName).child(getName);
        getReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("latitude");

                if (snapshot.hasChild("latitude"))
                {
                    btn_gotoMapDetails.setVisibility(View.VISIBLE);

                    String latitude = snapshot.child("latitude").getValue().toString();
                    String longitude = snapshot.child("longitude").getValue().toString();

                    tv_longDetails.setText(longitude);

                    tv_latDetails.setText(latitude);

                    tv_statusDetails.setText("Class Ongoing");

                    btn_gotoMapDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent location = new Intent(ClassListDetails.this, MapsActivity.class);
                            location.putExtra("location", latitude);
                            location.putExtra("location2", longitude);
                            location.putExtra("className", getName);
                            startActivity(location);
                        }
                    });


                }
                else
                {
                    btn_gotoMapDetails.setVisibility(View.INVISIBLE);

                    tv_statusDetails.setText("No data available for class");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}