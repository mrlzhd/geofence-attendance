package com.example.geofencing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {



    private CardView btn_viewClass, btn_registerClass, btn_classHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_viewClass = findViewById(R.id.btn_viewClass);
        btn_registerClass = findViewById(R.id.btn_registerClass);
        btn_classHistory = findViewById(R.id.btn_classHistory);


        btn_viewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent classList = new Intent(MainActivity.this, ClassListActivity.class);
                startActivity(classList);

            }
        });

        btn_classHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoHistory = new Intent(MainActivity.this, ClassHistoryActivity.class);
                startActivity(gotoHistory);


            }
        });

        btn_registerClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerClass = new Intent (MainActivity.this, RegisterClassActivity.class);
                startActivity(registerClass);
            }
        });

    }
}