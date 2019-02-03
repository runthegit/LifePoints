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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MentalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btnRead;
    Button btnMeditate;
    Button btnCoding;
    Button btnStudy;

    TextView lblRead;
    TextView lblMeditate;
    TextView lblCoding;
    TextView lblStudy;

    private String chkCodingClick;
    private String chkMeditateClick;
    private String chkReadClick;
    private String chkStudyClick;

    DatabaseReference mUsersRef = getInstance().getReference("Users");

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,  R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnRead = (Button) findViewById(R.id.btnMentalRead);
        btnMeditate = (Button) findViewById(R.id.btnMentalMeditate);
        btnCoding = (Button) findViewById(R.id.btnMentalCoding);
        btnStudy = (Button) findViewById(R.id.btnMentalStudy);

        lblRead = (TextView) findViewById(R.id.lblRead);
        lblMeditate = (TextView) findViewById(R.id.lblMeditate);
        lblCoding = (TextView) findViewById(R.id.lblCoding);
        lblStudy = (TextView) findViewById(R.id.lblStudy);

        final String uid = getIntent().getExtras().getString("intentUserId");

//-----------------------------------------------------------------------------------------------------
        //checks if the database sees already accomplished each task
        //sets the labels to green, buttons visibility and displays reward time for user

        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String uidCheck = uid;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    String snapKey = objSnapshot.getKey();

                    if (snapKey.equalsIgnoreCase(uidCheck)) {

                        String readBtnChk = objSnapshot.child("read").getValue().toString();
                        String meditateBtnChk = objSnapshot.child("meditate").getValue().toString();
                        String codingBtnChk = objSnapshot.child("coding").getValue().toString();
                        String studyBtnChk = objSnapshot.child("study").getValue().toString();

                        String rewardTime = objSnapshot.child("reward").getValue().toString();
                        Toast.makeText(MentalActivity.this, "Reward Time: " +  rewardTime + " minutes!", Toast.LENGTH_LONG).show();

                        if (readBtnChk.equalsIgnoreCase("click")) {
                            lblRead.setTextColor(Color.GREEN);
                            btnRead.setVisibility(View.INVISIBLE);
                        }
                        if (meditateBtnChk.equalsIgnoreCase("click")) {
                            lblMeditate.setTextColor(Color.GREEN);
                            btnMeditate.setVisibility(View.INVISIBLE);
                        }
                        if (codingBtnChk.equalsIgnoreCase("click")) {
                            lblCoding.setTextColor(Color.GREEN);
                            btnCoding.setVisibility(View.INVISIBLE);
                        }
                        if (studyBtnChk.equalsIgnoreCase("click")) {
                            lblStudy.setTextColor(Color.GREEN);
                            btnStudy.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
            }
        });

//-----------------------------------------------------------------------------------------
                    //each button has an onClickListener that gets database values and sets the button clicked value accordingly
                    //sets the label to green, button visibility and tells the user updated reward time

                    btnRead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                String uidCheck = uid;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot objSnapshot : snapshot.getChildren())
                                    {

                                        String snapKey = objSnapshot.getKey();

                                            if (snapKey.equalsIgnoreCase(uidCheck)) {

                                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                                String meditateChk = objSnapshot.child("meditate").getValue().toString();
                                                String codingChk = objSnapshot.child("coding").getValue().toString();
                                                String studyChk = objSnapshot.child("study").getValue().toString();

                                                chkReadClick = "click";

                                                writeNewMental(uid, rewardTime, taskCnt, chkReadClick, meditateChk, codingChk, studyChk);
                                                btnRead.setVisibility(View.INVISIBLE);
                                                lblRead.setTextColor(Color.GREEN);

                                                uidCheck = "finished";
                                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();
                                            }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    });

//----------------------------------------------------------
                    btnMeditate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                String uidCheck = uid;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot objSnapshot : snapshot.getChildren())
                                    {
                                        String snapKey = objSnapshot.getKey();

                                            if (snapKey.equalsIgnoreCase(uidCheck)) {

                                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                                String readChk = objSnapshot.child("read").getValue().toString();
                                                String codingChk = objSnapshot.child("coding").getValue().toString();
                                                String studyChk = objSnapshot.child("study").getValue().toString();

                                                chkMeditateClick = "click";

                                                writeNewMental(uid, rewardTime, taskCnt, readChk, chkMeditateClick, codingChk, studyChk);
                                                btnMeditate.setVisibility(View.INVISIBLE);
                                                lblMeditate.setTextColor(Color.GREEN);

                                                uidCheck = "finished";
                                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();
                                            }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                                }

                            });
                        }
                    });
