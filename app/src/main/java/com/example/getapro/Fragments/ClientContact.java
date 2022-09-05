package com.example.getapro.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.getapro.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class ClientContact extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button proceedB, changeServiceB, locationB;

    final int LOCATION_PERMISSION_REQUEST = 1;

    FusedLocationProviderClient client;
    EditText locEt;

    Geocoder geocoder;

    Handler handler = new Handler();


    private String mParam1;
    private String mParam2;

    public ClientContact() {
        // Required empty public constructor
    }

    public static ClientContact newInstance(String param1, String param2) {
        ClientContact fragment = new ClientContact();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Attention").setMessage("Application must have permission to location").setPositiveButton("Set it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        startActivity(intent);
                    }
                }).setNegativeButton("quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).setCancelable(false).show();
            }
        }
        else startLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view = inflater.inflate(R.layout.client_contact, container, false);

        locEt = view.findViewById(R.id.loc_et);


        geocoder = new Geocoder(getContext());
        Handler handler = new Handler();

        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
            else {
                locEt.setHint("Wait while we get your address...");
                startLocation();
            }
        }
        else {
            locEt.setHint("Wait while we get your address...");
            startLocation();
        }



        proceedB = view.findViewById(R.id.proceedButton);
        proceedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_clientContact_to_clientFinelForm);

            }
        });
        changeServiceB = view.findViewById(R.id.changeServiceBtn);
        changeServiceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeServiceB.setText("Change " + getArguments().getString("Selected_Handyman"));
                Navigation.findNavController(view).navigate(R.id.action_clientContact_to_clientDashboard);

            }
        });

        locationB = view.findViewById(R.id.locationBtn);
        locationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return view;
    }

    private void startLocation() {
        client = LocationServices.getFusedLocationProviderClient(getContext());
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();

                updateLoc(location);

            }
        };

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);


        if(Build.VERSION.SDK_INT >= 23 && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(locationRequest,callback,null);
        else client.requestLocationUpdates(locationRequest,callback,null);



    }

    private void updateLoc(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    List<Address> addresses = geocoder.getFromLocation(lat,lng,1);
                    final Address bestAddr = addresses.get(0);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String address = bestAddr.getAddressLine(0);
                            String state = bestAddr.getAdminArea();
                            String city = bestAddr.getLocality();
                            String country = bestAddr.getCountryName();
                            locEt.setText(address);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}