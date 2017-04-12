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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPatientRequest extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_request);


        final ListView listView = (ListView) findViewById(R.id.patReqView);
        String[] list = new String[]{};
        //MainActivity.acceptList = new ArrayList<String>(Arrays.asList(list));
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, MainActivity.acceptList);
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
                        MainActivity.completeList.add(MainActivity.acceptList.get(patPos));
                        MainActivity.acceptList.remove(patPos);
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
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

    }

}
