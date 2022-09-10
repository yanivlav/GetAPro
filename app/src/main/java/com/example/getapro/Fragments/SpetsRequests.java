package com.example.getapro.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getapro.MainActivity;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

//    String API_TOKEN = "AAAArNt3lv8:APA91bHUVkK6s9DCfNo0V4yLyZhBv68LWRZAyc-JPiJDDF5LMem2qBORMQmjGlmNy4w47d9SHQwMD-pnc5v6ELTV-HPrbIegYbbL5wG4zwBYZbxxZYphPa6vzXbIjibukbxtuPSlNANe";
public class SpetsRequests extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference("users");


    TextView messageTv;
    FirebaseMessaging messaging = FirebaseMessaging.getInstance();
    BroadcastReceiver receiver;
    TextView  mtv;
    String TAG = "MyFirebaseInstanceIdService";
    String API_TOKEN = "AAAArNt3lv8:APA91bHUVkK6s9DCfNo0V4yLyZhBv68LWRZAyc-JPiJDDF5LMem2qBORMQmjGlmNy4w47d9SHQwMD-pnc5v6ELTV-HPrbIegYbbL5wG4zwBYZbxxZYphPa6vzXbIjibukbxtuPSlNANe";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.spets_requests, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}