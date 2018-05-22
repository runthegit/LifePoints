package com.example.cwyma.lifepoints;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.content.Intent;



public class LifePointsApp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    // This lets the user go to the corresponding activity after clicking the menu item from the navbar.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        return false;
    }


}
