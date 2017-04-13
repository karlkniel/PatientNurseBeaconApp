package com.example.karl.mobilefinal;

import java.util.ArrayList;

/**
 * Created by Karl Everett Kniel on 4/12/17.
 */
public class arrays {
    private ArrayList<String> requestList;
    private ArrayList<String> acceptList;
    private ArrayList<String> completeList;
    private ArrayList<String> beaconList;

    public void setRequestList(ArrayList<String> requestList) { this.requestList = requestList; }
    public void setacceptList(ArrayList<String> acceptList) { this.acceptList = acceptList; }
    public void setCompleteList(ArrayList<String> completeList) { this.completeList = completeList; }
    public void setBeaconList(ArrayList<String> beaconList) { this.beaconList = beaconList; }

    private static arrays ourInstance = new arrays();
    public static arrays getInstance() {
        return ourInstance;
    }

    private arrays() {
    }
}
