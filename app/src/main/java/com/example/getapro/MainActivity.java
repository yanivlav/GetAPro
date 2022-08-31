package com.example.getapro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.getapro.Common.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

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

        //menu item has been chosen
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                drawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getTitle() == "Show Location"){
                    Intent intent = new Intent(MainActivity.this, Location.class);
                    startActivity(intent);
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
//            Toast.makeText(this, "On item pressed", Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}