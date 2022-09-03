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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.getapro.R;
import android.text.TextWatcher;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientDashboard extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CLIENT_DASHBOARD_TAG = "ClientDashboard";

    public TextView mInputDisplay;
    public String mInput;

    final String CLIENT_CONTACT_FRAGMENT_TAG = "client_contact_fragemnt";
    Button inquiriesBtn, searchBtn;
    TextView handymAnTV;

    final int LOCATION_PERMISSION_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientDashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientDashboard2.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientDashboard newInstance(String param1, String param2) {
        ClientDashboard fragment = new ClientDashboard();
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

        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
        }

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
                handymAnTV.setText("change "+result+"?");

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        NavHostFragment.findNavController(this).navigate(); without view
        View  view = inflater.inflate(R.layout.client_dashboard, container, false);



        inquiriesBtn = view.findViewById(R.id.inquiries);
        searchBtn = view.findViewById(R.id.search_button);
        handymAnTV = view.findViewById(R.id.handyMAnTV);

//        String spetz = getArguments().getString("Selected_Handyman", "def");
//        String spetz = getArguments().getString("Selected_Handyman","");
//        Navigation.findNavController(view).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
//                Log.e("TAG", "onDestinationChanged: "+navDestination.getLabel());
//            }
//        });

//        handymAnTV.setText("" + getArguments().get("Selected_Handyman") );
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
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_clientContact);
            }
        });

        return view;
    }
}