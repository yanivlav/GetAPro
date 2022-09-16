package com.example.getapro.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getapro.Helpers.FormAdapter;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SpetsRequests extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //name of the instance of the Forms table
    DatabaseReference forms_fire = database.getReference("Forms");
    FormAdapter adapter;
    ArrayList<Form> forms_local = new ArrayList<>();
    ArrayList<Form> temp = new ArrayList<>();

    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spetz_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        adapter = new FormAdapter(forms_local);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new FormAdapter.FormListener() {

            @Override
            public void onInfoClicked(int position, View view) {
                Bundle bundle =  new Bundle();
                bundle.putInt("position",position);
                bundle.putParcelable("form", forms_local.get(position));//maybe the form class should implement parceble
                Navigation.findNavController(view).navigate(R.id.action_spetsRequests_to_form_Dialog,bundle);
            }

            @Override
            public void onFormClicked(int position, View view) {

            }

            @Override
            public void onFormLongClicked(int position, View view) {

            }
        });


        final FirebaseUser user = firebaseAuth.getCurrentUser();
        forms_fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                forms_local.clear();
                temp.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot chailed : snapshot.getChildren()) {
                            Form form = chailed.getValue(Form.class);
                            temp.add(form);
                        }
                    }
                }


                for (int i=0; i<temp.size(); i++){
                    if  (temp.get(i).getSelectedSpetzUid().equals(user.getUid())){
                        forms_local.add(temp.get(i));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recyclerView.setAdapter(adapter);


        return view;
    }
}