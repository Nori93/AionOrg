package com.example.norbert.aion;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.norbert.aion.Database.DatabaseOperations;
import com.example.norbert.aion.Database.EventDB;
import com.example.norbert.aion.Database.EventRepository;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.Calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.norbert.aion.R.id.name;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        EasyPermissions.PermissionCallbacks{

    //Login
    private LinearLayout Prof_Section;
    private Button SingOut;
    private SignInButton SignIn;
    private TextView Name,Email;
    private ImageView Prof_Pic;
    //Calendar
    private ProgressDialog Progress;
    private Toast toast;
    //Login
    private GoogleApiClient googleApiClient;
    //Calender
    GoogleAccountCredential accountCredential;
    GoogleSignInAccount account;

    //Login
    private static final int REQ_CODE = 9001;
    //Calender
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    //Calender
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    //All Event From Calendar
    private int NUMBER_OF_Events;
    List<Event> eventList;
    Events events;
    private Calendar mService = null;

    //DataTime for select what Events we need
    DateTime TODAY = new DateTime(System.currentTimeMillis());
    DateTime WEEK = new DateTime(TODAY.getValue() + 604800000);




    //Contacts List

    HashMap<String,String> ContactList = new HashMap<String, String>();
    HashMap<String,String> EventsList = new HashMap<String, String>();


    //Database
    private DatabaseOperations databaseOperations;
    private EventRepository eventRepository;

    //Test
    TextView Console;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Login
        Prof_Section = (LinearLayout)findViewById(R.id.prof_section);
        SingOut = (Button)findViewById(R.id.bn_logout);
        SignIn = (SignInButton)findViewById(R.id.bn_login);
        Name = (TextView)findViewById(name);
        Email = (TextView)findViewById(R.id.email);
        Prof_Pic = (ImageView)findViewById(R.id.prof_pic);

        SignIn.setOnClickListener(this);
        SingOut.setOnClickListener(this);
        Prof_Section.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        //Calendar
        toast = new Toast(this);
        Progress = new ProgressDialog(this);
        Progress.setMessage(" Please wait");
        accountCredential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        addPermisions();
        //Test Console
        Console = (TextView)findViewById(R.id.Console);
        try{
            signIn();
        }catch (Exception e){
            toast.makeText(getApplication(),"Login false, please try again.",Toast.LENGTH_LONG).show();
        }
        databaseOperations = new DatabaseOperations(this);
        eventRepository = new EventRepository();
        eventRepository.deleteAll(databaseOperations);
    }

    private void addPermisions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE},
                1);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bn_login:
                signIn();
                break;
            case R.id.bn_logout:
                signOut();
        }
    }

//-----------------------------Account Login------------------------------------------------------
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //
    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(intent,REQ_CODE);

    }

    private void signOut(){

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });

    }

    private void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            accountCredential.setSelectedAccount(account.getAccount());
            Name.setText(name);
            Email.setText(email);
            try {
                String img_url = account.getPhotoUrl().toString();
                Glide.with(this).load(img_url).into(Prof_Pic);
            }catch (Exception e){
                Prof_Pic.setMinimumHeight(200);
                Prof_Pic.setBackgroundColor(Color.argb(255,255,0,0));
            }

            updateUI(true);
        }
        else {
            updateUI(false);
        }

    }

    private void updateUI(boolean isLogin){
        if(isLogin){
            Prof_Section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        }
        else {
            Prof_Section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }
    }


//---------------------------Calendar---------------------------------------------------------------




    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
            toast.makeText(getApplication(),"isGooglePlayServiceAvailable is false",Toast.LENGTH_LONG).show();
        } else if (accountCredential.getSelectedAccountName() == null) {

            toast.makeText(getApplication(),"No Account name in accountCredential.",Toast.LENGTH_LONG).show();
        } else if (! isDeviceOnline()) {

            toast.makeText(getApplication(),"No network connection available.",Toast.LENGTH_LONG).show();
        } else {
            new MakeRequestTask(accountCredential).execute();
            toast.makeText(getApplication(),"MakeRequestTask.",Toast.LENGTH_LONG).show();
        }
    }





    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {

                    toast.makeText(getApplication(), "This app requires Google Play Services. Please install \n" +
                            "Google Play Services on your device and relaunch this app.",Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQ_CODE:
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleResult(result);
                   getResultsFromApi();


                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {

                return getDataFromApi();

            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.


            events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(TODAY)
                    .setTimeMax(WEEK)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            eventList = events.getItems();

            for (Event event: eventList) {
                EventsList.put(event.getEnd().getDateTime().toString(),
                           "Jestem teraz na "+event.getSummary() +"\n" +
                           "W miejsciu " + event.getLocation() + "\n");

                 /*eventRepository.insert(new EventDB(
                                   Integer.parseInt(event.getId()),
                                   event.getSummary(),
                                   event.getStart().getDateTime().toString(),
                                   event.getEnd().getDateTime().toString(),
                                   event.getLocation(),
                                   event.getDescription(),
                                   event.getEtag(),
                                   event.getKind()),
                           databaseOperations);
                   toast.makeText(getApplication(),"Insert on "+event.getSummary(),Toast.LENGTH_LONG).show();



                /*
                Id, EVENT_NAME, START_DATETIME, END_DATETIME, ADDRESS, DESC, TAG, GUEST;
                 */




            }
            Intent intent = new Intent("my.events");
            intent.putExtra("Events",EventsList);
            sendBroadcast(intent);
            List<String> list = new ArrayList<String>(){};
            list.add("");
            return list;


        }


        @Override
        protected void onPreExecute() {
            Progress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            Progress.hide();
            if (output == null || output.size() == 0) {

                toast.makeText(getApplication(),"No results returned.",Toast.LENGTH_LONG).show();
            } else {

                toast.makeText(getApplication(),"Successful.",Toast.LENGTH_LONG).show();

                for (Event event: eventList) {
                    eventRepository.insert(new EventDB(
                                    event.getSummary(),
                                    event.getStart().getDateTime().getValue()+"",
                                    event.getEnd().getDateTime().getValue()+"",
                                    event.getLocation(),
                                    event.getDescription(),
                                    event.getEtag(),
                                    event.getKind()),
                            databaseOperations);
                    Log.e("DB","insert"+ event.getSummary());
                }


            }
        }



        @Override
        protected void onCancelled() {
            Progress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {

                    toast.makeText(getApplication(),"The following error occurred:\n"
                            + mLastError.getMessage(),Toast.LENGTH_LONG).show();

                }
            } else {

                toast.makeText(getApplication(),"Request cancelled.",Toast.LENGTH_LONG).show();
            }
        }
    }

    //-------- CallReceiver ---------------





}




