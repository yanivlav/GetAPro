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
import com.example.getapro.MyObjects.User;
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
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    private String spetzCategory;
    private String address;
    private String occupation;
    private String district;
    private String desc;
    private int pic;
    private String realImage;
    private Form newForm;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ArrayList<Spetz> spetzs_local = new ArrayList<>();
    DatabaseReference users_fire = database.getReference("Users");
    DatabaseReference forms_fire = database.getReference("Forms");
    SpetzAdapter adapter;
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    User myuser;


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
            realImage = getArguments().getString("realImage");
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
                Bundle bundle =  new Bundle();
                bundle.putString("name", spetzs_local.get(position).getUserName());
                bundle.putString("district", spetzs_local.get(position).getDistrict());
                bundle.putString("number", spetzs_local.get(position).getNumber());
                bundle.putString("occupation", spetzs_local.get(position).getOccupation());
                bundle.putParcelable("spetzs", spetzs_local.get(position));//maybe the form class should implement parceble
                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_user_Dialog,bundle);

            }

            @Override
            public void onSpetzClicked(int position, View view){
                Spetz spetz = spetzs_local.get(position);
                String selectedSpetzUid = spetzs_local.get(position).getUid();
                Toast.makeText(getContext(), "We noted "+spetzs_local.get(position).getUserName()+" that you are looking for him!", Toast.LENGTH_SHORT).show();


                newForm = new Form(desc, realImage, selectedSpetzUid,spetzCategory, address, spetz.getNumber(),spetz.getUserName(),myuser.getNumber(),myuser.getUserName());


                //Add user new form to user form list
                forms_user.add(newForm);
                forms_fire.child(user.getUid()).setValue(forms_user);

                messaging.unsubscribeFromTopic(selectedSpetzUid);
                messaging.subscribeToTopic(selectedSpetzUid);
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


                Navigation.findNavController(view).navigate(R.id.action_spetzList_to_clientDashboard);
            }

            @Override
            public void onSpetzLongClicked(int position, View view) {

            }
        });

        // Finds out who is a spetz and show it on adapter
        users_fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StorageReference pathReference;

                spetzs_local.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Spetz spetz = snapshot.child("0").getValue(Spetz.class);
                        pathReference = storageReference.child("UsersProfilePhotos/" + spetz.getEmail() + ".jpg");
                        pathReference.getDownloadUrl();
                        if (!spetz.getUid().equals(user.getUid())) {//not show spetz to spetz
//                        if (spetz.getOccupation()!=null && spetz.getOccupation().equals(occupation) && spetz.getDistrict().equals(district))
                            if (spetz.getOccupation() != null && spetz.getOccupation().equals(occupation)) {
                                spetzs_local.add(spetz);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        if (user.getUid().equals(spetz.getUid()))
                        myuser = snapshot.child("0").getValue(User.class);


                    }
                    if (spetzs_local.size() == 0){
                        Toast.makeText(getContext(), "Sorry, no Spetz for your request! ", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_spetzList_to_clientDashboard);
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