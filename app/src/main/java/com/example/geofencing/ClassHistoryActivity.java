package com.example.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.geofencing.Model.Prevalent;
import com.example.geofencing.ViewHolder.DataGetFire;
import com.example.geofencing.ViewHolder.FirebaseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClassHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<DataGetFire> arrayList;
    private FirebaseRecyclerOptions<DataGetFire> options;
    private FirebaseRecyclerAdapter<DataGetFire, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;

    String dbName = "History";

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
        setContentView(R.layout.activity_class_history);

        getSupportActionBar().setTitle("History");

        recyclerView = findViewById(R.id.rc_history);
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

                holder.tv_hisotryclassName.setText(model.getClassName());
                holder.tv_hisotryDate.setText(model.getDate());
                holder.tv_hisotryTime.setText(model.getTime());

            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(ClassHistoryActivity.this).inflate(R.layout.row_history, parent, false));
            }
        };

        recyclerView.setAdapter(adapter);
    }
}