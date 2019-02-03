package com.cwyman.taskreward.taskreward;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CreateAcctActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef;
    DatabaseReference mUsersRef;

    EditText txtCreateEmail, txtCreatePassword;
    ProgressBar progCreateAcct;

    String curUserKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acct);

        txtCreateEmail = findViewById(R.id.txtCreateEmail);
        txtCreatePassword = findViewById(R.id.txtCreatePassword);
        progCreateAcct = findViewById(R.id.progCreateAcct);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRef = FirebaseDatabase.getInstance().getReference("Users");


        findViewById(R.id.btnCreateAcct).setOnClickListener(this);
        findViewById(R.id.btnCreateSignIn).setOnClickListener(this);
    }

    //----------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreateAcct:
                registerUser();
                break;


            case R.id.btnCreateSignIn:
                Intent intent = new Intent(CreateAcctActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }


    //------------------------------------------------------------------------
    //checks if text in email field is present and in valid email format
    //checks if text in password field is not empty and longer than 6 characters
    //creates an authenticated user that still needs to click the link in their email inbox and complete the sign-in activity
    //checks and tells the user if they already created the account but still needs to verify with the sent email link
    //checks and tells the user if they already created the account and have verified it with the email link

    private void registerUser() {

        final String email = txtCreateEmail.getText().toString().trim();
        String password = txtCreatePassword.getText().toString().trim();

        if (email.isEmpty()) {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            txtCreateEmail.setError("Email is required");
            txtCreateEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            txtCreateEmail.setError("Please enter a valid email address");
            txtCreateEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            txtCreatePassword.setError("Password must contain at least 6 characters");
            txtCreatePassword.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            txtCreatePassword.setError("Password is required");
            txtCreatePassword.requestFocus();
            return;
        }

        final String checkEmail = txtCreateEmail.getText().toString().trim();

        progCreateAcct.setVisibility(View.VISIBLE);

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           // final String emailChk = email;
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

               // progCreateAcct.setVisibility(View.GONE);

                if (task.isSuccessful())
                {
                    addToDatabase();
                    mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {

                                 mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {

                                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                                        {
                                            String snapStr = objSnapshot.child("email").getValue().toString();

                                            if(checkEmail.equalsIgnoreCase(snapStr))
                                            {

                                                curUserKey = objSnapshot.getKey();
                                                Toast.makeText(getApplicationContext(), "User Account Created Successful! Check inbox to verify and sign-in!"  ,Toast.LENGTH_SHORT).show();
                                                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                                                Intent intent = new Intent(CreateAcctActivity.this, SignInActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("intentUserId", curUserKey);
                                                startActivity(intent);

                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                    {
                                        Toast.makeText(getApplicationContext(), "Database Error! Please connect to the internet!", Toast.LENGTH_LONG).show();
                                    }

                                });

                            }else{
                                    Toast.makeText(CreateAcctActivity.this, task.getException().getMessage()  ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    progCreateAcct.setVisibility(View.GONE);
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        Toast.makeText(getApplicationContext(), "Already registered! Click the 'Already have an Account' button and try signing in or resetting your password!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                }
            });

        }
//-------------------------------------------------------------------------------------------
        // creates a new user with unique key
        // each new user starts with 10 reward minutes

        private void addToDatabase(){

            DateFormat curDateFormat = new SimpleDateFormat("MM/dd/yy");
            Date curDate = new Date();
            final String curStrDate = curDateFormat.format(curDate);

                HashMap<String, String> dataPoints = new HashMap<String, String>();
                dataPoints.put("email", txtCreateEmail.getText().toString().trim());
                dataPoints.put("dishes", "unclicked");
                dataPoints.put("floors", "unclicked");
                dataPoints.put("laundry", "unclicked");
                dataPoints.put("teeth", "unclicked");
                dataPoints.put("fruit", "unclicked");
                dataPoints.put("veg", "unclicked");
                dataPoints.put("vitamins", "unclicked");
                dataPoints.put("water", "unclicked");
                dataPoints.put("coding", "unclicked");
                dataPoints.put("meditate", "unclicked");
                dataPoints.put("read", "unclicked");
                dataPoints.put("study", "unclicked");
                dataPoints.put("crunches", "0");
                dataPoints.put("jumprope", "0");
                dataPoints.put("pushups", "0");
                dataPoints.put("squats", "0");
                dataPoints.put("reward", "10");
                dataPoints.put("badcount", "0");
                dataPoints.put("taskcount", "0");
                dataPoints.put("lastLoginDate", curStrDate);
                 mUsersRef.push().setValue(dataPoints);

            };

    //------------------------------------------------------------------------------------------

};
