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
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getapro.Fragments.LoginFragment;
import com.example.getapro.Fragments.SignupFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class MainActivity extends AppCompatActivity {//implements NavigationView.OnNavigationItemSelectedListener{

    String fullname;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseUser user;

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

//        if (savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new LoginFragment()).commit();
//            navigationView.setCheckedItem(R.id.login_btn);
//        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                if (!item.isChecked()){
                    item.setChecked(true);
                    Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    //if (user in skip)
                    switch (item.getItemId()) {
                        case R.id.item_signup:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
//                        Navigation.findNavController().navigate(R.id.action_signupFragment_to_clientDashboard,bundle);
//                        Navigation.findNavController().navigate(R.id.);
//                            Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_signupFragment);
                            break;
                        case R.id.item_login:
//                            Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment).navigate(R.id.action_signupFragment_to_loginFragment);
                            break;
                        case R.id.item_logout:
                            firebaseAuth.signOut();
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

                user = firebaseAuth.getCurrentUser();
                if (user != null) {//logon
                    if (fullname != null) {//signup = update profile with full name

                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullname).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullname = null;
                                if (task.isSuccessful())
                                    Snackbar.make(coordinatorLayout, user.getDisplayName() + "Welcome", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                    FragmentManager fragmentManager;//???????????????

                    userTv.setText("loggen in as " + user.getDisplayName());

                    navigationView.getMenu().findItem(R.id.item_login).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_signup).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_myInquries).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_logout).setVisible(true);
//
//                    read the user database

                } else {
                    userTv.setText("Welcome guest ,please login.");
                    //collapsing?
                    navigationView.getMenu().findItem(R.id.item_login).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_signup).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_myInquries).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_logout).setVisible(false);

//                    adapter.notifyDataSetChanged();

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


}



