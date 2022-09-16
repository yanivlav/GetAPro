//https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/

package com.example.getapro.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Lifecycle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.getapro.MainActivity;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.MyObjects.User;
import com.example.getapro.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.TextWatcher;
import android.widget.Toast;

import java.util.ArrayList;


public class ClientDashboard extends Fragment{
    private static final String CLIENT_DASHBOARD_TAG = "ClientDashboard";

    public TextView mInputDisplay;
    public String message;

    final String CLIENT_CONTACT_FRAGMENT_TAG = "client_contact_fragemnt";
    Button inquiriesBtn, searchBtn, requestsBtn;
    ImageView logoIv;
    TextView handymAnTV, messageTV;

    final int LOCATION_PERMISSION_REQUEST = 1;
    final int POST_NOTIFICATIONS = 2;

    // TODO: Rename and change types of parameters
    private String username;
    private int pos;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    String result;

    //check if user is a spetz
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users_fire = database.getReference("Users");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();

    ArrayList<User> users_local = new ArrayList<>();

    BroadcastReceiver receiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            message = getArguments().getString("message");
            if (message!=null)
                messageTV.setText(message);
//            else
//                messageTV.setText("since 1972");
            username = getArguments().getString("username");
        }

        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
        }




//        if (Build.VERSION.SDK_INT >= 23) {
//            int hasNotificationsPermission = getContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS);
//            if (getContext().checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
//                    PackageManager.PERMISSION_GRANTED) {
//                // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//            } else {
//                // Directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
//            }
//        }

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                result = bundle.getString("bundleKey");
                // Do something with the result
                handymAnTV.setText("change "+result+"?");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        NavHostFragment.findNavController(this).navigate(); without view
        View view = inflater.inflate(R.layout.client_dashboard, container, false);

        inquiriesBtn = view.findViewById(R.id.inquiries);
        searchBtn = view.findViewById(R.id.search_button);
        handymAnTV = view.findViewById(R.id.handyMAnTV);
        requestsBtn = view.findViewById(R.id.spetsRequests);
        messageTV = view.findViewById(R.id.message_tv);
        logoIv = view.findViewById(R.id.logo);

        IntentFilter filter = new IntentFilter("il.org.syntax.sms_received");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                if (message!=null)
                    messageTV.setText(message);
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);

//        if (getArguments() != null) {
//            message = getArguments().getString("message");
//            if (message!=null)
//                messageTV.setText(message);
//
//        }

        if (user != null){
            users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users_local.clear();
                    if(dataSnapshot.exists()) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Spetz spetz = snapshot.getValue(Spetz.class);
                            if (spetz.getOccupation() != null) {
                                requestsBtn.setVisibility(View.VISIBLE);
                                messageTV.setText("Here you'll get new descriptions from new clients");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            inquiriesBtn.setVisibility(View.INVISIBLE);
            messageTV.setText("since 1792");
        }
        //check  if this is the right on
//        messageTV.setText(message);


        requestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_spetsRequests);

            }
        });
        handymAnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_handyMan_Dialog);

            }
        });
        inquiriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_clientInquiries);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("category", result);
                    Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_clientContact, bundle);
                }
                else
                    Toast.makeText(getContext(), "Choose a Spetz!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }

}
