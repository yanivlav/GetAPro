package com.example.getapro.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.getapro.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientContact extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button proceedB, changeServiceB;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientContact() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientContactFragment2.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view = inflater.inflate(R.layout.client_contact, container, false);
        proceedB = view.findViewById(R.id.proceedButton);
        proceedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_clientContact_to_clientFinelForm2);

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



        return view;
    }
}