package com.example.getapro.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getapro.Helpers.FormAdapter;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ClientInquiries extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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
                StorageReference pathReference = storageReference.child("Problems/"+user.getUid()+"_Form_Number_"".jpg");

                forms_local.clear();
                int num = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        pathReference = storageReference.child("Problems/"+user.getUid()+"_Form_Number_"+num+".jpg");
                        pathReference.getDownloadUrl();
                        Form form = snapshot.getValue(Form.class);
                        form.setIssueImage(pathReference.getDownloadUrl());
                        forms_local.add(form);
                        num++;
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
}



//    // Create a reference with an initial file path and name
//    StorageReference pathReference = storageRef.child("images/stars.jpg");
//
//    // Create a reference to a file from a Cloud Storage URI
//    StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");
//
//    // Create a reference from an HTTPS URL
//// Note that in the URL, characters are URL escaped!
//    StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");

//    After you've created an appropriate reference, you can then download files from Cloud Storage by calling the getBytes() or getStream() method.

//        If you prefer to download the file with another library, you can get a download URL with getDownloadUrl().