package com.example.karl.mobilefinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> reqList;
    public static ArrayList<String> acceptList;
    public static ArrayList<String> completeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = new String[] {};
        MainActivity.acceptList = new ArrayList<String>(Arrays.asList(list));
        MainActivity.completeList = new ArrayList<String>(Arrays.asList(list));


        Button startScan = (Button) findViewById(R.id.start_scan);
        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartBeaconScan.class);
                startActivityForResult(intent, 0);
            }
        });


        Button patientReq = (Button) findViewById(R.id.pat_req);
        patientReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewPatientRequest.class);
                startActivity(intent);
            }
        });


        Button completedReq = (Button) findViewById(R.id.comp_req);
        completedReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompletedRequests.class);
                startActivity(intent);
            }
        });



    }


}
