package com.example.litongwang.airport;
 import android.app.Application;
 import android.content.Intent;
 import android.view.View;
 import android.widget.RadioButton;

 import com.estimote.sdk.Beacon;
 import com.estimote.sdk.BeaconManager;
 import com.estimote.sdk.Region;

 import java.util.List;
 import java.util.UUID;

/**
 * Created by litongwang on 2/15/2016.
 */
public class Myapplication extends Application {

    private  BeaconManager beaconManager;

    @Override
    public void onCreate(){
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        22504, 48827));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                show();
            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });
    }

    public void show(){
        Intent intent2 = new Intent(this,select.class);
        startActivity(intent2);
    }
}
