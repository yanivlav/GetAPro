package com.example.getapro.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getapro.Helpers.FormAdapter;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.R;
import com.google.android.material.navigation.NavigationView;
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
import java.util.List;
import java.util.Map;

//public class ClientInquiries extends Fragment {
//
//    FormAdapter adapter;
//    ArrayList<Form> forms = new ArrayList<>();
//
//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    FirebaseAuth.AuthStateListener authStateListener;
//
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference users = database.getReference("users");
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.client_inquiries, container, false);
//
//        if (forms==null) {
//            forms = new ArrayList<>();
//            forms.add(new Form("alablabla", R.drawable.report_problem_icon));
//            forms.add(new Form("blablabla", R.drawable.report_problem_icon));
//            forms.add(new Form("clablabla", R.drawable.report_problem_icon));
//            forms.add(new Form("dlablabla", R.drawable.report_problem_icon));
//
//
////            spetzs.add(new Spetz("Bar", "North", "B@gmail.com", "123", 022346543, R.drawable.person_icon, "Foundations"));
////            spetzs.add(new Spetz("Yaniv", "North", "Y@gmail.com", "123", 022234543, R.drawable.person_icon, "Cleaner"));
////            spetzs.add(new Spetz("Eran", "North", "E@gmail.com", "123", 02765543, R.drawable.person_icon, "Shiatsu"));
////            spetzs.add(new Spetz("John", "North", "J@gmail.com", "123", 05454567, R.drawable.person_icon, "Surf instructor"));
//        }
//
//
//
//        RecyclerView recyclerView = view.findViewById(R.id.recycler);
//        adapter = new FormAdapter(forms);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setListener(new FormAdapter.IFormListener() {
//            @Override
//            public void onInfoClicked(int position, View view) {
//                Toast.makeText(getContext(), "open problem info card", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFormClicked(int position, View view) {
//                Form form = forms.get(position);
////                form.setCompleted(!form.isCompleted());
//                adapter.notifyItemChanged(position);
//
//                //update the database
//                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(forms);
//            }
//
//            @Override
//            public void onFormLongClicked(int position, View view) {
//                forms.remove(position);
//                adapter.notifyItemRemoved(position);
//
//                //update the database
//                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(forms);
//
//            }
//        });
//
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
////                View headerView  = navigationView.getHeaderView(0);
////                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);
//
//                final FirebaseUser user = firebaseAuth.getCurrentUser();
//
////                if(user != null) {//sign up or sign in
////
////                    if(fullName!=null)  { //sign up - update profile with full name
////
////                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////                                fullName = null;
////                                if(task.isSuccessful())
////                                    Snackbar.make(coordinatorLayout,user.getDisplayName() + " Welcome!!!",Snackbar.LENGTH_SHORT).show();
////                            }
////                        });
////                    }
////
////                    userTv.setText(user.getDisplayName() + " logged in");
////                    collapsing.setTitle(user.getDisplayName() + " missions");
////
////                    navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(false);
////                    navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(false);
////                    navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(true);
//
//                    //Read the user data base - missions
//
//                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
////                    progressDialog.setMessage("Loading " + user.getDisplayName() + " missions, please wait...");
//                    progressDialog.setMessage("Loading Bars missions, please wait...");
//                    progressDialog.show();
//
//                    users.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            forms.clear();
//
//                            if(dataSnapshot.exists()) {
//                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    Form form = snapshot.getValue(Form.class);
//                                    forms.add(form);
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//                            progressDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
////                }
////                else {
////                    userTv.setText("Please log in");
////                    collapsing.setTitle("Please log in");
////
////                    navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(true);
////                    navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(true);
////                    navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(false);
////
////                    missions.clear();
////                    adapter.notifyDataSetChanged();
////                }
//            }
//        };
////    }
//
//
//
//
//        return view;
//    }
//}


//
public class ClientInquiries extends Fragment {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    FormAdapter adapter;
    List<Form> forms = new ArrayList<>();

    String fullName;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        coordinatorLayout = findViewById(R.id.coordinator);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView  = getLayoutInflater().inflate(R.layout.sign_dialog,null);

