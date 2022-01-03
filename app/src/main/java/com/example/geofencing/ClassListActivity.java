package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.Model.Prevalent;
import com.example.geofencing.ViewHolder.DataGetFire;
import com.example.geofencing.ViewHolder.FirebaseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<DataGetFire> arrayList;
    private FirebaseRecyclerOptions<DataGetFire> options;
    private FirebaseRecyclerAdapter<DataGetFire, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;

    String dbName = "StudentClassList";

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        recyclerView = findViewById(R.id.rc_class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        String currentUser = Prevalent.currentOnlineUser.getMatric();

        arrayList = new ArrayList<DataGetFire>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(dbName).child(currentUser);
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<DataGetFire>().setQuery(databaseReference, DataGetFire.class).build();


        adapter = new FirebaseRecyclerAdapter<DataGetFire, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull DataGetFire model) {

                holder.tv_className.setText(model.getName());
                holder.tv_classLat.setText(model.getLatitude());
                holder.tv_classlong.setText(model.getLongitude());

                holder.btn_gotoMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent location = new Intent(ClassListActivity.this, ClassListDetails.class);
                        location.putExtra("className", holder.tv_className.getText());
//                        location.putExtra("location", holder.tv_classLat.getText());
//                        location.putExtra("location2", holder.tv_classlong.getText());
                        startActivity(location);
                    }
                });

            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(ClassListActivity.this).inflate(R.layout.row, parent, false));
            }
        };

        recyclerView.setAdapter(adapter);

    }
}