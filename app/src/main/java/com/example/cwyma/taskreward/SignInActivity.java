package com.example.cwyma.taskreward;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    public  String curUserKey;
    private FirebaseAuth mAuth;
  //  private FirebaseAuth.AuthStateListener mAuthStateListener;

    EditText txtEmail, txtPassword;
    ProgressBar progBarSignIn;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        progBarSignIn = findViewById(R.id.progBarSignIn);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnNewAcct).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnResetPassword).setOnClickListener(this);

    }

    DateFormat curDateFormat = new SimpleDateFormat("MM/dd/yy");
    Date curDate = new Date();
    final String curStrDate = curDateFormat.format(curDate);
//-------------------------------------------------------------------------------------
    //onClick method switch cases for the 3 different buttons
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnNewAcct:

                startActivity(new Intent(this, CreateAcctActivity.class));

                break;
            //-----------------------------------------------
            case R.id.btnSignIn:

                try {
                    checkEmailLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                    progBarSignIn.setVisibility(View.GONE);
                    txtEmail.requestFocus();
                }
                try {
                    checkPasswordLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                    progBarSignIn.setVisibility(View.GONE);
                }

                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                try {
                    addToDatabase();
                } catch (Exception e) {
                    e.printStackTrace();
                    progBarSignIn.setVisibility(View.GONE);
                }

                break;
            //-------------------------------------------------------------------------
            case R.id.btnResetPassword:

                try {
                    checkEmailLogin();
                    mAuth.sendPasswordResetEmail(txtEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this, "An email has been sent to you.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                    Toast.makeText(getApplicationContext(), "password reset process", Toast.LENGTH_LONG).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (Exception e) {
                    txtEmail.requestFocus();
                    e.printStackTrace();
                }

                break;
                //-------------------------------------------------------------------------------------
        } //<-- end of switch
    } //<-- end of onClick
//--------------------------------------------------------------------------
    private void checkEmailLogin() {

        final String email = txtEmail.getText().toString().trim();

             if (email.isEmpty()) {
                 ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                       .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                 txtEmail.setError("Email is required");
                 txtEmail.requestFocus();
            return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                         .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                txtEmail.setError("Please enter a valid email address");
                txtEmail.requestFocus();
            return;
            }

    }
    //-------------------------------------------------------------------
    private void checkPasswordLogin() {

        String password = txtPassword.getText().toString().trim();

        if (password.length() < 6)
        {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

            txtPassword.setError("Password must contain at least 6 characters");
            txtPassword.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }
    }
//------------------------------------------------------------------------------
    private void addToDatabase(){

        final String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                mRootRef.child("CurLoginDate").setValue(curStrDate);
                if (task.isSuccessful())
                {
                    progBarSignIn.setVisibility(View.VISIBLE);

                    if(mAuth.getCurrentUser().isEmailVerified())
                    {

                        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot snapshot)
                            {

                                String checkEmail = txtEmail.getText().toString().trim();

                                for (DataSnapshot objSnapshot : snapshot.getChildren())
                                {

                                    String snapStr = objSnapshot.child("email").getValue().toString();
                                    String snapKey = objSnapshot.getKey();


                                    if (checkEmail.equalsIgnoreCase(snapStr))
                                    {
                                        if (!curStrDate.equalsIgnoreCase(objSnapshot.child("lastLoginDate").getValue().toString()))
                                        {
                                            writeNewDate(snapKey);
                                        }

                                        curUserKey = snapKey;

                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.putExtra("intentUserId", curUserKey);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }

                                }

                            }

                            //------------------------------------------------------------------------------
                            @Override
                            public void onCancelled(DatabaseError firebaseError) {
                                Log.e("Read failed", firebaseError.getMessage());
                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "Please verify your inbox.", Toast.LENGTH_SHORT).show();
                        progBarSignIn.setVisibility(View.GONE);
                        txtPassword.setText("");
                        txtEmail.setText("");
                        txtEmail.requestFocus();
                    }

                }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                }
                    //------------------------------------------------------------------------------------------------------

            }
        });
    }
  //----------------------------------------------------------------------------------------------------------------
    private void writeNewDate(String dbKey) {
        //used to reset all the values except reward time

        Map<String, Object> newValues = new HashMap<>();

        newValues.put("dishes", "unclicked");
        newValues.put("floors", "unclicked");
        newValues.put("laundry", "unclicked");
        newValues.put("teeth", "unclicked");
        newValues.put("fruit", "unclicked");
        newValues.put("veg", "unclicked");
        newValues.put("vitamins", "unclicked");
        newValues.put("water", "unclicked");
        newValues.put("coding", "unclicked");
        newValues.put("meditate", "unclicked");
        newValues.put("read", "unclicked");
        newValues.put("study", "unclicked");
        newValues.put("crunches", "0");
        newValues.put("jumprope", "0");
        newValues.put("pushups", "0");
        newValues.put("squats", "0");
        newValues.put("lastLoginDate", curStrDate);
        newValues.put("badcount", "0");
        newValues.put("taskcount", "0");

        mUsersRef.child(dbKey).updateChildren(newValues);

    }
}