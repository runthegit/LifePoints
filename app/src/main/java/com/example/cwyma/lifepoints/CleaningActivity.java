package com.example.cwyma.lifepoints;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CleaningActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // This lets the user go to the corresponding activity after clicking the menu item from the navbar.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_physical){
            Intent i = new Intent(getApplicationContext(),PhysicalActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){
            Intent i = new Intent(getApplicationContext(),BadHabitActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){
            Intent i = new Intent(getApplicationContext(),DietActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){
            Intent i = new Intent(getApplicationContext(),MentalActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){
            Intent i = new Intent(getApplicationContext(),CleaningActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){
            Intent i = new Intent(getApplicationContext(),RewardActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(CleaningActivity.this);
            builder.setMessage("Are you sure you want to Sign Out?")
                    .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Signs out!
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(CleaningActivity.this, SignInActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return false;
    }
}
