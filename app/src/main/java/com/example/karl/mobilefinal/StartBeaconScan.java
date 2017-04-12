package com.example.karl.mobilefinal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessageFilter;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import android.widget.AdapterView.OnItemClickListener;

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

public class StartBeaconScan extends AppCompatActivity implements BeaconConsumer, RangeNotifier, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private BeaconManager mBeaconManager;
    private GoogleApiClient mGoogleApiClient;
    private MessageListener mMessageListener;

    public ArrayAdapter<String> arrayAdapter;

    private com.google.android.gms.nearby.messages.Message mActiveMessage;
    public boolean b1, i1, m1;
    int count = 0;
    int messageCount = 0;

    @Override
    public void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        mBeaconManager.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("Local Beacons", Identifier.parse("0xedd1ebeac04e5defa017"), null, null);
        if(!b1 && !i1 && !m1) {
            try {
                mBeaconManager.stopRangingBeaconsInRegion(region);
                mBeaconManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBeaconManager.setRangeNotifier(this);
        }else {
            try {
                mBeaconManager.stopRangingBeaconsInRegion(region);

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

                if(!b1 && instanceId.equals(Identifier.parse("0xa4412b4109f7"))) {
                    Log.d("Notification", "I see Beacon blueberry");
                    showNotification("Entering Wing B: ", "Click to view current patient(s) requests.");
                    b1 = true;
                    count++;
                }
                if(!i1 && instanceId.equals(Identifier.parse("0xf5ac5b224426"))) {
                    Log.d("Notification", "I see Beacon ice");
                    showNotification("Entering Wing I: ", "Click to view current patient(s) requests.");
                    i1 = true;
                    count++;
                }
                if(!m1 && instanceId.equals(Identifier.parse("0xd211181d4d1c"))) {
                    Log.d("Notification", "I see Beacon mint");
                    showNotification("Entering Wing M: ", "Click to view current patient(s) requests.");
                    m1 = true;
                    count++;
                }

                Log.d("Count:", String.valueOf(count));
            }
        }
    }

    //healthcarepn-164116
    //key: AIzaSyBmpulDwsyv55GyEi9hGsFgWLOuy16MLqM
    //OAuth Key: 113749439321-38oml7jd4jt41dmt03b56gtthtrlns7i.apps.googleusercontent.com

    //SEEBASS
    //health-care-app-164116
    //key: AIzaSyDRBUtFLJM3Q9TDfl-u2O2IwpMXZOv59l4



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_beacon_scan);


        final ListView listView = (ListView) findViewById(R.id.beaconReqView);
        String[] list = new String[] {};
        MainActivity.reqList = new ArrayList<String>(Arrays.asList(list));

        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, MainActivity.reqList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                final int patPos = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(StartBeaconScan.this);
                builder.setMessage("Do you want to accept this request?");
                builder.setCancelable(true);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.acceptList.add(MainActivity.reqList.get(patPos));
                        dialog.cancel();
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .build();

    }

    private void subscribe() {
        Log.i("TAG", "Subscribing.");

        MessageFilter messageFilter = new MessageFilter.Builder()
                //.includeNamespacedType("healthcarepn-164116", "string")
                .includeNamespacedType("health-care-app-164116", "111")
                .build();

        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .setFilter(messageFilter)
                .build();

        mMessageListener = new MessageListener() {

            public void onFound(com.google.android.gms.nearby.messages.Message message) {
                String patNameSpace = message.getNamespace();
                String patType = message.getType();

                if("healthcarepn-164116".equals(patNameSpace) && "string".equals(patType)) {
                    String messageAsString = new String(message.getContent());
                    Log.d("FMS", "Found Message: " + messageAsString);
                    messageCount++;
                    String counter = Integer.toString(messageCount);
                    PatientInfo object = new PatientInfo("A", 1, messageAsString, messageCount);
                    MainActivity.reqList.add("Room " + patType + ": " + messageAsString);
                    arrayAdapter.notifyDataSetChanged();
                }

                if("health-care-app-164116".equals(patNameSpace) && "111".equals(patType)) {
                    String messageAsString = new String(message.getContent());
                    Log.d("FMS", "Found Message: " + messageAsString);
                    messageCount++;
                    String counter = Integer.toString(messageCount);
                    PatientInfo object = new PatientInfo("A", 1, messageAsString, messageCount);
                    MainActivity.reqList.add("Room " + patType + ": " + messageAsString);
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            public void onLost(com.google.android.gms.nearby.messages.Message message) {
                String messageAsString = new String(message.getContent());
                Log.d("TAG", "Lost sight of message: " + messageAsString);
            }
        };
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options);
    }

    private void unsubscribe() {
        Log.i("TAG", "Unsubscribing.");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }

    private void publish(String message) {
        Log.i("TAG", "Publishing message: " + message);
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();

        mActiveMessage = new com.google.android.gms.nearby.messages.Message(message.getBytes());
        Nearby.Messages.publish(mGoogleApiClient, mActiveMessage, options);
    }

    private void unpublish() {
        Log.i("TAG", "Unpublishing.");
        if (mActiveMessage != null) {
            Nearby.Messages.unpublish(mGoogleApiClient, mActiveMessage);
            mActiveMessage = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("CONNECT", "i guess");

        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void showNotification(String title, String message) {
        Intent addActive = new Intent(this, ViewPatientRequest.class);
        addActive.putExtra("NotClicked", true);
        PendingIntent addActivePendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] {addActive}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(addActivePendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(count, notification);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "Connection Failed");
    }


}
