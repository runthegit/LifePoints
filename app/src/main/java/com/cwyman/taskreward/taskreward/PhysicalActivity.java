package com.cwyman.taskreward.taskreward;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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

public class PhysicalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnPushUp;
    Button btnCrunch;
    Button btnSquat;
    Button btnJumpRope;

    TextView lblPushups;
    TextView lblCrunch;
    TextView lblSquat;
    TextView lblJump;

    TextView txtPushupSetNum;
    TextView txtSquatSetNum;
    TextView txtCrunchSetNum;
    TextView txtJRSetNum;

    private String chkPushupClick;
    private String chkCrunchClick;
    private String chkSquatClick;
    private String chkJRClick;
    private String rTime;
    private int tCount;

    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("Users");

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnPushUp = (Button) findViewById(R.id.btnPhysicalPushUp);
        btnCrunch = (Button) findViewById(R.id.btnPhysicalCrunch);
        btnSquat = (Button) findViewById(R.id.btnPhysicalSquat);
        btnJumpRope = (Button) findViewById(R.id.btnPhysicalJump);

        lblPushups = (TextView) findViewById(R.id.lblPushups);
        lblCrunch = (TextView) findViewById(R.id.lblCrunches);
        lblSquat = (TextView) findViewById(R.id.lblSquats);
        lblJump = (TextView) findViewById(R.id.lblJump);

        txtCrunchSetNum = (TextView) findViewById(R.id.txtCrunchNum);
        txtJRSetNum = (TextView) findViewById(R.id.txtJumpRopeNum);
        txtSquatSetNum = (TextView) findViewById(R.id.txtSquatNum);
        txtPushupSetNum = (TextView) findViewById(R.id.txtPushUpNum);

        final String uid = getIntent().getExtras().getString("intentUserId");
        //-------------------------------------------------------------------------------------------------------------------
                //initial check to see if user hit 20 sets
                //sets label to green and button visibility
                //tells the user a message the user finished the daily 200 reps of the correlating exercise
                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    String uidCheck = uid;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                            String snapKey = objSnapshot.getKey();

                            if (snapKey.equalsIgnoreCase(uidCheck)){

                                String squatBtnChk = objSnapshot.child("squats").getValue().toString();
                                String pushupBtnChk = objSnapshot.child("pushups").getValue().toString();
                                String crunchBtnChk = objSnapshot.child("crunches").getValue().toString();
                                String jumpBtnChk = objSnapshot.child("jumprope").getValue().toString();

                                if (pushupBtnChk.equalsIgnoreCase("200")){

                                    lblPushups.setTextColor(Color.GREEN);
                                    btnPushUp.setVisibility(View.INVISIBLE);
                                    txtPushupSetNum.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200 reps of green colored activities!", Toast.LENGTH_LONG).show();
                                }
                                if (squatBtnChk.equalsIgnoreCase("200")){

                                    lblSquat.setTextColor(Color.GREEN);
                                    btnSquat.setVisibility(View.INVISIBLE);
                                    txtSquatSetNum.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200 reps of green colored activities!", Toast.LENGTH_LONG).show();

                                }
                                if (crunchBtnChk.equalsIgnoreCase("200")){

                                    lblCrunch.setTextColor(Color.GREEN);
                                    btnCrunch.setVisibility(View.INVISIBLE);
                                    txtCrunchSetNum.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200 reps of green colored activities!", Toast.LENGTH_LONG).show();

                                }
                                if (jumpBtnChk.equalsIgnoreCase("200")){

                                    lblJump.setTextColor(Color.GREEN);
                                    btnJumpRope.setVisibility(View.INVISIBLE);
                                    txtJRSetNum.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200 reps of green colored activities!", Toast.LENGTH_LONG).show();

                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });

