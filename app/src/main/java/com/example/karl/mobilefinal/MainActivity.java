package com.example.karl.mobilefinal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class MainActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    static String[] list = new String[] {};
    public static ArrayList<String> reqList = new ArrayList<String>(Arrays.asList(list));
    public static ArrayList<String> acceptList = new ArrayList<String>(Arrays.asList(list));
    public static ArrayList<String> completeList = new ArrayList<String>(Arrays.asList(list));
    public static String regionName = "NO REGION SELECTED";
    public Button regionButton;
    public TextView regionInfo;
    public TextView placeInfo;
    public ArrayList<String> beaconList;
    private BeaconManager mBeaconManager;

    public boolean b1, m1, w1;
    int count = 0;

    @Override
    public void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        mBeaconManager.bind(this);

        TextView titleText = (TextView) findViewById(R.id.title_with_region);
        titleText.setText(regionName);

        if(w1 || b1 || m1) {
            showNotification("Beacons Found", "Select an option below.");
        }

        if(!regionName.equals("NO REGION SELECTED")) {
            regionButton.setVisibility(View.VISIBLE);
            regionInfo.setVisibility(View.VISIBLE);
            placeInfo.setVisibility(View.INVISIBLE);
        }else{
            regionButton.setVisibility(View.INVISIBLE);
            regionInfo.setVisibility(View.INVISIBLE);
            placeInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("Local Beacons", Identifier.parse("0xedd1ebeac04e5defa017"), null, null);
        Region sRegion = new Region("Sebass", Identifier.parse("0x1234abcd1234abcd1234"), null, null);
        if(!b1 && !m1 && !w1) {
            try {
                mBeaconManager.stopRangingBeaconsInRegion(region);
                mBeaconManager.startRangingBeaconsInRegion(region);
                mBeaconManager.stopRangingBeaconsInRegion(sRegion);
                mBeaconManager.startRangingBeaconsInRegion(sRegion);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBeaconManager.setRangeNotifier(this);
        }else {
            try {
                mBeaconManager.stopRangingBeaconsInRegion(region);
                mBeaconManager.stopRangingBeaconsInRegion(sRegion);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBeaconManager.setRangeNotifier(this);
        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                Identifier instanceId = beacon.getId2();
                String area = "NONE";
                String beaconID = "NONE";

                if(!b1 && instanceId.equals(Identifier.parse("0xa4412b4109f7"))) {
                    b1 = true;
                    count++;
                    Log.d("Notification", "I see Beacon blueberry");
                    beaconID = instanceId.toString();
                    area = "B";
                    beaconList.add("Area: " + area + " || ID: " + beaconID);
                    //areaChoice("Entering Area " + area + ": ", messageReq, beaconID);
                    //showNotification("Entering Wing B: ", "Click to view current patient(s) requests.", beaconID);
                }

                /*
                if(!i1 && instanceId.equals(Identifier.parse("0xf5ac5b224426"))) {
                    Log.d("Notification", "I see Beacon ice");
                    //showNotification("Entering Wing I: ", "Click to view current patient(s) requests.");
                    i1 = true;
                    count++;
                }
                */

                if(!m1 && instanceId.equals(Identifier.parse("0xd211181d4d1c"))) {
                    m1 = true;
                    count++;
                    Log.d("Notification", "I see Beacon mint");
                    beaconID = instanceId.toString();
                    area = "M";
                    beaconList.add("Area: " + area + " || ID: " + beaconID);
                    //areaChoice("Entering Wing " + area + ": ", messageReq, beaconID);
                    //showNotification("Entering Wing M: ", "Click to view current patient(s) requests.", beaconID);
                }

                if(!w1 && instanceId.equals(Identifier.parse("0xabcd1234abcd"))) {
                    w1 = true;
                    count++;
                    Log.d("Notification", "I see Beacon white");
                    beaconID = instanceId.toString();
                    area = "W";
                    beaconList.add("Area: " + area + " || ID: " + beaconID);
                    //areaChoice("Entering Wing " + area + ": ", messageReq, beaconID);
                    //showNotification("Entering Wing W: ", "Click to view current patient(s) requests.", beaconID);
                }


                Log.d("Count:", String.valueOf(count));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = false;
        m1 = false;
        w1 = false;

        String[] list = new String[] {};
        beaconList = new ArrayList<String>(Arrays.asList(list));


        Button startScan = (Button) findViewById(R.id.start_scan);
        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AreaList.class);
                intent.putStringArrayListExtra("BL", beaconList);
                startActivity(intent);
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

        TextView titleText = (TextView) findViewById(R.id.title_with_region);
        titleText.setText(regionName);

        regionButton = (Button) findViewById(R.id.view_region_req);
        regionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartBeaconScan.class);
                intent.putExtra("ID", regionName);
                startActivity(intent);
            }
        });

        regionInfo = (TextView) findViewById(R.id.region_text);
        placeInfo = (TextView) findViewById(R.id.place_holder_region);

    }


    public void showNotification(String title, String message) {

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(count, notification);
    }

}
