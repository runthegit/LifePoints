package com.cwyman.taskreward.taskreward;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class BadHabitActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnSmoking;
    Button btnAlcohol;
    Button btnSoda;
    Button btnChips;

    TextView lblSmoke;
    TextView lblAlcohol;
    TextView lblSoda;
    TextView lblChips;

    private String rTime;
    private String bCount;

    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_habit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnSmoking = (Button) findViewById(R.id.btnBadSmoke);
        btnAlcohol = (Button) findViewById(R.id.btnBadAl);
        btnSoda = (Button) findViewById(R.id.btnBadSoda);
        btnChips = (Button) findViewById(R.id.btnBadChip);

        lblSmoke = (TextView) findViewById(R.id.lblSmoke);
        lblAlcohol = (TextView) findViewById(R.id.lblAlcohol);
        lblSoda = (TextView) findViewById(R.id.lblSoda);
        lblChips = (TextView) findViewById(R.id.lblChips);

        final String uid = getIntent().getExtras().getString("intentUserId");

//------------------------------------------------------------------------------------------------------------------------
        //checks if there is any reward time to subtract from
        //tells the user how much reward time there is
        //if there is not reward time left then changes the label color and button visibility
        mUsersRef.addValueEventListener(new ValueEventListener()
        {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 String uidCheck = uid;

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    String snapKey = objSnapshot.getKey();

                    if (snapKey.equalsIgnoreCase(uidCheck)) {

                        String rewardTime = objSnapshot.child("reward").getValue().toString();
                        Toast.makeText(BadHabitActivity.this, "Reward Time: " +  rewardTime + " minutes!", Toast.LENGTH_LONG).show();

                        // if statement checking reward time being 0 sets button visibility and label text color
                        if(rewardTime.equalsIgnoreCase("0")){

                            btnChips.setVisibility(View.INVISIBLE);
                            lblChips.setTextColor(Color.RED);

                            btnSoda.setVisibility(View.INVISIBLE);
                            lblSoda.setTextColor(Color.RED);

                            btnSmoking.setVisibility(View.INVISIBLE);
                            lblSmoke.setTextColor(Color.RED);

                            btnAlcohol.setVisibility(View.INVISIBLE);
                            lblAlcohol.setTextColor(Color.RED);

                        }
                    }
                }
        }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError)
             {

             }
});

//-----------------------------------------------------------------------------------------
        //every button has an onClickListener to retrieve reward time and bad habit count
        //sends the data it retrieves to the write method below to change the database
        btnSmoking.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
                {

                    String uidCheck = uid;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {

                        for (DataSnapshot objSnapshot : snapshot.getChildren())
                        {
                            String snapKey = objSnapshot.getKey();

                                if (snapKey.equalsIgnoreCase(uidCheck))
                                {
                                    String rewardTime = objSnapshot.child("reward").getValue().toString();
                                    String badCount = objSnapshot.child("badcount").getValue().toString();

                                    writeNewBad(uid, rewardTime, badCount);
                                    uidCheck = "finished";

                                }

                            }
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(BadHabitActivity.this, "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
//-----------------------------------------------------------------------------------------
        btnAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
                {

                    String uidCheck = uid;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {

                        for (DataSnapshot objSnapshot : snapshot.getChildren())
                        {
                            String snapKey = objSnapshot.getKey();

                            if (snapKey.equalsIgnoreCase(uidCheck))
                            {
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String badCount = objSnapshot.child("badcount").getValue().toString();

                                writeNewBad(uid, rewardTime, badCount);
                                uidCheck = "finished";

                            }

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(BadHabitActivity.this, "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
//-----------------------------------------------------------------------------------------
        btnSoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
                {

                    String uidCheck = uid;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {

                        for (DataSnapshot objSnapshot : snapshot.getChildren())
                        {
                            String snapKey = objSnapshot.getKey();

                            if (snapKey.equalsIgnoreCase(uidCheck))
                            {
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String badCount = objSnapshot.child("badcount").getValue().toString();

                                writeNewBad(uid, rewardTime, badCount);
                                uidCheck = "finished";

                            }

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(BadHabitActivity.this, "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
//-----------------------------------------------------------------------------------------
        btnChips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
                {

                    String uidCheck = uid;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {

                        for (DataSnapshot objSnapshot : snapshot.getChildren())
                        {
                            String snapKey = objSnapshot.getKey();

                            if (snapKey.equalsIgnoreCase(uidCheck))
                            {
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String badCount = objSnapshot.child("badcount").getValue().toString();

                                writeNewBad(uid, rewardTime, badCount);
                                uidCheck = "finished";

                            }

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(BadHabitActivity.this, "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        //--------------------------------------------------------------------------------------------------------
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String intentUid = getIntent().getExtras().getString("intentUserId");
            Intent intent = new Intent(BadHabitActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
    }

    // This lets the user go to the corresponding activity after clicking the menu item from the navbar.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        String intentUid = getIntent().getExtras().getString("intentUserId");

        if (id == R.id.nav_physical){

            Intent intent = new Intent(BadHabitActivity.this, PhysicalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){

            Intent intent = new Intent(BadHabitActivity.this, BadHabitActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){

            Intent intent = new Intent(BadHabitActivity.this, DietActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){

            Intent intent = new Intent(BadHabitActivity.this, MentalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){

            Intent intent = new Intent(BadHabitActivity.this, CleaningActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){

            Intent intent = new Intent(BadHabitActivity.this, RewardActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){

            Intent intent = new Intent(BadHabitActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(BadHabitActivity.this);
            builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                    .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Signs out!
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(BadHabitActivity.this, SignInActivity.class));
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
    //-----------------------------------------------------------------------------------------------
    private void writeNewBad(String dbKey, String rTime, String bCount) {
        //used to set the bad habit count and reward time in the database
        //displays a message letting the user know how much reward time is left

        int badCountNum = Integer.parseInt(bCount) + 1;
        String newBadCount = Integer.toString(badCountNum);

        String newRewardTime = rTime;
        int rewardTimeNum = Integer.parseInt(rTime);

        if (rewardTimeNum >= 2){
            newRewardTime = Integer.toString((rewardTimeNum - 2));
        }else if(rewardTimeNum < 2){
            newRewardTime = "0";
        };

        Map<String, Object> newBadValues = new HashMap<>();

        newBadValues.put("badcount", newBadCount);
        newBadValues.put("reward", newRewardTime);

        mUsersRef.child(dbKey).updateChildren(newBadValues);
        Toast.makeText(BadHabitActivity.this, "Reward Time: " +  newRewardTime + " minutes left!", Toast.LENGTH_LONG).show();

    }
    //------------------------------------------------------------------------
}
