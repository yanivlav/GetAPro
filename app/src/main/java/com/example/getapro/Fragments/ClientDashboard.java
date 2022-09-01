//https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/

package com.example.getapro.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
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
public class ClientDashboard extends Fragment implements DialogFragment.OnInputListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CLIENT_DASHBOARD_TAG = "ClientDashboard";

    @Override public void sendInput(String input)
    {
        mInput = input;
        setInputToTextView();
    }

    public TextView mInputDisplay;
    public String mInput;

    final String CLIENT_CONTACT_FRAGMENT_TAG = "client_contact_fragemnt";
    Button inquiriesBtn, searchBtn;
    TextView handymAnTV;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        NavHostFragment.findNavController(this).navigate(); without view
        View  view = inflater.inflate(R.layout.client_dashboard, container, false);

        inquiriesBtn = view.findViewById(R.id.inquiries);
        searchBtn = view.findViewById(R.id.search_button);
        handymAnTV = view.findViewById(R.id.handyMAnTV);

        if(getArguments() != null) //        changeServiceB.setText("Change " + getArguments().getString("serviceName"));
        {
            handymAnTV.setText(getArguments().getString("Selected_Handyman"));
        }



        handymAnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("serviceName", arrayList);
//                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_handyMan_Dialog, bundle);
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_handyMan_Dialog);

            }
        });

        inquiriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_clientInquiries);
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_handyMan_Dialog);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
//                bundle.putString("serviceName", searchBtn.getText().toString());
                bundle.putString("serviceName", "Shiputznik");
//                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_clientContact, bundle);
                Navigation.findNavController(view).navigate(R.id.action_clientDashboard_to_handyMan_Dialog, bundle);
            }
        });

        return view;
    }

    private void setInputToTextView()
    {
        mInputDisplay.setText(mInput);
    }
}