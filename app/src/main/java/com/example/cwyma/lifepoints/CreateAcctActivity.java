package com.example.cwyma.lifepoints;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class CreateAcctActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txtCreateEmail, txtCreatePassword;
    ProgressBar progCreateAcct;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acct);

        txtCreateEmail = findViewById(R.id.txtCreateEmail);
        txtCreatePassword = findViewById(R.id.txtCreatePassword);
        progCreateAcct = findViewById(R.id.progCreateAcct);



        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnCreateAcct).setOnClickListener(this);
        findViewById(R.id.btnCreateSignIn).setOnClickListener(this);
    }


    private void registerUser(){

        String email = txtCreateEmail.getText().toString().trim();
        String password = txtCreatePassword.getText().toString().trim();

        if(email.isEmpty()){

            txtCreateEmail.setError("Email is required");
            txtCreateEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            txtCreateEmail.setError("Please enter a valid email address");
            txtCreateEmail.requestFocus();
            return;
        }

        if (password.length() < 6){

            txtCreatePassword.setError("Password must contain at least 6 characters");
            txtCreatePassword.requestFocus();
            return;
        }

        if(password.isEmpty()){

            txtCreatePassword.setError("Password is required");
            txtCreatePassword.requestFocus();
            return;
        }

        progCreateAcct.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progCreateAcct.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(CreateAcctActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "User Account Created Successful!", Toast.LENGTH_SHORT).show();
                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreateAcct:
            registerUser();
            break;

            case R.id.txtCreateEmail:

                startActivity(new Intent(this, SignInActivity.class));
                break;

            case R.id.btnCreateSignIn:
                Intent intent = new Intent(CreateAcctActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
