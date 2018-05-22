package com.example.cwyma.lifepoints;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText txtEmail, txtPassword;
    ProgressBar progBarSignIn;

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

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnNewAcct:

                startActivity(new Intent(this, CreateAcctActivity.class ));

                break;

            case R.id.btnSignIn:

                userLogin();
                break;

        }


    }

    private void userLogin() {

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(email.isEmpty()){

            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            txtEmail.setError("Please enter a valid email address");
            txtEmail.requestFocus();
            return;
        }

        if (password.length() < 6){

            txtPassword.setError("Password must contain at least 6 characters");
            txtPassword.requestFocus();
            return;
        }

        if(password.isEmpty()){

            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }

        progBarSignIn.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progBarSignIn.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
