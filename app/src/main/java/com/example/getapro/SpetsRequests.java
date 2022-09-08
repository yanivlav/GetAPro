package com.example.getapro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

public class SpetsRequests extends Fragment {


    BroadcastReceiver receiver;
    TextView messageTv;
    FirebaseMessaging messaging = FirebaseMessaging.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.spets_requests, container, false);
        messageTv = view.findViewById(R.id.massage);


        messaging.unsubscribeFromTopic("Yaniv");
        messaging.subscribeToTopic("Yaniv");


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                messageTv.setText(intent.getStringExtra("message"));
            }
        };

        IntentFilter filter = new IntentFilter("message_received");
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,filter);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);

    }
}