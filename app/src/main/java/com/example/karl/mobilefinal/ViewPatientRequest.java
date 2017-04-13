package com.example.karl.mobilefinal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.karl.mobilefinal.MainActivity.acceptList;

public class ViewPatientRequest extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_request);

        final ListView listView = (ListView) findViewById(R.id.patReqView);

        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, acceptList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                final int patPos = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientRequest.this);
                builder.setMessage("Do you want to mark this request as completed?");
                builder.setCancelable(true);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.completeList.add(acceptList.get(patPos));
                        acceptList.remove(patPos);
                        dialog.cancel();
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        Button home = (Button) findViewById(R.id.home_s);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatientRequest.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button areaR = (Button) findViewById(R.id.region_requests);
        areaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatientRequest.this, StartBeaconScan.class);
                intent.putExtra("ID", MainActivity.regionName);
                startActivity(intent);
            }
        });
    }

}