//--------------------------------------------------------------------------------------
        //each button has an onClickListener
        //checks if the user put in a valid set number
        //sets the reward time of 2 minutes per set and adds 1 to task counter per set
        //if the user enters more than a total of 20 sets on multiple inputs still sets the limit at 20 sets
        //changes the label colors and button visibility accordingly
        //gives user messages according to database and user input
        btnPushUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {

                if ((!txtPushupSetNum.getText().toString().isEmpty()) && (Integer.parseInt(txtPushupSetNum.getText().toString()) <= 20))
                {

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
                                    String pushupChk = objSnapshot.child("pushups").getValue().toString();
                                    String crunchChk = objSnapshot.child("crunches").getValue().toString();
                                    String squatChk = objSnapshot.child("squats").getValue().toString();
                                    String jumpropeChk = objSnapshot.child("jumprope").getValue().toString();

                                    int pushupNum = Integer.parseInt(pushupChk);
                                    int pushNumTxt = Integer.parseInt(txtPushupSetNum.getText().toString());
                                    int pushNum = (pushNumTxt * 10) + pushupNum;

                                    int pushRewardNum;

                                    //checks the rep count and sets the reward time, task count, labels, buttons and text fields accordingly
                                    //even if the user goes above the 200 reps it will calculate a 200 rep reward time and task count limit
                                    if (pushNum <= 200) {

                                        tCount = Integer.parseInt(taskCnt) + pushNumTxt;
                                        chkPushupClick = Integer.toString(pushNum);
                                        pushRewardNum = Integer.parseInt(rewardTime) + (2 * pushNumTxt);
                                        rTime = Integer.toString(pushRewardNum);

                                    }else{

                                        chkPushupClick = "200";
                                        pushNum = 200;
                                        pushNumTxt = (20) - (Integer.parseInt(pushupChk)/10);
                                        pushRewardNum = Integer.parseInt(rewardTime) + (2 * pushNumTxt);
                                        rTime = Integer.toString(pushRewardNum);

                                        tCount = Integer.parseInt(taskCnt) + pushNumTxt;

                                        lblPushups.setTextColor(Color.GREEN);
                                        btnPushUp.setVisibility(View.INVISIBLE);
                                        txtPushupSetNum.setVisibility(View.INVISIBLE);

                                    }

                                    writeNewPhysical(uid, rTime, tCount, chkPushupClick, crunchChk, squatChk, jumpropeChk);

                                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                                    txtPushupSetNum.setText("");

                                    //if database shows whether the user has 200 reps recorded in the database
                                    //displays messages and sets label, button, text fields changes
                                    if (pushNum == 200){
                                        lblPushups.setTextColor(Color.GREEN);
                                        btnPushUp.setVisibility(View.INVISIBLE);
                                        txtPushupSetNum.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200+ reps of push-ups! +2 reward mins./set", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Great! You have finished " + pushNum + " reps of squats! +2 mins./set", Toast.LENGTH_LONG).show();
                                    }

                                    uidCheck = "finished";
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    txtPushupSetNum.setText("");
                    txtPushupSetNum.requestFocus();
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    Toast.makeText(getApplicationContext(), "Enter a valid number of sets! Only 20 sets allowed!", Toast.LENGTH_LONG).show();
                }

            }
        });

//------------------------------------------------------------------------------------------------------------
        btnCrunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtCrunchSetNum != null && Integer.parseInt(txtCrunchSetNum.getText().toString()) <= 20)
                {

                mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    String uidCheck = uid;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            String snapKey = objSnapshot.getKey();

                            if (snapKey.equalsIgnoreCase(uidCheck))
                            {

                                String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                String rewardTime = objSnapshot.child("reward").getValue().toString();
                                String pushupChk = objSnapshot.child("pushups").getValue().toString();
                                String crunchChk = objSnapshot.child("crunches").getValue().toString();
                                String squatChk = objSnapshot.child("squats").getValue().toString();
                                String jumpropeChk = objSnapshot.child("jumprope").getValue().toString();

                                int crunchNumber = Integer.parseInt(crunchChk);
                                int crunchNumTxt = Integer.parseInt(txtCrunchSetNum.getText().toString());
                                int crunchNum = (crunchNumTxt * 10) + crunchNumber;
                                int crunchRewardNum;

                                //checks the rep count and sets the reward time, task count, labels, buttons and text fields accordingly
                                //even if the user goes above the 200 reps it will calculate a 200 rep reward time and task count limit
                                if (crunchNum <= 200)
                                {

                                    tCount = Integer.parseInt(taskCnt) + crunchNumTxt;

                                    crunchRewardNum = Integer.parseInt(rewardTime) + (2 * crunchNumTxt);
                                    rTime = Integer.toString(crunchRewardNum);
                                    chkCrunchClick = Integer.toString(crunchNum);


                                }else{
                                    chkCrunchClick = "200";
                                    crunchNum = 200;
                                    crunchNumTxt = (20) - (Integer.parseInt(crunchChk)/10);
                                    crunchRewardNum = Integer.parseInt(rewardTime) + (2 * crunchNumTxt);
                                    rTime = Integer.toString(crunchRewardNum);

                                    tCount = Integer.parseInt(taskCnt) + crunchNumTxt;

                                    lblCrunch.setTextColor(Color.GREEN);
                                    btnCrunch.setVisibility(View.INVISIBLE);
                                    txtCrunchSetNum.setVisibility(View.INVISIBLE);

                                }

                                writeNewPhysical(uid, rTime, tCount, pushupChk, chkCrunchClick, squatChk, jumpropeChk);

                                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                                txtCrunchSetNum.setText("");

                                //if database shows whether the user has 200 reps recorded in the database
                                //displays messages and sets label, button, text fields changes
                                if (crunchNum == 200) {
                                    lblCrunch.setTextColor(Color.GREEN);
                                    btnCrunch.setVisibility(View.INVISIBLE);
                                    txtCrunchSetNum.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200+ reps of crunches! +2 reward mins./set", Toast.LENGTH_LONG).show();

                                } else {
                                      Toast.makeText(getApplicationContext(), "Great! You have finished " + crunchNum + " reps of squats! +2 mins./set", Toast.LENGTH_LONG).show();
                                }

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
            else{
                txtCrunchSetNum.setText("");
               // txtCrunchSetNum.requestFocus();
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                Toast.makeText(getApplicationContext(), "Enter a valid number of sets! Only 20 sets allowed!", Toast.LENGTH_LONG).show();
                }

            }
        });

