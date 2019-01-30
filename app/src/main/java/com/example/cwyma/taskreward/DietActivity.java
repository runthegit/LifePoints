package com.example.cwyma.taskreward;

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

public class DietActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btnFruit;
    Button btnVeg;
    Button btnVitamins;
    Button btnWater;

    TextView lblFruit;
    TextView lblVeg;
    TextView lblVitamins;
    TextView lblWater;

    private String chkFruitClick;
    private String chkVitClick;
    private String chkVegClick;
    private String chkWaterClick;

    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnFruit = (Button) findViewById(R.id.btnDietFruit);
        btnVeg = (Button) findViewById(R.id.btnDietVeg);
        btnVitamins = (Button) findViewById(R.id.btnDietVit);
        btnWater = (Button) findViewById(R.id.btnDietAddWater);

        lblFruit = (TextView) findViewById(R.id.lblFruit);
        lblVeg = (TextView) findViewById(R.id.lblVeg);
        lblVitamins = (TextView) findViewById(R.id.lblVitamin);
        lblWater = (TextView) findViewById(R.id.lblWater);

        final String uid = getIntent().getExtras().getString("intentUserId");

        //--------------------------------------------------------------------------------------------
        //initial check for each database value
        //changes lbl colors and btn visibility accordingly
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String uidCheck = uid;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    String snapKey = objSnapshot.getKey();

                    if (snapKey.equalsIgnoreCase(uidCheck))
                    {

                        String fruitBtnChk = objSnapshot.child("fruit").getValue().toString();
                        String vitBtnChk = objSnapshot.child("vitamins").getValue().toString();
                        String vegBtnChk = objSnapshot.child("veg").getValue().toString();
                        String waterBtnChk = objSnapshot.child("water").getValue().toString();
                        String rewardTime = objSnapshot.child("reward").getValue().toString();
                        Toast.makeText(DietActivity.this, "Reward Time: " +  rewardTime + " minutes!", Toast.LENGTH_LONG).show();

                        if (fruitBtnChk.equalsIgnoreCase("click")){
                            lblFruit.setTextColor(Color.GREEN);
                            btnFruit.setVisibility(View.INVISIBLE);
                        }
                        if (vitBtnChk.equalsIgnoreCase("click")){
                            lblVitamins.setTextColor(Color.GREEN);
                            btnVitamins.setVisibility(View.INVISIBLE);
                        }
                        if (vegBtnChk.equalsIgnoreCase("click")){
                            lblVeg.setTextColor(Color.GREEN);
                            btnVeg.setVisibility(View.INVISIBLE);
                        }
                        if (waterBtnChk.equalsIgnoreCase("click")){
                            lblWater.setTextColor(Color.GREEN);
                            btnWater.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
            }
        });

//-----------------------------------------------------------------------------------------------------
        //each button has a onClickListener
        //gets the proper id matched user and then sends the proper info to the write method below
        //sets the llb color to green and changes btn visibility then tells the user the amount of reward time added
        btnFruit.setOnClickListener(new View.OnClickListener() {
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
                                    String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                    String rewardTime = objSnapshot.child("reward").getValue().toString();
                                    String vitChk = objSnapshot.child("vitamins").getValue().toString();
                                    String vegChk = objSnapshot.child("veg").getValue().toString();
                                    String waterChk = objSnapshot.child("water").getValue().toString();

                                    chkFruitClick= "click";

                                    writeNewDiet(uid, rewardTime, taskCnt, chkFruitClick, vegChk, vitChk, waterChk);

                                    lblFruit.setTextColor(Color.GREEN);
                                    btnFruit.setVisibility(View.INVISIBLE);

                                    uidCheck = "finished";
                                }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Reward Bank added 2 minutes!", Toast.LENGTH_LONG).show();

            }
        });

