package com.example.litongwang.airport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.widget.EditText;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BeaconManager beaconManager;
    private Region region;
    private Button mButton;
    private String area;
    private String areaold;
    private String areanew;
    private Stack<String> stack = new Stack<>();
    Timer timer;

    private static final Map<String,String> PLACES_BY_BEACONS;

        static {
            Map<String,String> placesByBeacons = new HashMap<>();
            placesByBeacons.put("2087:49384","Test");
            placesByBeacons.put("49920:34810","Bedroom_A");
            placesByBeacons.put("44737:14541","Bedroom_B");
            placesByBeacons.put("59024:52400","Bedroom_c");
            placesByBeacons.put("17295:20790","Kitchen");
            placesByBeacons.put("30315:4375","Bathroom");

            PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
        }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.litongwang.airport/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.litongwang.airport/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mButton = (Button)findViewById(R.id.button_test);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectbeacon();
            }
        });



       detectbeacon();

    }
    @Override
    protected void onResume() {

        super.onResume();

        detectbeacon();
        /*
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        */
    }
    /*
    @Override
    protected void onPause(){
        beaconManager.stopRanging(region);
        super.onPause();
    }
    */


    public void detectbeacon(){

        Log.d("range", "detect");

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        null,null));
            }
        });
        Log.d("range","connect");
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d("range","timer");
                startTimer();

            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });

    }


    public void startTimer() {

        beaconManager = new BeaconManager(this);
        beaconManager.setForegroundScanPeriod(120000,120000);
        beaconManager.setBackgroundScanPeriod(120000,120000);

        region = new Region("ranged region",UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),null,null);

        timer = new Timer();

        Log.d("timer", "start1");

        timer.schedule(new RemindTask(), 0, 60000);
       
    }

    class RemindTask extends TimerTask {
        int count = 0;
        //Log.d("timer","clear");
        public void run() {
            Log.d("timer", "start3");


            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });

            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                    if (!list.isEmpty()) {
                        Beacon nearestBeacon = list.get(0);
                        // if (count < 1){
                        area = placesNearBeacon(nearestBeacon);
                        stack.push(area);
                        count++;
                        Log.d("timer", "stack");
                        // }

                        if (stack.size() > 1) {
                            areanew = stack.peek();
                            stack.pop();
                            areaold = stack.peek();
                            stack.push(areanew);
                            if (!areanew.equals(areaold)) {
                                popSelectPage();
                            }
                        }
                        Log.d("range", area);

                    }
                }
            });

            //timer.cancel(); //Terminate the timer thread
        }
    }


    private String placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return "";
    }


    public void popSelectPage(){
        Log.d("timer","popcount");
        Intent intent1=new Intent();
        intent1.setClass(MainActivity.this,select.class);
        startActivity(intent1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