//---------------------------------------------------------------------------------------------------------
        btnSquat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSquatSetNum != null && Integer.parseInt(txtSquatSetNum.getText().toString()) <= 20)
                {

                    mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        String uidCheck = uid;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                                String snapKey = objSnapshot.getKey();

                                if (snapKey.equalsIgnoreCase(uidCheck))
                                {

                                    String taskCnt = objSnapshot.child("taskcount").getValue().toString();
                                    String rewardTime = objSnapshot.child("reward").getValue().toString();
                                    String pushupChk = objSnapshot.child("pushups").getValue().toString();
                                    String crunchChk = objSnapshot.child("crunches").getValue().toString();
                                    String squatChk = objSnapshot.child("squats").getValue().toString();
                                    String jumpropeChk = objSnapshot.child("jumprope").getValue().toString();

                                    int squatNumber = Integer.parseInt(squatChk);
                                    int squatNumTxt = Integer.parseInt(txtSquatSetNum.getText().toString());
                                    int squatNum = (squatNumTxt * 10) + squatNumber;
                                    int squatRewardNum;

                                    //checks the rep count and sets the reward time, task count, labels, buttons and text fields accordingly
                                    //even if the user goes above the 200 reps it will calculate a 200 rep reward time and task count limit
                                    if (squatNum <= 200)
                                    {

                                        tCount = Integer.parseInt(taskCnt) + squatNumTxt;
                                        chkSquatClick = Integer.toString(squatNum);
                                        squatRewardNum = Integer.parseInt(rewardTime) + (2 * squatNumTxt);
                                        rTime = Integer.toString(squatRewardNum);

                                    }else{
                                        squatNum = 200;
                                        chkSquatClick = "200";

                                        squatNumTxt = (20) - (Integer.parseInt(squatChk)/10);
                                        squatRewardNum = Integer.parseInt(rewardTime) + (2 * squatNumTxt);
                                        rTime = Integer.toString(squatRewardNum);

                                        tCount = Integer.parseInt(taskCnt) + squatNumTxt;

                                        lblSquat.setTextColor(Color.GREEN);
                                        btnSquat.setVisibility(View.INVISIBLE);
                                        txtSquatSetNum.setVisibility(View.INVISIBLE);

                                    }

                                    writeNewPhysical(uid, rTime, tCount, pushupChk, crunchChk, chkSquatClick, jumpropeChk);

                                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                                    txtSquatSetNum.setText("");

                                    //if database shows whether the user has 200 reps recorded in the database
                                    //displays messages and sets label, button, text fields changes
                                    if (squatNum == 200) {
                                        lblSquat.setTextColor(Color.GREEN);
                                        btnSquat.setVisibility(View.INVISIBLE);
                                        txtSquatSetNum.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200+ reps of squats! +2 reward mins./set", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Great! You have finished " + squatNum + " reps of squats! +2 mins./set", Toast.LENGTH_LONG).show();
                                    }

                                    uidCheck = "finished";
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    txtSquatSetNum.setText("");
                    //txtSquatSetNum.requestFocus();
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    Toast.makeText(getApplicationContext(), "Enter a valid number of sets! Only 20 sets allowed!", Toast.LENGTH_LONG).show();
                }

            }
        });

