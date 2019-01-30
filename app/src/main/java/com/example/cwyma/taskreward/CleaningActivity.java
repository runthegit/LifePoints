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


public class CleaningActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnDishes;
    Button btnFloors;
    Button btnLaundry;
    Button btnTeeth;

    TextView lblDishes;
    TextView lblFloors;
    TextView lblLaundry;
    TextView lblTeeth;

    private String chkDishClick;
    private String chkFloorClick;
    private String chkLaundryClick;
    private String chkTeethClick;

    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

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

        btnDishes =  (Button) findViewById(R.id.btnCleanDish);
        btnFloors = (Button) findViewById(R.id.btnCleanFloor);
        btnLaundry = (Button) findViewById(R.id.btnCleanLaundry);
        btnTeeth = (Button) findViewById(R.id.btnCleanTeeth);

        lblDishes = (TextView) findViewById(R.id.lblDishes);
        lblFloors = (TextView) findViewById(R.id.lblFloors);
        lblLaundry = (TextView) findViewById(R.id.lblLaundry);
        lblTeeth = (TextView) findViewById(R.id.lblTeeth);

        final String uid = getIntent().getExtras().getString("intentUserId");

//--------------------------------------------------------------------------------------------
        //checks to see if the buttons were already clicked to change label color and button visibility accordingly
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

                    String floorBtnChk = objSnapshot.child("floors").getValue().toString();
                    String laundryBtnChk = objSnapshot.child("laundry").getValue().toString();
                    String teethBtnChk = objSnapshot.child("teeth").getValue().toString();
                    String dishBtnChk = objSnapshot.child("dishes").getValue().toString();

                    String rewardTime = objSnapshot.child("reward").getValue().toString();
                    Toast.makeText(CleaningActivity.this, "Reward Time: " +  rewardTime + " minutes!", Toast.LENGTH_LONG).show();

                    if (dishBtnChk.equalsIgnoreCase("click")){
                        lblDishes.setTextColor(Color.GREEN);
                        btnDishes.setVisibility(View.INVISIBLE);
                    }
                    if (floorBtnChk.equalsIgnoreCase("click")){
                        lblFloors.setTextColor(Color.GREEN);
                        btnFloors.setVisibility(View.INVISIBLE);
                    }
                    if (laundryBtnChk.equalsIgnoreCase("click")){
                        lblLaundry.setTextColor(Color.GREEN);
                        btnLaundry.setVisibility(View.INVISIBLE);
                    }
                    if (teethBtnChk.equalsIgnoreCase("click")){
                        lblTeeth.setTextColor(Color.GREEN);
                        btnTeeth.setVisibility(View.INVISIBLE);
                    }
                }

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
        }
    });


//----------------------------------------------------------------
// next statements deal with each button click, sends values to the write method, displays info for the user
        btnDishes.setOnClickListener(new View.OnClickListener() {

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
                                    String floorChk = objSnapshot.child("floors").getValue().toString();
                                    String laundryChk = objSnapshot.child("laundry").getValue().toString();
                                    String teethChk = objSnapshot.child("teeth").getValue().toString();

                                    chkDishClick = "click";

                                    writeNewCleaning(uid, rewardTime, taskCnt, chkDishClick, floorChk, laundryChk, teethChk);

                                    lblDishes.setTextColor(Color.GREEN);
                                    btnDishes.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();

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

                }
                });

//----------------------------------------------------------------
        btnFloors.setOnClickListener(new View.OnClickListener() {
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
                                String dishChk = objSnapshot.child("dishes").getValue().toString();
                                String laundryChk = objSnapshot.child("laundry").getValue().toString();
                                String teethChk = objSnapshot.child("teeth").getValue().toString();
                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();

                                chkFloorClick = "click";

                                writeNewCleaning(uid, rewardTime, taskCnt, dishChk, chkFloorClick, laundryChk, teethChk);

                                btnFloors.setVisibility(View.INVISIBLE);
                                lblFloors.setTextColor(Color.GREEN);
                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();

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


            }
        });

//----------------------------------------------------------------
        btnLaundry.setOnClickListener(new View.OnClickListener() {
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
                                String floorChk = objSnapshot.child("floors").getValue().toString();
                                String dishChk = objSnapshot.child("dishes").getValue().toString();
                                String teethChk = objSnapshot.child("teeth").getValue().toString();
                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();

                                chkLaundryClick = "click";

                                writeNewCleaning(uid, rewardTime, taskCnt, dishChk, floorChk, chkLaundryClick, teethChk);

                                btnLaundry.setVisibility(View.INVISIBLE);
                                lblLaundry.setTextColor(Color.GREEN);
                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();

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


            }
        });
//----------------------------------------------------------------
        btnTeeth.setOnClickListener(new View.OnClickListener() {
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
                                String floorChk = objSnapshot.child("floors").getValue().toString();
                                String laundryChk = objSnapshot.child("laundry").getValue().toString();
                                String dishChk = objSnapshot.child("dishes").getValue().toString();
                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();

                                chkTeethClick = "click";

                                writeNewCleaning(uid, rewardTime, taskCnt, dishChk, floorChk, laundryChk, chkTeethClick);

                                btnTeeth.setVisibility(View.INVISIBLE);
                                lblTeeth.setTextColor(Color.GREEN);
                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();

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


            }
        });
        //----------------------------------------------------------------
    } //<--end of onCreate
//-------------------------------------------------------------------------------------------------------------------------
    @Override
   public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
       } else {
            super.onBackPressed();
        }
    }

    //--------------------------------------------------------------------------------
    // This lets the user go to the corresponding activity after clicking the menu item from the navbar.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String intentUid = getIntent().getExtras().getString("intentUserId");

        if (id == R.id.nav_physical){

            Intent intent = new Intent(CleaningActivity.this, PhysicalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){

            Intent intent = new Intent(CleaningActivity.this, BadHabitActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){

            Intent intent = new Intent(CleaningActivity.this, DietActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){

            Intent intent = new Intent(CleaningActivity.this, MentalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){

            Intent intent = new Intent(CleaningActivity.this, CleaningActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){

            Intent intent = new Intent(CleaningActivity.this, RewardActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){

            Intent intent = new Intent(CleaningActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(CleaningActivity.this);
            builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                    .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
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
    //------------------------------------------------------------------------------------------------------------
    private void writeNewCleaning(String dbKey, String rTime, String tCount, String dishClick, String floorClick,String laundryClick, String teethClick) {
        //used to set the button clicks in the database and set reward time

        int taskCountNum = Integer.parseInt(tCount);
        int newTaskCnt = taskCountNum + 1;
        String newTaskCount = Integer.toString(newTaskCnt);

        int rewardTimeNum = Integer.parseInt(rTime);
        int newRewardTimeNum = rewardTimeNum + 2;
        String newRewardTime = Integer.toString(newRewardTimeNum);

        Map<String, Object> newCleaningValues = new HashMap<>();

        newCleaningValues.put("dishes", dishClick);
        newCleaningValues.put("floors", floorClick);
        newCleaningValues.put("laundry", laundryClick);
        newCleaningValues.put("teeth", teethClick);
        newCleaningValues.put("taskcount", newTaskCount);
        newCleaningValues.put("reward", newRewardTime);

        mUsersRef.child(dbKey).updateChildren(newCleaningValues);
    }
    //--------------------------------------------------------------------------------------
}
