package com.example.getapro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getapro.Common.Location;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    String fullname;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        coordinatorLayout = findViewById(R.id.coordinator);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                View headerView = navigationView.getHeaderView(0);
                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){//logon
                    if(fullname!= null) {//signup = update profile with full name

                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullname).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullname = null;
                                if (task.isSuccessful())
                                    Snackbar.make(coordinatorLayout,user.getDisplayName() + "welcome", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                    userTv.setText(user.getDisplayName() + "logged in");

                    navigationView.getMenu().findItem(R.id.item_login).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_signup).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_logout).setVisible(true);

                    //read the user database

                }
                else{
                    userTv.setText("please Login");
                    //collapsing?
                    navigationView.getMenu().findItem(R.id.item_login).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_signup).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_logout).setVisible(false);

                    //adapter.notifyDataSetChanged();

                }
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //menu item has been chosen
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                drawerLayout.closeDrawers();
//                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();

                //try to change to fragmentDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.sign_dialog,null);


                EditText usernameEt = dialogView.findViewById(R.id.username_input);
                EditText fullnameEt = dialogView.findViewById(R.id.fullname_input);
                EditText passwordEt = dialogView.findViewById(R.id.password_input);

                switch (item.getItemId()){
                    case R.id.item_signup:
                        builder.setView(dialogView).setPositiveButton("Register", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String username = usernameEt.getText().toString();
                                fullname = fullnameEt.getText().toString();
                                String password = passwordEt.getText().toString();

                                //signup to user
                                firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                            Snackbar.make(coordinatorLayout,"Signup success",Snackbar.LENGTH_SHORT);
                                        else
                                            Snackbar.make(coordinatorLayout,"Signup failed",Snackbar.LENGTH_SHORT);
                                    }
                                });
                            }
                        }).show();
                        break;

                    case R.id.item_login:
                        fullnameEt.setVisibility(View.GONE);
                        builder.setView(dialogView).setPositiveButton("Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String username = usernameEt.getText().toString();
                                String password = passwordEt.getText().toString();

                                //login to user
                                firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                            Snackbar.make(coordinatorLayout,"Signup success",Snackbar.LENGTH_SHORT);
                                        else
                                            Snackbar.make(coordinatorLayout,"Signup failed",Snackbar.LENGTH_SHORT);
                                    }
                                });
                            }

                        }).show();
                        break;

                    case R.id.item_logout:
                        firebaseAuth.signOut();
                        break;
                }



                return true;
            }
        });

        //action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "pressed", Toast.LENGTH_SHORT).show();
                Snackbar.make(coordinatorLayout,"im a Snackbar",Snackbar.LENGTH_LONG).setAction("action", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "sneakbar action pressed", Toast.LENGTH_SHORT).show();

                    }
                }).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Toast.makeText(this, "On item pressed", Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}