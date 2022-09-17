package com.example.getapro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.getapro.Fragments.ClientDashboard;
import com.example.getapro.Fragments.LoginFragment;
import com.example.getapro.Fragments.SignupFragment;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.MyObjects.User;
import com.example.getapro.Services.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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


public class MainActivity extends AppCompatActivity {//implements NavigationView.OnNavigationItemSelectedListener{

    String fullname;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;


    String path ;
    ImageView userPic;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

//    StorageReference pathReference = problemImagesRef.getDownloadUrl();





    final String API_TOKEN_KEY = "AAAA5liTr20:APA91bFEfcKcX8g78mJnO-NPtkn1FSYo7aCXa8-bsfKxYQyG75KUE1asONXC6qkA_jodeMatug7Sreud-dRXOBi7O9uxK7cz_Vl0YQyBF3oix5EG5JGXWbTVhoiwe1qDeAgpfm5Q3_49";
    FirebaseMessaging messaging = FirebaseMessaging.getInstance();
    BroadcastReceiver receiver;


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
                if (!item.isChecked()){
                    item.setChecked(true);
                    Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    //if (user in skip)
                    NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment);
                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.my_nav,true).build();

                    switch (item.getItemId()) {
                        case R.id.item_signup:
                            navController.navigate(R.id.signupFragment,null,navOptions);
                            break;
                        case R.id.item_login:
                            navController.navigate(R.id.loginFragment,null,navOptions);
                            break;
                        case R.id.item_logout:
                            messaging.unsubscribeFromTopic(user.getUid());
                            firebaseAuth.signOut();
                            navController.navigate(R.id.clientDashboard,null,navOptions);
                            break;
                        case R.id.item_myInquries:
                            navController.navigate(R.id.clientInquiries,null,navOptions);
                            break;
                        case R.id.item_myRequests:
                            navController.navigate(R.id.spetsRequests,null,navOptions);
                            break;
                        case R.id.item_home:
                            navController.navigate(R.id.clientDashboard,null,navOptions);
                            break;
                    }
                }
                return true;
            }
        });



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                View headerView = navigationView.getHeaderView(0);
                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);
                userPic = headerView.findViewById(R.id.user_pic);
                user = firebaseAuth.getCurrentUser();

                if (user != null) {//logon

//                    messaging.unsubscribeFromTopic(user.getUid());
//                    messaging.subscribeToTopic(user.getUid());
                    path = user.getEmail()+".jpg";


                    if (fullname != null) {//sign in - update profile with full name

                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullname).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullname = null;
                                if (task.isSuccessful()){
                                    Snackbar.make(coordinatorLayout, user.getDisplayName() + "Welcome", Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }

                    userTv.setText(getString(R.string.loggen_in_as) + user.getEmail());


                    StorageReference pathReference;
                    String path = "UsersProfilePhotos/"+user.getEmail()+".jpg";
//                    String path = "UsersProfilePhotos/Yaniv@gmail.com.jpg";
                    pathReference = storageReference.child(path);
                    pathReference.getDownloadUrl();


                    storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(MainActivity.this).load(uri).into(userPic);
                        }
                    });


                    navigationView.getMenu().findItem(R.id.item_login).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_signup).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_myInquries).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_logout).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_home).setChecked(true);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference users_fire = database.getReference("Users");
                    ArrayList<User> users_local = new ArrayList<>();
                    users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            users_local.clear();
                            if(dataSnapshot.exists()) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Spetz spetz = snapshot.getValue(Spetz.class);
                                    if (spetz.getOccupation() != null){
                                        messaging.unsubscribeFromTopic(user.getUid());
                                        messaging.subscribeToTopic(user.getUid());
                                        navigationView.getMenu().findItem(R.id.item_myRequests).setVisible(true);
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    userTv.setText(R.string.welcome_guest);
//                    Glide.with(MainActivity.this).load(R.drawable.problem_icon).into(userPic);
                    userPic.setImageResource(R.drawable.handymanlogo01);
                    navigationView.getMenu().findItem(R.id.item_login).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_signup).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_myInquries).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_logout).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_myRequests).setVisible(false);
                    if ( navigationView.getCheckedItem() == navigationView.getMenu().findItem(R.id.item_logout))
                        navigationView.getMenu().findItem(R.id.item_home).setChecked(true);

                }
            }

        };


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}



