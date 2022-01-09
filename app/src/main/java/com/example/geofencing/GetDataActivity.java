package com.example.geofencing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.geofencing.Model.Prevalent;

public class GetDataActivity extends AppCompatActivity {

    private TextView tv_test01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        tv_test01 = findViewById(R.id.tv_test01);

        String currentUser = Prevalent.currentOnlineUser.getMatric();

        tv_test01.setText(currentUser);
    }
}