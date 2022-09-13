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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpetzList extends Fragment {

    ArrayList<Spetz> spetzs;
    ArrayList<Form> forms_user = new ArrayList<>();
    ArrayList<Form> forms_spetz = new ArrayList<>();

    BroadcastReceiver receiver;
    String TAG = "MyFirebaseInstanceIdService";

    String API_TOKEN_KEY = "AAAA8BK5-Gs:APA91bEHQfVvwoR62JRU2tdmdkTZcdckkftwsqxqE1NOmZCmfrkUFbzLHJQPuDkY69u3dh5aL_4s1u1AD1GTxhLHant-oUCuyw4cx-dEC-TJz_yFiPf6e1apu6FXwGKAekKnwqBBO6co";
    FirebaseMessaging messaging = FirebaseMessaging.getInstance();

    private String spetzCategory;
    private String address;
    private String occupation;
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
            occupation = getArguments().getString("category");




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

        //Read user forms
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




        adapter.setListener(new SpetzAdapter.ISpetzListener() {
            @Override
            public void onInfoClicked(int position, View view) {
//                Bundle bundle =  new Bundle();
//                bundle.putParcelable("spetzs", spetzs.get(position));//maybe the form class should implement parceble
//                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_spetzCard);

            }

            @Override
            public void onSpetzClicked(int position, View view){
                String selectedSpetzUid = spetzs_local.get(position).getUid();

                newForm = new Form(desc, pic, selectedSpetzUid,spetzCategory, address);

                //Add user new form to user form list
                forms_user.add(newForm);
                forms_fire.child(user.getUid()).setValue(forms_user);


                //Messaging stuff ----------Messaging stuff ----------Messaging stuff ----------Messaging stuff ----------


                messaging.unsubscribeFromTopic(selectedSpetzUid);
                messaging.subscribeToTopic(selectedSpetzUid);

                /*
                https://fcm.googleapis.com/fcm/send
                Content-Type:application/json
                Authorization:key=AIzaSyZ-1u...0GBYzPu7Udno5aA
                {
                     "to": "/topics/foo-bar", (OR:   "condition": "'dogs' in topics || 'cats' in topics",)
                    "data": {
                        "message": "This is a Firebase Cloud Messaging Topic Message!",
                    }
                }
                */

                //maybe to json and then reverse
//                String textToSend = newForm;

                final JSONObject rootObject  = new JSONObject();

                try {
                    rootObject.put("to", "/topics/" + selectedSpetzUid);
                    rootObject.put("data",new JSONObject().put("message", desc));
                    String url = "https://fcm.googleapis.com/fcm/send";

                //Was main activity
                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers = new HashMap<>();
                        headers.put("Content-Type","application/json");
                        headers.put("Authorization","key="+API_TOKEN_KEY);
                        return headers;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return rootObject.toString().getBytes();
                    }
                };
                queue.add(request);
                queue.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                messaging.unsubscribeFromTopic(selectedSpetzUid);







////                //Add the user form to the Spets form  list
//                if (user.getUid() != spetzs_local.get(position).getUid()){
//                    forms_spetz.add(newForm);
//                    forms_fire.child(spetzs_local.get(position).getUid()).setValue(forms_spetz);
////                    forms_fire.child(spetzs_local.get(position).getUid()).push().setValue(forms_spetz);
//                }
//                else Toast.makeText(getContext(), "can't open to yourself", Toast.LENGTH_SHORT).show();


                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_clientDashboard);
            }


            @Override
            public void onSpetzLongClicked(int position, View view) {

            }
        });

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            System.out.println("Fetching FCM registration token failed");
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        token = task.getResult();
//
//                        // Log and toast
//                        System.out.println(token);
//                        Log.d(TAG, "Refreshed token: " + token);
//                        Toast.makeText(getContext(), "Device Token is: " + token, Toast.LENGTH_SHORT).show();
////                        mtv.setText(token);
//                    }
//                });


        // Finds out who is a spetz and show it on adapter
        users_fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spetzs_local.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Spetz spetz = snapshot.child("0").getValue(Spetz.class);
//                        if (spetz.getOccupation()!=null && spetz.getOccupation().equals(occupation) && spetz.getDistrict().equals(district))
                        if (spetz.getOccupation()!=null && spetz.getOccupation().equals(occupation)) {
                            spetzs_local.add(spetz);
                            adapter.notifyDataSetChanged();
                        }
                    }
//                    adapter.notifyDataSetChanged();
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