//https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/

package com.example.getapro.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.getapro.MainActivity;
import com.example.getapro.R;
import com.google.android.material.navigation.NavigationView;

import android.text.TextWatcher;
import android.widget.Toast;

import java.util.ArrayList;


public class ClientDashboard extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private static final String CLIENT_DASHBOARD_TAG = "ClientDashboard";

    public TextView mInputDisplay;
    public String mInput;

    final String CLIENT_CONTACT_FRAGMENT_TAG = "client_contact_fragemnt";
    Button inquiriesBtn, searchBtn, requestsBtn;
    TextView handymAnTV, messageTV;

    final int LOCATION_PERMISSION_REQUEST = 1;
    final int POST_NOTIFICATIONS = 2;

    // TODO: Rename and change types of parameters
    private String username;
    private int pos;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    String result;


    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            pos = getArguments().getInt("pos");
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
//                Bundle bundle = new Bundle();
//                bundle.putString("category", result);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        NavHostFragment.findNavController(this).navigate(); without view
        View view = inflater.inflate(R.layout.client_dashboard, container, false);

//        drawerLayout = view.findViewById(R.id.drawer_layout);
//        navigationView = view.findViewById(R.id.navigation_view);
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                drawerLayout.closeDrawers();
//                if (!item.isChecked()){
//                    item.setChecked(true);
//
//                    switch (item.getItemId()) {
//                        case R.id.item_signup:
//                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
//                            break;
//                        case R.id.item_login:
//                            Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment);
//                            break;
//
//                    }
//
//                }
//                return true;
//            }
//        });


        inquiriesBtn = view.findViewById(R.id.inquiries);
        searchBtn = view.findViewById(R.id.search_button);
        handymAnTV = view.findViewById(R.id.handyMAnTV);
        requestsBtn = view.findViewById(R.id.spetsRequests);
        messageTV = view.findViewById(R.id.message_tv);

        String message = "" + getArguments().getString("edttext");
        if (message != "")
            messageTV.setText(message);


//        String spetz = getArguments().getString("Selected_Handyman", "def");
//        String spetz = getArguments().getString("Selected_Handyman","");
//        Navigation.findNavController(view).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
//                Log.e("TAG", "onDestinationChanged: "+navDestination.getLabel());
//            }
//        });

//        handymAnTV.setText("" + getArguments().get("Selected_Handyman") );
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

                Bundle bundle = new Bundle();
                bundle.putString("category", result);
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_clientContact,bundle);
            }
        });




        //Messaging stuff ----------Messaging stuff ----------Messaging stuff ----------Messaging stuff ----------
        //if spetz then unsbscribe and then subscribe to his id token
//        messaging.unsubscribeFromTopic("A");
//        messaging.unsubscribeFromTopic("B");
//
//        groupAcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b)
//                    messaging.subscribeToTopic("A");
//                else
//                    messaging.unsubscribeFromTopic("A");
//            }
//        });



        return view;
    }


//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.drawer_menu,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
//        //handle menu item clicks
//    }
}