package com.example.getapro.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.getapro.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

public class ClientContact extends Fragment {

    Button proceedB;
    final int LOCATION_PERMISSION_REQUEST = 1;
    FusedLocationProviderClient client;
    EditText locEt;
    Geocoder geocoder;
    Handler handler = new Handler();
    SupportMapFragment mapFragment;
    double lat ;
    double lng;
    ProgressBar progressBar;
    private String username;
    private String category;
    private String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
            category = getArguments().getString("category");
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

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.client_contact, container, false);
        locEt = view.findViewById(R.id.loc_et);
        progressBar = view.findViewById(R.id.indeterminateBar);
        geocoder = new Geocoder(getContext());
        Handler handler = new Handler();

        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
            else {
                locEt.setHint(R.string.wait_for_address);
                startLocation();
            }
        }
        else {
            locEt.setHint("Wait while we get your address...");
            startLocation();
        }

        mapFragment = SupportMapFragment.newInstance();
        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.map_frame, mapFragment)
                .commit();

//         Get a handle to the fragment and register the callback.
//        mapFragment.getMapAsync(this);

        proceedB = view.findViewById(R.id.proceedButton);
        proceedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null )
                    Toast.makeText(getContext(), "Sign up or register first!", Toast.LENGTH_SHORT).show();
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("address",locEt.getText().toString());
                    bundle.putString("category",category);
//                bundle.putString("district",state);
                    bundle.putString("district",state);
                    Navigation.findNavController(view).navigate(R.id.action_clientContact_to_clientFinelForm,bundle);
                }
            }
        });
        return view;
    }

    private void startLocation() {

        client = LocationServices.getFusedLocationProviderClient(getContext());

        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                updateLoc(location);
            }
        };


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(2);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(2000);

        if(Build.VERSION.SDK_INT >= 23 && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(locationRequest,callback,null);
        else client.requestLocationUpdates(locationRequest,callback,null);


    }

    private void updateLoc(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();


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
                            state = bestAddr.getAdminArea();
                            String city = bestAddr.getLocality();
                            String country = bestAddr.getCountryName();
                            locEt.setText(address);
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .title("Marker"));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18));
                                }
                            });

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

//
//    final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading you're inquiries, please wait...");
//                progressDialog.show();
//
//                progressDialog.dismiss();