//---------------------------------------------------------------
                    btnCoding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                String uidCheck = uid;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot objSnapshot : snapshot.getChildren())
                                    {
                                        String snapKey = objSnapshot.getKey();

                                            if (snapKey.equalsIgnoreCase(uidCheck))
                                            {
                                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                                String readChk = objSnapshot.child("read").getValue().toString();
                                                String studyChk = objSnapshot.child("study").getValue().toString();
                                                String meditateChk = objSnapshot.child("meditate").getValue().toString();

                                                chkCodingClick = "click";

                                                writeNewMental(uid, rewardTime, taskCnt, readChk, meditateChk, chkCodingClick, studyChk);
                                                btnCoding.setVisibility(View.INVISIBLE);
                                                lblCoding.setTextColor(Color.GREEN);

                                                uidCheck = "finished";
                                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();
                                            }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
//------------------------------------------------------------

                    btnStudy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                String uidCheck = uid;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot objSnapshot : snapshot.getChildren())
                                    {
                                        String snapKey = objSnapshot.getKey();

                                            if (snapKey.equalsIgnoreCase(uidCheck))
                                            {

                                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                                String readChk = objSnapshot.child("read").getValue().toString();
                                                String codingChk = objSnapshot.child("coding").getValue().toString();
                                                String meditateChk = objSnapshot.child("meditate").getValue().toString();

                                                chkStudyClick = "click";

                                                writeNewMental(uid, rewardTime, taskCnt, readChk, meditateChk,codingChk, chkStudyClick);
                                                btnStudy.setVisibility(View.INVISIBLE);
                                                lblStudy.setTextColor(Color.GREEN);

                                                uidCheck = "finished";
                                                Toast.makeText(getApplicationContext(), "Reward Time is " + (Integer.parseInt(rewardTime) + 2 ) + " minutes!", Toast.LENGTH_LONG).show();
                                            }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                                }

                            });

                        }
                    });
                    //----------------------------------------------------------------------------------------------
    } //<--end of onCreate
//---------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String intentUid = getIntent().getExtras().getString("intentUserId");
            Intent intent = new Intent(MentalActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
    }

                @Override
                public boolean onNavigationItemSelected (@NonNull MenuItem item){
                    int id = item.getItemId();
                    String intentUid = getIntent().getExtras().getString("intentUserId");

                    if (id == R.id.nav_physical) {

                        Intent intent = new Intent(MentalActivity.this, PhysicalActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_physical);
                    }
                    if (id == R.id.nav_bad) {

                        Intent intent = new Intent(MentalActivity.this, BadHabitActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_bad_habit);
                    }
                    if (id == R.id.nav_diet) {

                        Intent intent = new Intent(MentalActivity.this, DietActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_diet);
                    }
                    if (id == R.id.nav_mental) {

                        Intent intent = new Intent(MentalActivity.this, MentalActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_mental);
                    }
                    if (id == R.id.nav_cleaning) {

                        Intent intent = new Intent(MentalActivity.this, CleaningActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_cleaning);
                    }
                    if (id == R.id.nav_reward) {

                        Intent intent = new Intent(MentalActivity.this, RewardActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_reward);
                    }
                    if (id == R.id.nav_main) {

                        Intent intent = new Intent(MentalActivity.this, MainActivity.class);
                        intent.putExtra("intentUserId", intentUid);
                        startActivity(intent);
                        setContentView(R.layout.activity_main);
                    }

                    if (id == R.id.nav_logout) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MentalActivity.this);
                        builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                                .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Signs out!
                                        FirebaseAuth.getInstance().signOut();
                                        finish();
                                        startActivity(new Intent(MentalActivity.this, SignInActivity.class));
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
    private void writeNewMental(String dbKey, String rTime, String tCount, String readClick, String meditateClick, String codingClick, String studyClick ) {
        //used to set the button clicks in the database and set reward time

        int newTaskCnt =  Integer.parseInt(tCount) + 1;
        String newTaskCount = Integer.toString(newTaskCnt);

        int newRewardTimeNum = Integer.parseInt(rTime) + 2;
        String newRewardTime = Integer.toString(newRewardTimeNum);

        Map<String, Object> newMentalValues = new HashMap<>();

        newMentalValues.put("read", readClick);
        newMentalValues.put("meditate", meditateClick);
        newMentalValues.put("coding", codingClick);
        newMentalValues.put("study", studyClick);
        newMentalValues.put("taskcount", newTaskCount);
        newMentalValues.put("reward", newRewardTime);

        mUsersRef.child(dbKey).updateChildren(newMentalValues);
    }
    //--------------------------------------------------------------------------------------
}
