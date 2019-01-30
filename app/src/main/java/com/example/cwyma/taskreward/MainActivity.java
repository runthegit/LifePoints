package com.example.cwyma.taskreward;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtBadCounter;
    TextView txtTaskCounter;
    TextView txtRewardTimeB;

    private DrawerLayout drawer;

    DatabaseReference mUser = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBadCounter = (TextView) findViewById(R.id.txtBadHabitCount);
        txtRewardTimeB = (TextView) findViewById(R.id.txtRewardTimeBank);
        txtTaskCounter = (TextView) findViewById(R.id.txtTaskCount);

        final String uid = getIntent().getExtras().getString("intentUserId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//-------------------------------------------------------------------------------------------------------------------
        //checks the internet connection status and lets the user know if they are offline
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected)
                {
                    Toast.makeText(getApplicationContext(), "App is not connected to the internet. This app will work offline but needs an internet connection to save data.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getApplicationContext(), "Canceled Connection test!", Toast.LENGTH_LONG).show();
            }

        });
//---------------------------------------------------------------------------------------------------------------------------
//displays the completed and bad task counts along with the reward time
        mUser.addValueEventListener(new ValueEventListener() {
            String uidCheck = uid;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    String snapKey = objSnapshot.getKey();
                    if (snapKey.equalsIgnoreCase(uidCheck))
                    {
                        txtBadCounter.setText(objSnapshot.child("badcount").getValue().toString());
                        txtRewardTimeB.setText(objSnapshot.child("reward").getValue().toString() + " mins.");
                        txtTaskCounter.setText(objSnapshot.child("taskcount").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//------------------------------------------------------------------------------------------------------------
    } //<----end bracket of onCreate()
//------------------------------------------------------------------------------------------------------------
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

        String intentUid = getIntent().getExtras().getString("intentUserId");

        int id = item.getItemId();

        if (id == R.id.nav_physical){

            Intent intent = new Intent(MainActivity.this, PhysicalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){

            Intent intent = new Intent(MainActivity.this, BadHabitActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){

            Intent intent = new Intent(MainActivity.this, DietActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){

            Intent intent = new Intent(MainActivity.this, MentalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){

            Intent intent = new Intent(MainActivity.this, CleaningActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){

            Intent intent = new Intent(MainActivity.this, RewardActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                    .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Signs out!
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
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



