package com.example.getapro.Fragments;

import android.annotation.SuppressLint;
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

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getapro.Helpers.SpetzAdapter;
import com.example.getapro.MainActivity;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.MyObjects.User;
import com.example.getapro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpetzList extends Fragment {

    ArrayList<Spetz> spetzs;
    ArrayList<Form> forms_local = new ArrayList<>();

    BroadcastReceiver receiver;
    String TAG = "MyFirebaseInstanceIdService";
    String token;

    String apiToken = "AAAA8BK5-Gs:APA91bEHQfVvwoR62JRU2tdmdkTZcdckkftwsqxqE1NOmZCmfrkUFbzLHJQPuDkY69u3dh5aL_4s1u1AD1GTxhLHant-oUCuyw4cx-dEC-TJz_yFiPf6e1apu6FXwGKAekKnwqBBO6co";
    FirebaseMessaging messaging = FirebaseMessaging.getInstance();


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ArrayList<Spetz> spetzs_local = new ArrayList<>();
    DatabaseReference users_fire = database.getReference("Users");
    DatabaseReference forms_fire = database.getReference("Forms");
    SpetzAdapter spetzAdapter;

    public static SpetzList newInstance(String param1, String param2) {
        SpetzList fragment = new SpetzList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.spetz_list, container, false);

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
//        messaging.unsubscribeFromTopic("A");
//        messaging.unsubscribeFromTopic("B");

//        if (spetzs==null) {
//            spetzs = new ArrayList<>();
//            spetzs.add(new Spetz("Bar", "North", "B@gmail.com", "022346543", R.drawable.person_icon, "Foundations"));
//            spetzs.add(new Spetz("Yaniv", "south", "Y@gmail.com", "022346543", R.drawable.person_icon, "Cleaner"));
//            spetzs.add(new Spetz("Eran", "North", "E@gmail.com", "022346543", R.drawable.person_icon, "Shiatsu"));
//            spetzs.add(new Spetz("John", "North", "J@gmail.com", "022346543", R.drawable.person_icon, "Surf instructor"));
//        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();
            users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    spetzs_local.clear();

                    if(dataSnapshot.exists()) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Spetz spetz1 = snapshot.getValue(Spetz.class);
                            spetzs_local.add(spetz1);
                        }
                        spetzAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        spetzAdapter = new SpetzAdapter(spetzs_local);
        spetzAdapter.setListener(new SpetzAdapter.ISpetzListener() {
            @Override
            public void onInfoClicked(int position, View view) {
                Bundle bundle =  new Bundle();
                bundle.putParcelable("spetzs", spetzs.get(position));//maybe the form class should implement parceble
                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_spetzCard);

            }

            @Override
            public void onSpetzClicked(int position, View view) {
                //1. read forms from database
                //2. add form to local forms array than push to database
                //3. send notification to the spetz

                //Add form--------------        //Add form--------------        //Add form--------------
                //pass with bundle the real user form to here
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                forms_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        forms_local.clear();
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Form form = snapshot.getValue(Form.class);
                                forms_local.add(form);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //update the database
                forms_local.add(new Form("yaniv", R.drawable.problem_icon));
                forms_fire.child(firebaseAuth.getCurrentUser().getUid()).setValue(forms_local);


//                Open Chat
                Toast.makeText(getContext(),"Sending the massage", Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putString("to", spetzs.get(position).getUserName());
//                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_clientInquiries, bundle);
            }


            @Override
            public void onSpetzLongClicked(int position, View view) {

            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(context,intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();
//                messageTv.setText(intent.getStringExtra("message"));
            }
        };

        IntentFilter filter = new IntentFilter("message_received");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);

        recyclerView.setAdapter(spetzAdapter);


        return view;
    }


}