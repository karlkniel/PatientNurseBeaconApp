package com.example.karl.mobilefinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewPatientRequest extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    String pName;
    String pHelp;
    int pRoom;
    int pReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_request);

        /*
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PatientView(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        */

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        ListView patientView = (ListView) findViewById(R.id.patReqView);

        ArrayList results = new ArrayList<PatientInfo>();
        PatientInfo object = new PatientInfo(pName, pRoom, pHelp, pReq);

        int count = 0;
        while(count < pReq) {
            results.add(pReq, object);
        }


        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String s = bundle.getString("A");
            adapter.add(s);
            Log.d("LIST", s);
        }

    }

    /*
    private ArrayList<PatientInfo> getDataSet() {
        ArrayList results = new ArrayList<PatientInfo>();
        for (int index = 0; index < 20; index++) {
            PatientInfo obj = new PatientInfo("Name " + index, index, "Reason " + index);
            results.add(index, obj);
        }
        return results;
    }
    */

}
