package com.example.norbert.aion;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.norbert.aion.Database.DatabaseOperations;
import com.example.norbert.aion.Database.EventDB;
import com.example.norbert.aion.Database.EventRepository;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CallReceiver extends BroadcastReceiver {

    static boolean isRinging = false;
    static boolean isReceived = false;
    static String callerPhoneNumber;
    static String TITLE = "*Ta wiadomość została wygenerowana automatycznie:\n";
    String INSIDE_MESSAGE ;
    String callerPhoneName;
    private boolean wasSended = false;
    Event event;


    HashMap<String,String> eventsList;
    private String END_MESSAGE = "Postaram się oddzwonić za niedługo .";

    //Database
    private DatabaseOperations databaseOperations;
    private EventRepository eventRepository;
    private ArrayList<EventDB> eventDBs;
    private EventDB eventDB;
    private DateTime NOW= new DateTime(System.currentTimeMillis());

    @Override
    public void onReceive(Context mContext, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String action = intent.getAction();
        databaseOperations = new DatabaseOperations(mContext);
        eventRepository = new EventRepository();
        eventDBs = eventRepository.getArrayList(databaseOperations);

            try{
                eventDB = getEvent();
                INSIDE_MESSAGE = "Jestem teraz na " + eventDB.getEVENT_NAME() + "\n";
                Log.e("CallRec",INSIDE_MESSAGE);
            }catch (Exception e){
                Log.e("CallRec",e.getMessage());
                Toast.makeText(mContext, "No event on now", Toast.LENGTH_LONG).show();}





        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            isRinging = true;
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            isReceived = true;
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // detect missed call
            if (isRinging == true && isReceived == false && !wasSended && !INSIDE_MESSAGE.isEmpty()) {
                Toast.makeText(mContext, "Nie odebrano : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                final SmsManager smsManager = SmsManager.getDefault();
                Toast.makeText(mContext, INSIDE_MESSAGE, Toast.LENGTH_LONG).show();
                smsManager.sendTextMessage(callerPhoneNumber, null, TITLE + INSIDE_MESSAGE + END_MESSAGE , null, null);
                wasSended = true;
            }
        }
    }

    private EventDB getEvent(){
        long start,end;
        for(EventDB e:eventDBs){
            try {
                start = Long.parseLong(e.getSTART_DATETIME());
                end = Long.parseLong(e.getEND_DATETIME());
                if((NOW.getValue() > start) && (NOW.getValue() < end)){
                    Log.e("CallRec","succes" + e.getEVENT_NAME());
                    return e;

                }
            }catch (Exception ex){Log.e("CallRec",ex.getMessage());}

        }
            return null;
    }
}