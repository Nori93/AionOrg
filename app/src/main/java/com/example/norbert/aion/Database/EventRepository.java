package com.example.norbert.aion.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kamil Czaja on 20.01.2017.
 *  All rights reserved
 */

public class EventRepository {

    public static abstract class EventTableDetails implements BaseColumns {

       private static final String
       TABLE_NAME  = "Events",
       COLUMN_EVENT_ID = "Id",
       COLUMN_EVENT_NAME = "Name",
       COLUMN_START_DATATIME = "StartEvent",
       COLUMN_END_DATATIME = "EndEvent",
       COLUMN_ADDRESS = "Address",
       COLUMN_DESC = "Description",
       COLUMN_TAG = "Tag",
       COLUMN_GUEST = "Guest";

        private static final String[] COLUMNS =
        new String[]{
        COLUMN_EVENT_ID,
        COLUMN_EVENT_NAME,
        COLUMN_START_DATATIME,
        COLUMN_END_DATATIME,
        COLUMN_ADDRESS,
        COLUMN_DESC,
        COLUMN_TAG,
        COLUMN_GUEST};

    }


    public void insert(EventDB eventDB, DatabaseOperations db){
        try{


            SQLiteDatabase DB = db.getWritableDatabase();
            ContentValues content = new ContentValues();

            content.put(
                    EventTableDetails.COLUMN_EVENT_NAME,
                    eventDB.getEVENT_NAME());

            content.put(
                    EventTableDetails.COLUMN_START_DATATIME,
                    eventDB.getSTART_DATETIME());

            content.put(
                    EventTableDetails.COLUMN_END_DATATIME,
                    eventDB.getEND_DATETIME());

            content.put(
                    EventTableDetails.COLUMN_ADDRESS,
                    eventDB.getADDRESS());

            content.put(
                    EventTableDetails.COLUMN_DESC,
                    eventDB.getDESC());

            content.put(
                    EventTableDetails.COLUMN_TAG,
                    eventDB.getTAG());

            content.put(
                    EventTableDetails.COLUMN_GUEST,
                    eventDB.getGUEST());

            long success = DB.insert(EventTableDetails.TABLE_NAME,null,content);
            Log.d("DataBase operations","inserted row exercise");

        }
        catch (Exception ex){
            Log.d("DataBase operations","error while inserting row exercise");
        }
    }

    public EventDB getById(int Id,DatabaseOperations db){


        try{
            ArrayList<EventDB> eventDBs = new ArrayList<EventDB>();
            SQLiteDatabase DB = db.getReadableDatabase();


            String whereClause =  EventTableDetails.COLUMN_EVENT_ID+"= ?";
            String[] whereArgs = new String[] {
                   Integer.toString(Id)
            };

            Cursor CR = DB.query(EventTableDetails.TABLE_NAME,EventTableDetails.COLUMNS,whereClause,whereArgs,null,null,null);
            CR.moveToFirst();

            Log.d("DataBase operations","Recieved exercise");
            return getEvent(CR);
        }
        catch (Exception e){
            Log.e("E_Repos",e.getMessage());
            return  new EventDB(-1);
        }

    }

    public ArrayList<EventDB> getArrayList(DatabaseOperations db)
        {
            try{
                ArrayList<EventDB> ExerciseArrayList = new ArrayList<EventDB>();
                SQLiteDatabase DB = db.getReadableDatabase();

                Cursor CR = DB.query(EventTableDetails.TABLE_NAME, EventTableDetails.COLUMNS,null,null,null,null,null);
                CR.moveToFirst();

                do{
                  ExerciseArrayList.add(getEvent(CR));
                }while(CR.moveToNext());

                Log.d("DataBase operations","Recieved all exercises");

                return ExerciseArrayList ;
            }
            catch (Exception e){
                Log.e("E_Repos", e.getMessage());
                return  new ArrayList<EventDB>();
            }
        }

    private EventDB getEvent(Cursor cursor){
        return new EventDB(
                Integer.parseInt(cursor.getString(0)), //ID
                cursor.getString(1),                   //NAME
                cursor.getString(2),                   //S_DateT
                cursor.getString(3),                   //E_DateT
                cursor.getString(4),                   //ADDRESS
                cursor.getString(5),                   //DESC
                cursor.getString(6),                   //TAG
                cursor.getString(7));                  //GUEST
    }
    public boolean delete (int eventId,DatabaseOperations db){ //return false if exception was throwed

        SQLiteDatabase DB = db.getWritableDatabase();
        try{
            DB.beginTransaction();
            String whereClause = EventTableDetails.COLUMN_EVENT_ID+"= ?";
            String[] whereArgs = new String[] {
                    Integer.toString(eventId)
            };
            DB.delete(EventTableDetails.TABLE_NAME,whereClause,whereArgs);

            DB.setTransactionSuccessful();
            Log.d("DataBase operations", "Exercise successfully deleted");
            return  true;
        }
        catch (Exception ex)
        {
            return false;
        }
        finally {
            DB.endTransaction();
        }
    }

    public boolean delete (String start,DatabaseOperations db){ //return false if exception was throwed

        SQLiteDatabase DB = db.getWritableDatabase();
        try{
            DB.beginTransaction();
            String whereClause = EventTableDetails.COLUMN_START_DATATIME+"= ?";
            String[] whereArgs = new String[] {
                    start
            };
            DB.delete(EventTableDetails.TABLE_NAME,whereClause,whereArgs);

            DB.setTransactionSuccessful();
            Log.d("DataBase operations", "Exercise successfully deleted");
            return  true;
        }
        catch (Exception ex)
        {
            return false;
        }
        finally {
            DB.endTransaction();
        }
    }
    public boolean deleteAll(DatabaseOperations db){
        SQLiteDatabase DB = db.getWritableDatabase();
        DB.beginTransaction();
        DB.delete(EventTableDetails.TABLE_NAME,null,null);
        return true;
    }
}
