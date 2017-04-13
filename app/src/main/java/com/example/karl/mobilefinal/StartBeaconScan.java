package com.example.karl.mobilefinal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.example.karl.mobilefinal.MainActivity.reqList;

public class StartBeaconScan extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private MessageListener mMessageListener;

    public ArrayAdapter<String> arrayAdapter;
    boolean messageExists = false;

    private com.google.android.gms.nearby.messages.Message mActiveMessage;

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

        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, reqList);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                final int patPos = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(StartBeaconScan.this);
                builder.setMessage("Do you want to accept this request?");
                builder.setCancelable(true);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.acceptList.add(reqList.get(patPos));
                        reqList.remove(patPos);
                        arrayAdapter.notifyDataSetChanged();
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

        Button home = (Button) findViewById(R.id.hhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(StartBeaconScan.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button reload = (Button) findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqList.clear();
                recreate();
            }
        });
    }


    private void subscribe() {
        Log.i("TAG", "Subscribing.");
        String typeAttachment = "";

        String beaconID = getIntent().getExtras().getString("ID");

        Log.d("BEACON", beaconID);

        if(beaconID.equals("0xabcd1234abcd") || beaconID.equals("W")) {
            typeAttachment = "111";
        }else if(beaconID.equals("0xd211181d4d1c") || beaconID.equals("M")) {
            typeAttachment = "mint";
        }else if(beaconID.equals("0xa4412b4109f7") || beaconID.equals("B")) {
            typeAttachment = "blue";
            Log.d("BLUE", beaconID);
        }else if(beaconID.equals("LOCAL")) {

            Log.d("LOCAL", beaconID);
        }else {
            showNotification("ALERT: ", "No beacons found in current area.");
        }

        MessageFilter messageFilter = new MessageFilter.Builder()
                //.includeNamespacedType("healthcarepn-164116", typeAttachment)
                .includeNamespacedType("health-care-app-164116", typeAttachment)
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
                    String [] parts = messageAsString.split("!");
                    String roomNumber = parts[0];
                    String requestMessage = parts[1];

                    if(parts[1].equals("EMERGENCY")) {
                        showEmergencyNotification("Room " + parts[0], parts[1]);
                    }

                    Log.d("FMS", "Found Message: " + messageAsString);

                    reqList.add("Room " + roomNumber + ": " + requestMessage);
                    arrayAdapter.notifyDataSetChanged();
                }

                if("health-care-app-164116".equals(patNameSpace) && "111".equals(patType)) {
                    String messageAsString = new String(message.getContent());
                    String [] parts = messageAsString.split("!");
                    String roomNumber = parts[0];
                    String requestMessage = parts[1];

                    if(parts[1].equals("EMERGENCY")) {
                        showEmergencyNotification("Room " + parts[0], parts[1]);
                    }

                    Log.d("BEACON", "Found Message: " + messageAsString);



                    if(!reqList.isEmpty()) {
                        for(int i = 0; i <  reqList.size(); i++) {
                            String [] tempParts = reqList.get(i).split(": ");
                            if(requestMessage.equals(tempParts[1])) {
                                if(roomNumber.equals(tempParts[0])) {
                                    messageExists = true;
                                    break;
                                }

                            }
                        }
                    }

                    if(!messageExists) {
                        reqList.add("Room " + roomNumber + ": " + requestMessage);
                    }

                    messageExists = false;

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
        //unpublish();
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

        notificationManager.notify(1, notification);
    }

    public void showEmergencyNotification(String title, String message) {

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "Connection Failed");
    }


}
