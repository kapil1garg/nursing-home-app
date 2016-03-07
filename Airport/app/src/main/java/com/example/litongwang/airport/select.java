package com.example.litongwang.airport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import android.app.Activity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class select extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int count;
    private int current_activity = 0;
    private int previous_activity = 0;
    private Button submit;
    private TextView final_text;
    private int time = 0;
    private int user = 1;
    private String timestamp;
    private String uuid;
    private Session session;
    private String area;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "select Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.litongwang.airport/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "select Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.litongwang.airport/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        count = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Intent intent = getIntent();
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        uuid = tManager.getDeviceId();
        final_text = (TextView) findViewById(R.id.user);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);
        timestamp = getCurrentTimeStamp();

        area = MainActivity.getArea();
        if (area.equals("Kitchen")){
            final_text.setText("Are you in the kitchen or living room?");
        }else if (area.equals("Bathroom")){
            final_text.setText("Are you in the bathroom?");
        }else if (area.equals("Bedroom_A")){
            final_text.setText("Are you in Kapil's Bedroom?");
        }else if (area.equals("Bedroom_B")){
            final_text.setText("Are you in Connor's Bedroom?");
        }else if (area.equals("Bedroom_C")){
            final_text.setText("Are you in Kathryn's Bedroom?");
        }else if (area.equals("Test")){
            final_text.setText("Are you testing?");
        }

        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText Time = (EditText) findViewById(R.id.duration);

                time = new Integer(Time.getText().toString());

                new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            connectRemote("lwc078", "wlt291517!", "murphy.wot.eecs.northwestern.edu", 22);
                            try {
                                Class.forName("com.mysql.jdbc.Driver");

                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                Log.d("catch", "error");
                            }
                            String url = "jdbc:mysql://127.0.0.1:3306/mhealthplay";
                            String user_name = "mhealth";
                            String pass_word = "mhealth";
                            Connection con;
                            try {
                                con = DriverManager.getConnection(url, user_name, pass_word);
                                Log.d("jdbc", "connect");

                                Statement st = con.createStatement();

                                String insert_query = "INSERT INTO nursing_home_survey VALUE(NULL,'" + timestamp + "','" + user + "','" + previous_activity + "','" + current_activity + "','" + time + "','" + uuid + "')";

                                st.executeUpdate(insert_query);
                                Log.d("onListItemClick", "Successfully save in the database.");

                                st.close();
                            } catch (SQLException se) {
                                Log.d("onListItemClick", "Cannot save in the database.");
                                se.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        session.disconnect();
                        return null;
                    }
                }.execute(1);

                Intent intent1 = new Intent();
                intent1.setClass(select.this, MainActivity.class);
                startActivity(intent1);
                //upload the activity and time
            }
        });
        //clearSharePreferences();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void selectActivity(View view) {

        switch (view.getId()) {
            case R.id.toilet_button1:
                current_activity = 1;
                break;
            case R.id.shower_button1:
                current_activity = 2;
                break;
            case R.id.dressing_button1:
                current_activity = 3;
                break;
            case R.id.prepare_button1:
                current_activity = 4;
                break;
            case R.id.sleep_button1:
                current_activity = 5;
                break;
            case R.id.others_button1:
                current_activity = 6;
                break;

            case R.id.toilet_button2:
                previous_activity = 1;
                break;
            case R.id.shower_button2:
                previous_activity = 2;
                break;
            case R.id.dressing_button2:
                previous_activity = 3;
                break;
            case R.id.prepare_button2:
                previous_activity = 4;
                break;
            case R.id.sleep_button2:
                previous_activity = 5;
                break;
            case R.id.others_button2:
                previous_activity = 6;
                break;

            case R.id.yes_button:
                user = 1;
                break;
            case R.id.no_button:
               user = 0;
                break;
        }
    }

    public void connectRemote(String username, String password,
                                     String hostname, int port)
            throws Exception {

        JSch jsch = new JSch();
        session = jsch.getSession(username, hostname, 22);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);
        session.connect();
        session.setPortForwardingL(3306, "127.0.0.1", 3306);

        if (session.isConnected())
            Log.d("connectRemote", "Successfully connected");
        else
            Log.d("connectRemote", "Connection failed.");
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(new Date()); // Find todays date

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
