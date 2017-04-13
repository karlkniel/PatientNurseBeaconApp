package com.example.karl.mobilefinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.karl.mobilefinal.MainActivity.regionName;

public class AreaList extends AppCompatActivity {

    public ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> bList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_list);

        final ListView listView = (ListView) findViewById(R.id.beaconAreaView);

        bList = getIntent().getStringArrayListExtra("BL");

        Log.d("BEACON", Integer.toString(bList.size()));

        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, bList);
        listView.setAdapter(arrayAdapter);

        arrayAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                final int patPos = pos;

                AlertDialog.Builder builder = new AlertDialog.Builder(AreaList.this);
                builder.setMessage("Do you want to view requests from this area?");
                builder.setCancelable(true);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String clickedLine = bList.get(patPos);
                        String parts [] = clickedLine.split(" ");
                        String beaconID = parts[4];
                        regionName = parts[1];
                        dialog.cancel();

                        Intent intent = new Intent(AreaList.this, StartBeaconScan.class);
                        intent.putExtra("ID", beaconID);
                        startActivity(intent);
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
