package com.example.getapro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getapro.Helpers.FormAdapter;
import com.example.getapro.MyObjects.Form;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.spetz_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        adapter = new FormAdapter(forms_local);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new FormAdapter.FormListener() {
            @Override
            public void onFormClicked(int position, View view) {

            }

            @Override
            public void onFormLongClicked(int position, View view) {

            }
        });


        final FirebaseUser user = firebaseAuth.getCurrentUser();
        forms_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                forms_local.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Form form = snapshot.getValue(Form.class);
                        forms_local.add(form);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        recyclerView.setAdapter(adapter);


        return view;
    }

//    BroadcastReceiver receiver;
//    TextView messageTv;
//    FirebaseMessaging messaging = FirebaseMessaging.getInstance();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        View view =  inflater.inflate(R.layout.spets_requests, container, false);
//        messageTv = view.findViewById(R.id.massage);
//
//
//        messaging.unsubscribeFromTopic("Yaniv");
//        messaging.subscribeToTopic("Yaniv");
//
//
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                messageTv.setText(intent.getStringExtra("message"));
//            }
//        };
//
//        IntentFilter filter = new IntentFilter("message_received");
////        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,filter);
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
//
//    }
}