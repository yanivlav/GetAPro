package com.example.getapro.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.getapro.R;

import java.util.List;

public class Location extends AppCompatActivity implements LocationListener {

    final int LOCATION_PERMISSION_REQUEST = 1;

    LocationManager locationManager;
    TextView coordinateTv, addressTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        coordinateTv = findViewById(R.id.location_coordinates_tv);
        addressTv = findViewById(R.id.location_address);

        if(Build.VERSION.SDK_INT>=23){
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
            }
        }

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(false);
        for (String provider:providers){
            Log.d(provider,"enabled:" + locationManager.isProviderEnabled(provider));
        }
        Criteria criteria = new Criteria();
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String bestProvider = locationManager.getBestProvider(criteria,false);
        if(bestProvider!=null)
            Log.d("best provider",bestProvider);

        Button startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=23){
                    int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                    if (hasLocationPermission != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
                    }
                    else locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100,Location.this);

                }
                else
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100,Location.this);
            }
        });

        Button stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.removeUpdates(Location.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(Location.this);
                builder.setTitle("Attention").setMessage("Application must have permission to location").setPositiveButton("Set it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:"+getPackageName()));
                        startActivity(intent);
                    }
                }).setNegativeButton("quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setCancelable(false).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        coordinateTv.setText(location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onLocationChanged(@NonNull List<android.location.Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}