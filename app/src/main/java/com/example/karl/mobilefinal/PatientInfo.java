package com.example.karl.mobilefinal;

/**
 * Created by Karl Everett Kniel on 4/11/17.
 */

public class PatientInfo {
    private String m_name;
    private String m_room;
    private String m_help;
    private int m_req;

    PatientInfo(String name, int room, String help, int req){
        m_name = name;
        m_room = Integer.toString(room);
        m_help = help;
        m_req = req;
    }

    public String get_mname(){ return m_name;}
    public String get_mroom(){ return m_room;}
    public String get_mhelp(){ return m_help;}
    public int get_mreq(){ return m_req;}
}

