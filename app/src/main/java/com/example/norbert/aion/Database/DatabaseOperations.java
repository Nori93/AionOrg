package com.example.norbert.aion.Database;

/**
 * Created by norbert on 19.02.2017.
 */

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;



import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class DatabaseOperations extends SQLiteAssetHelper {

    public static final int DB_VERSION = 2;
    public static final String DATABASE_NAME = "baza.db";
    Context context;




    public DatabaseOperations(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    //  @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        String CREATE_INFORMACJE_TABLE = "CREATE TABLE \"Informacje\" ( `waga` REAL, `wzrost` REAL, `ilosc_kalorii` INTEGER, `poziom_aktywnosci` TEXT, `tluszcz` INTEGER, `weglowodany` INTEGER, `bialko` INTEGER, `plec` NUMERIC, `budowa_ciala` TEXT, `ID` INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, `cel` TEXT )";
//        db.execSQL(CREATE_INFORMACJE_TABLE);
//        Log.d(LOG,"Informacje created");
//        String CREATE_SERIA_TABLE = "CREATE TABLE \"Seria\" ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, `powtorzenia` INTEGER, `obciazenie` INTEGER, `ID_cw` INTEGER, `ID_tr` INTEGER)";
//        db.execSQL(CREATE_SERIA_TABLE);
//        Log.d(LOG,"Serie created");
//        String CREATE_EXERCISE_TABLE = "CREATE TABLE \"cwiczenia\" ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, `nazwa` TEXT UNIQUE, `opis` TEXT, `instrukcje` TEXT, `path` TEXT )";
//        db.execSQL(CREATE_EXERCISE_TABLE);
//        Log.d(LOG,"Cwiczenia created");
//        String CREATE_TRAINING_TABLE ="CREATE TABLE `trening` ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `data` TEXT NOT NULL )";
//        db.execSQL(CREATE_TRAINING_TABLE);
//        Log.d(LOG,"Trening created");
//
//      //  copyDatabaseData();
//        try
//        {
//            copyDataBase(db);
//        }
//        catch (IOException ex)
//        {
//            Log.d(LOG,"Error while copying exercise data");
//        }
//
//        feedDatabase(db);
//    }


//    private void copyDataBase(SQLiteDatabase DB) throws IOException{
//
//
//
//        Uri db = Uri.parse("android.resource://com.example.norbert.myapplication/raw/db.db");
//        //Open your local db as the input stream
//        InputStream myInput =  new FileInputStream(db.getPath());
//
//
//        //Open the empty db as the output stream
//        OutputStream myOutput = new FileOutputStream(DB.getPath());
//
//        //transfer bytes from the inputfile to the outputfile
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer))>0){
//            myOutput.write(buffer, 0, length);
//        }
//
//        Log.d(LOG,"copying exercise data success");
//
//        //Close the streams
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();
//
//    }

//    public void openDatabase(){
//
//        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
//        if(mDatabase !=null && mDatabase.isOpen())
//        {
//            return;
//        }
//        Uri db = Uri.parse("android.resource://com.example.norbert.myapplication/raw/db.db");
//      //  String tmp = context.getResources().db.getPath();
//
//      //  Log.d(LOG,tmp);
//       mDatabase = SQLiteDatabase.openDatabase(db.getPath(),null,SQLiteDatabase.OPEN_READWRITE);
//    }
//
//    public  void closeDatabase(){
//        if(mDatabase != null)
//        {
//            mDatabase.close();
//        }
//    }

//    private void copyDatabaseData(){
//
//        try
//        {
//            List<Exercise> exerciseList =  getAllExcercises();
//            ExerciseRepository exerciseRepository = new ExerciseRepository();
//
//            for(Exercise exercise : exerciseList)
//            {
//                exerciseRepository.insertNewExcerise(exercise,this);
//            }
//            Log.d(LOG,"copying exercise data success");
//        }catch (Exception ex){
//            Log.d(LOG,"Error while copying exercise data");
//        }
//    }
//
//    public List<Exercise> getAllExcercises(){
//
//        Exercise exercise = null;
//        List<Exercise> exerciseList =  new ArrayList<>();
//
//        openDatabase();
//        Cursor cursor = mDatabase.rawQuery("SELECT * FROM cwiczenia",null);
//        cursor.moveToFirst();
//
//        while(!cursor.isAfterLast()){
//            exerciseList.add(new Exercise(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        closeDatabase();
//
//        return  exerciseList;
//    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
