package com.example.getapro.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getapro.Helpers.SpetzAdapter;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
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

import java.util.ArrayList;

public class SpetzList extends Fragment {

    ArrayList<Spetz> spetzs;
    ArrayList<Form> forms_user = new ArrayList<>();
    ArrayList<Form> forms_spetz = new ArrayList<>();

    BroadcastReceiver receiver;
    String TAG = "MyFirebaseInstanceIdService";
    String token;

    String apiToken = "AAAA8BK5-Gs:APA91bEHQfVvwoR62JRU2tdmdkTZcdckkftwsqxqE1NOmZCmfrkUFbzLHJQPuDkY69u3dh5aL_4s1u1AD1GTxhLHant-oUCuyw4cx-dEC-TJz_yFiPf6e1apu6FXwGKAekKnwqBBO6co";
    FirebaseMessaging messaging = FirebaseMessaging.getInstance();

    private String spetzCategory;
    private String address;
    private String district;
    private String desc;
    private int pic;
    private Form newForm;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ArrayList<Spetz> spetzs_local = new ArrayList<>();
    DatabaseReference users_fire = database.getReference("Users");
    DatabaseReference forms_fire = database.getReference("Forms");
    SpetzAdapter adapter;
    final FirebaseUser user = firebaseAuth.getCurrentUser();
//
//    public static SpetzList newInstance(String param1, String param2) {
//        SpetzList fragment = new SpetzList();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spetzCategory = getArguments().getString("category");
            address = getArguments().getString("address");
            district = getArguments().getString("district");
            desc = getArguments().getString("desc");
            pic = getArguments().getInt("pic");



        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.spetz_list, container, false);
//

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        adapter = new SpetzAdapter(spetzs_local);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);




        adapter.setListener(new SpetzAdapter.ISpetzListener() {
            @Override
            public void onInfoClicked(int position, View view) {
//                Bundle bundle =  new Bundle();
//                bundle.putParcelable("spetzs", spetzs.get(position));//maybe the form class should implement parceble
//                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_spetzCard);

            }

            @Override
            public void onSpetzClicked(int position, View view) {
                //1. read forms from database
                //2. add form to local forms array than push to database
                //3. send notification to the spetz

                //Add form--------------        //Add form--------------        //Add form--------------
                //pass with bundle the real user form to here

                //add form to the user inquries
//                forms_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                newForm = new Form(desc, pic, spetzs_local.get(position).getUid(),spetzCategory, address);
//                forms_spetz.clear();
//                forms_user.clear();


                forms_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        forms_user.clear();
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Form form = snapshot.getValue(Form.class);
                                forms_user.add(form);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //update the database

                forms_user.add(newForm);
                forms_fire.child(user.getUid()).setValue(forms_user);


//                //add form to spetz selected from list
                forms_fire.child(spetzs_local.get(position).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        forms_spetz.clear();
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Form form = snapshot.getValue(Form.class);
                                forms_spetz.add(form);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //update the database
                if (user.getUid() != spetzs_local.get(position).getUid()){
                    forms_spetz.add(newForm);
                    forms_fire.child(spetzs_local.get(position).getUid()).setValue(forms_spetz);
                }
                else Toast.makeText(getContext(), "can't open to yourself", Toast.LENGTH_SHORT).show();
//
//


//                Open Chat
                Toast.makeText(getContext(),"Sending the massage", Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putString("to", spetzs.get(position).getUserName());
                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_clientDashboard);
            }


            @Override
            public void onSpetzLongClicked(int position, View view) {

            }
        });



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        System.out.println(token);
                        Log.d(TAG, "Refreshed token: " + token);
                        Toast.makeText(getContext(), "Device Token is: " + token, Toast.LENGTH_SHORT).show();
//                        mtv.setText(token);
                    }
                });


        //Read users from DB
//        final FirebaseUser user = firebaseAuth.getCurrentUser();
//        final FirebaseUser user = firebaseAuth.getCurrentUser();
//            users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

        users_fire.addListenerForSingleValueEvent(new ValueEventListener() {
//        users_fire.getDatabase().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spetzs_local.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Spetz spetz = snapshot.child("0").getValue(Spetz.class);
//                        Spetz spetz = snapshot.getValue(Spetz.class);
                        //if occupation exists
                        if (spetz.getOccupation() != null)
                            spetzs_local.add(spetz);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                Toast.makeText(context,intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();
////                messageTv.setText(intent.getStringExtra("message"));
//            }
//        };
//
//        IntentFilter filter = new IntentFilter("message_received");
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);

        recyclerView.setAdapter(adapter);


        return view;
    }


}