//-----------------------------------------------------------------------------------------------------------------
        btnJumpRope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { if (txtJRSetNum != null && Integer.parseInt(txtJRSetNum.getText().toString()) <= 20)
            {

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
                                String pushupChk = objSnapshot.child("pushups").getValue().toString();
                                String crunchChk = objSnapshot.child("crunches").getValue().toString();
                                String squatChk = objSnapshot.child("squats").getValue().toString();
                                String jumpropeChk = objSnapshot.child("jumprope").getValue().toString();

                                int jumpNumber = Integer.parseInt(jumpropeChk);
                                int jumpNumTxt = Integer.parseInt(txtJRSetNum.getText().toString());
                                int jumpNum = (jumpNumTxt * 10) + jumpNumber;
                                int jumpRewardNum;

                                //checks the rep count and sets the reward time, task count, labels, buttons and text fields accordingly
                                //even if the user goes above the 200 reps it will calculate a 200 rep reward time and task count limit
                                if (jumpNum <= 200)
                                {

                                    tCount = Integer.parseInt(taskCnt) + jumpNumTxt;
                                    chkJRClick = Integer.toString(jumpNum);
                                    jumpRewardNum = Integer.parseInt(rewardTime) + (2 * jumpNumTxt);
                                    rTime = Integer.toString(jumpRewardNum);

                                }else{
                                    chkJRClick = "200";
                                    jumpNum = 200;
                                    jumpNumTxt = (20) - (Integer.parseInt(jumpropeChk)/10);
                                    jumpRewardNum = Integer.parseInt(rewardTime) + (2 * jumpNumTxt);
                                    rTime = Integer.toString(jumpRewardNum);

                                    tCount = Integer.parseInt(taskCnt) + jumpNumTxt;

                                    lblJump.setTextColor(Color.GREEN);
                                    btnJumpRope.setVisibility(View.INVISIBLE);
                                    txtJRSetNum.setVisibility(View.INVISIBLE);

                                }

                                writeNewPhysical(uid, rTime, tCount, pushupChk, crunchChk, squatChk, chkJRClick);

                                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                                txtJRSetNum.setText("");

                                //if database shows whether the user has 200 reps recorded in the database
                                //displays messages and sets label, button, text fields changes
                                if (jumpNum == 200) {
                                    lblJump.setTextColor(Color.GREEN);
                                    btnJumpRope.setVisibility(View.INVISIBLE);
                                    txtJRSetNum.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Congrats! You have finished the max 200+ reps of jump rope! +2 reward mins./set", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Great! You have finished " + jumpNum + " reps of jump rope! +2 reward mins.", Toast.LENGTH_LONG).show();
                                }
                                uidCheck = "finished";
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                txtJRSetNum.setText("");
                txtJRSetNum.requestFocus();
                Toast.makeText(getApplicationContext(), "Enter a valid number of sets! Only 20 sets allowed!", Toast.LENGTH_LONG).show();
            }

            }
        });
//--------------------------------------------------------------------------------------------------------------
    } //<--end of onCerate()
//------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String intentUid = getIntent().getExtras().getString("intentUserId");
            Intent intent = new Intent(PhysicalActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
    }
//-------------------------------------------------------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String intentUid = getIntent().getExtras().getString("intentUserId");

        if (id == R.id.nav_physical){

            Intent intent = new Intent(PhysicalActivity.this, PhysicalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_physical);
        }
        if (id == R.id.nav_bad){

            Intent intent = new Intent(PhysicalActivity.this, BadHabitActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_bad_habit);
        }
        if (id == R.id.nav_diet){

            Intent intent = new Intent(PhysicalActivity.this, DietActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_diet);
        }
        if (id == R.id.nav_mental){

            Intent intent = new Intent(PhysicalActivity.this, MentalActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_mental);
        }
        if (id == R.id.nav_cleaning){

            Intent intent = new Intent(PhysicalActivity.this, CleaningActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_cleaning);
        }
        if (id == R.id.nav_reward){

            Intent intent = new Intent(PhysicalActivity.this, RewardActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_reward);
        }
        if (id == R.id.nav_main){

            Intent intent = new Intent(PhysicalActivity.this, MainActivity.class);
            intent.putExtra("intentUserId", intentUid);
            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
        if (id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(PhysicalActivity.this);
            builder.setMessage("Do you want to Sign Out? Remember to make sure you're connected to the internet to save data correctly!")
                    .setPositiveButton("Sign Out!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Signs out!
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(PhysicalActivity.this, SignInActivity.class));
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
    private void writeNewPhysical(String dbKey, String rTime,  int tCount, String pushupClick, String crunchClick,String squatClick, String jumpropeClick) {
        //used to update values in the database

        String newTaskCount = Integer.toString(tCount);

        Map<String, Object> newPhysicalValues = new HashMap<>();

        newPhysicalValues.put("pushups", pushupClick);
        newPhysicalValues.put("crunches", crunchClick);
        newPhysicalValues.put("squats", squatClick);
        newPhysicalValues.put("jumprope", jumpropeClick);
        newPhysicalValues.put("taskcount", newTaskCount);
        newPhysicalValues.put("reward", rTime);

        mUsersRef.child(dbKey).updateChildren(newPhysicalValues);
    }
    //--------------------------------------------------------------------------------------
}
