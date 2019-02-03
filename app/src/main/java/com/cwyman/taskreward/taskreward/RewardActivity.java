package com.cwyman.taskreward.taskreward;

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

public class RewardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnReward;
    TextView lblTimeBank, txtNumMins;

    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lblTimeBank = (TextView) findViewById(R.id.lblTimeBanked);
        txtNumMins = (TextView) findViewById(R.id.txtNumMins);
        btnReward = (Button) findViewById(R.id.btnRewardUse);
        final String uid = getIntent().getExtras().getString("intentUserId");

//-----------------------------------------------------------------------------------
        //tells the user the amount of reward time there is
        //listens for changes in reward time amount then tells the user the updated reward time
        mUsersRef.addValueEventListener(new ValueEventListener() {
            String uidCheck = uid;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    String snapKey = objSnapshot.getKey();
                    if (snapKey.equalsIgnoreCase(uidCheck))
                    {
                        lblTimeBank.setText(objSnapshot.child("reward").getValue().toString() + " minutes!");

                        if (objSnapshot.child("reward").getValue().toString().equalsIgnoreCase("1"))
                        {
                            lblTimeBank.setText(objSnapshot.child("reward").getValue().toString() + " minute!");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //-----------------------------------------------------------------------------------------

        //user clicks the reward button and checks if the user entered a number
        //sends
        btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtNumMins.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter an amount of minutes to use." , Toast.LENGTH_LONG).show();
                    txtNumMins.requestFocus();
                }

                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    String uidCheck = uid;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                        {
                            String snapKey = objSnapshot.getKey();
                            if (snapKey.equalsIgnoreCase(uidCheck))
                            {
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                writeNewReward(uid, rewardTime);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RewardActivity.this, "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }

                });
            }
        });
        //------------------------------------------------------------------------------------------------------------
    } // <--end of onCreate
//----------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String intentUid = getIntent().getExtras().getString("intentUserId");
            Intent intent = new Intent(RewardActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String intentUid = getIntent().getExtras().getString("intentUserId");

        if (id == R.id.nav_physical){

            Intent intent = new Intent(RewardActivity.this, PhysicalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){

            Intent intent = new Intent(RewardActivity.this, BadHabitActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){

            Intent intent = new Intent(RewardActivity.this, DietActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){

            Intent intent = new Intent(RewardActivity.this, MentalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){

            Intent intent = new Intent(RewardActivity.this, CleaningActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){

            Intent intent = new Intent(RewardActivity.this, RewardActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){

            Intent intent = new Intent(RewardActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(RewardActivity.this);
            builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                    .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Signs out!
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(RewardActivity.this, SignInActivity.class));
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

    //-------------------------------------------------------------------------------------------
    //checks if the user enters a valid reward time number
    //used to calculate new reward time and write the value into the database
    private void writeNewReward(String dbKey, String rTime) {

        final String rewardTimeEntry = txtNumMins.getText().toString().trim();

        if(!rewardTimeEntry.isEmpty())
        {
            int rewardTimeNum = Integer.parseInt(rTime);
            int newRewardTimeEntry = Integer.parseInt(rewardTimeEntry);

            if (newRewardTimeEntry <= rewardTimeNum)
            {
                newRewardTimeEntry = rewardTimeNum - newRewardTimeEntry;
                String newRewardTime = Integer.toString(newRewardTimeEntry);

                Map<String, Object> newRewardValues = new HashMap<>();

                newRewardValues.put("reward", newRewardTime);

                mUsersRef.child(dbKey).updateChildren(newRewardValues);
                txtNumMins.setText("");
            }else{
                Toast.makeText(getApplicationContext(), "You can't use more time than is in the time bank. Please re-enter the number." , Toast.LENGTH_LONG).show();
                txtNumMins.setText("");
                txtNumMins.requestFocus();

            }

        }

    }
    //-------------------------------------------------------------------------------------------------
}