//---------------------------------------------------------------------------------------------------
        btnVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
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
                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String fruitChk = objSnapshot.child("fruit").getValue().toString();
                                String vitChk = objSnapshot.child("vitamins").getValue().toString();
                                String waterChk = objSnapshot.child("water").getValue().toString();

                                chkVegClick = "click";

                                writeNewDiet(uid, rewardTime, taskCnt, fruitChk, chkVegClick, vitChk, waterChk);

                                lblVeg.setTextColor(Color.GREEN);
                                btnVeg.setVisibility(View.INVISIBLE);

                                uidCheck = "finished";
                            }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                }
            });

                Toast.makeText(getApplicationContext(), "Reward Bank added 2 minutes!", Toast.LENGTH_LONG).show();
            }
        });
//------------------------------------------------------------------------------------------------------------------------
        btnVitamins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
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
                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String fruitChk = objSnapshot.child("fruit").getValue().toString();
                                String vegChk = objSnapshot.child("veg").getValue().toString();
                                String waterChk = objSnapshot.child("water").getValue().toString();

                                chkVitClick = "click";

                                writeNewDiet(uid, rewardTime, taskCnt, fruitChk, vegChk, chkVitClick, waterChk);

                                lblVitamins.setTextColor(Color.GREEN);
                                btnVitamins.setVisibility(View.INVISIBLE);

                                uidCheck = "finished";
                            }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                }
            });

                Toast.makeText(getApplicationContext(), "Reward Bank added 2 minutes!", Toast.LENGTH_LONG).show();
            }
        });
//------------------------------------------------------------------------------------------------
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
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

                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String fruitChk = objSnapshot.child("fruit").getValue().toString();
                                String vitChk = objSnapshot.child("vitamins").getValue().toString();
                                String vegChk = objSnapshot.child("veg").getValue().toString();

                                chkWaterClick = "click";

                                writeNewDiet(uid, rewardTime, taskCnt, fruitChk, vegChk, vitChk, chkWaterClick);

                                lblWater.setTextColor(Color.GREEN);
                                btnWater.setVisibility(View.INVISIBLE);

                                uidCheck = "finished";
                            }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                }
            });
                Toast.makeText(getApplicationContext(), "Reward Bank added 2 minutes!", Toast.LENGTH_LONG).show();

            }
        });
        //-------------------------------------------------------------------------------------------------------------------------
    } //<--end of onCreate
//---------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//----------------------------------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String intentUid = getIntent().getExtras().getString("intentUserId");

        if (id == R.id.nav_physical){

            Intent intent = new Intent(DietActivity.this, PhysicalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){

            Intent intent = new Intent(DietActivity.this, BadHabitActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){

            Intent intent = new Intent(DietActivity.this, DietActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){

            Intent intent = new Intent(DietActivity.this, MentalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){

            Intent intent = new Intent(DietActivity.this, CleaningActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){

            Intent intent = new Intent(DietActivity.this, RewardActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){

            Intent intent = new Intent(DietActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(DietActivity.this);
            builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                    .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Signs out!
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(DietActivity.this, SignInActivity.class));
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
    //------------------------------------------------------------------------------------------------------------
    private void writeNewDiet(String dbKey, String rTime, String tCount, String fruitClick, String vegClick, String vitClick, String waterClick) {
        //used to set the button clicks in the database and set reward time

        int taskCountNum = Integer.parseInt(tCount);
        int newTaskCnt = taskCountNum + 1;
        String newTaskCount = Integer.toString(newTaskCnt);

        int rewardTimeNum = Integer.parseInt(rTime);
        int newRewardTimeNum = rewardTimeNum + 2;
        String newRewardTime = Integer.toString(newRewardTimeNum);

        Map<String, Object> newDietValues = new HashMap<>();

        newDietValues.put("fruit", fruitClick);
        newDietValues.put("veg", vegClick);
        newDietValues.put("vitamins", vitClick);
        newDietValues.put("water", waterClick);
        newDietValues.put("taskcount", newTaskCount);
        newDietValues.put("reward", newRewardTime);

        mUsersRef.child(dbKey).updateChildren(newDietValues);
    }
    //--------------------------------------------------------------------------------------
}
