package com.example.norbert.aion.Database;

/**
 * Created by norbert on 19.02.2017.
 */

public class EventDB {

    private int ID;
    private String
            EVENT_NAME,
            START_DATETIME,
            END_DATETIME,
            ADDRESS,
            DESC,
            TAG,
            GUEST;

    // For not finding matching event in database
    public EventDB(int ID) {
        this.ID = ID;
    }

    public EventDB(String EVENT_NAME, String START_DATETIME, String END_DATETIME, String ADDRESS,
                   String DESC, String TAG, String GUEST) {
        this.EVENT_NAME = EVENT_NAME;
        this.START_DATETIME = START_DATETIME;
        this.END_DATETIME = END_DATETIME;
        this.ADDRESS = ADDRESS;
        this.DESC = DESC;
        this.TAG = TAG;
        this.GUEST = GUEST;
    }

    public EventDB(int ID, String EVENT_NAME, String START_DATETIME, String END_DATETIME,
                   String ADDRESS, String DESC, String TAG, String GUEST) {
        this.ID = ID;
        this.EVENT_NAME = EVENT_NAME;
        this.START_DATETIME = START_DATETIME;
        this.END_DATETIME = END_DATETIME;
        this.ADDRESS = ADDRESS;
        this.DESC = DESC;
        this.TAG = TAG;
        this.GUEST = GUEST;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEVENT_NAME() {
        return EVENT_NAME;
    }

    public void setEVENT_NAME(String EVENT_NAME) {
        this.EVENT_NAME = EVENT_NAME;
    }

    public String getSTART_DATETIME() {
        return START_DATETIME;
    }

    public void setSTART_DATETIME(String START_DATETIME) {
        this.START_DATETIME = START_DATETIME;
    }

    public String getEND_DATETIME() {
        return END_DATETIME;
    }

    public void setEND_DATETIME(String END_DATETIME) {
        this.END_DATETIME = END_DATETIME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getGUEST() {
        return GUEST;
    }

    public void setGUEST(String GUEST) {
        this.GUEST = GUEST;
    }
}