                final EditText usernameEt = dialogView.findViewById(R.id.username_input);
                final EditText fullnameEt = dialogView.findViewById(R.id.fullname_input);
                final EditText passwordEt = dialogView.findViewById(R.id.password_input);

                switch (item.getItemId()) {

                    case R.id.item_sign_up:
                        builder.setView(dialogView).setPositiveButton("Register", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String username  = usernameEt.getText().toString();
                                fullName = fullnameEt.getText().toString();
                                String password = passwordEt.getText().toString();

                                //sign up the user
                                firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful())
                                            Snackbar.make(coordinatorLayout,"Sign up successful",Snackbar.LENGTH_SHORT).show();
                                        else
                                            Snackbar.make(coordinatorLayout,"Sign up failed",Snackbar.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).show();
                        break;
                    case R.id.item_sign_in:

                        fullnameEt.setVisibility(View.GONE);
                        builder.setView(dialogView).setPositiveButton("Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String username  = usernameEt.getText().toString();
                                String password = passwordEt.getText().toString();

                                //Sign in the user

                                firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful())
                                            Snackbar.make(coordinatorLayout,"Sign in successful",Snackbar.LENGTH_SHORT).show();
                                        else
                                            Snackbar.make(coordinatorLayout,"Sign in failed",Snackbar.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).show();
                        break;
                    case R.id.item_sign_out:
                        firebaseAuth.signOut();
                        break;
                }
                return false;
            }
        });


        //Add form--------------        //Add form--------------        //Add form--------------
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView  = getLayoutInflater().inflate(R.layout.form_dialog,null);

                final EditText editText = dialogView.findViewById(R.id.form_txt);
                builder.setView(dialogView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = editText.getText().toString();
                        forms.add(new Form(text,false));
                        adapter.notifyItemInserted(forms.size()-1);

                        //update the database
                        users.child(firebaseAuth.getCurrentUser().getUid()).setValue(forms);

                        Snackbar.make(coordinatorLayout,"Form added",Snackbar.LENGTH_LONG).show();
                    }
                }).show();
            }
            // Toast.makeText(MainActivity.this, "fab pressed", Toast.LENGTH_SHORT).show();
        });

        final CollapsingToolbarLayout collapsing  = findViewById(R.id.collapsing_layout);
        collapsing.setTitle("Please log in");

        RecyclerView recyclerView = findViewById(R.id.recycler);
        adapter = new FormAdapter(forms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new FormAdapter.FormListener() {
            @Override
            public void onFormClicked(int position, View view) {

                Form form = forms.get(position);
                form.setCompleted(!form.isCompleted());
                adapter.notifyItemChanged(position);

                //update the database
                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(forms);

            }

            @Override
            public void onFormLongClicked(int position, View view) {

                forms.remove(position);
                adapter.notifyItemRemoved(position);

                //update the database
                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(forms);

            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                View headerView  = navigationView.getHeaderView(0);
                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);

                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {//sign up or sign in

                    if(fullName!=null)  { //sign up - update profile with full name

                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullName = null;
                                if(task.isSuccessful())
                                    Snackbar.make(coordinatorLayout,user.getDisplayName() + " Welcome!!!",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                    userTv.setText(user.getDisplayName() + " logged in");
                    collapsing.setTitle(user.getDisplayName() + " forms");

                    navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(true);

                    //Read the user data base - forms

                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading " + user.getDisplayName() + " forms, please wait...");
                    progressDialog.show();

                    users.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            forms.clear();

                            if(dataSnapshot.exists()) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Form form = snapshot.getValue(Form.class);
                                    forms.add(form);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    userTv.setText("Please log in");
                    collapsing.setTitle("Please log in");

                    navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(false);

                    forms.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            //  Toast.makeText(this, "Home button pressed", Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }



}


public class ClientInquiries extends Fragment {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    FormAdapter adapter;
    List<Form> forms = new ArrayList<>();

    String fullName;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users = database.getReference("users");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_inquiries, container, false);

        return view;
    }
}