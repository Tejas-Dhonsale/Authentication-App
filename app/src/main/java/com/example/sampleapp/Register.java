package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    private EditText Firstname;
    private EditText Lastname;
    private EditText Email;
    private EditText Password;
    private Button Signup;
    private TextView Login;
    private FirebaseAuth mAuth;
    private ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Firstname = findViewById(R.id.editTextTextPersonName);
        Lastname = findViewById(R.id.editTextTextPersonName2);
        Email = findViewById(R.id.editTextTextEmailAddress2);
        Password = findViewById(R.id.editTextTextPassword2);
        Signup = findViewById(R.id.button2);
        Login = findViewById(R.id.textView3);
        pr = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser();
            }

            private void registeruser() {
                String firstname = Firstname.getText().toString().trim();
                String lastname  = Lastname.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (firstname.isEmpty()){
                    Firstname.setError("Enter Your First Name");
                    Firstname.requestFocus();
                    return;
                }
                if (lastname.isEmpty()){
                    Lastname.setError("Enter Your Last Name");
                    Lastname.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    Email.setError("Enter Your Email Address");
                    Email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("please provide valid Email Address");
                    Email.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    Password.setError("Enter password");
                    Password.requestFocus();
                }
                if (password.length()<6){
                    Password.setError("Min Password length should be 7 characters!");
                    Password.requestFocus();
                }

                pr.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(firstname,lastname,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Register.this,"User is Registered successfully",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Register.this,LoginActivity.class));
                                                finish();
                                                pr.setVisibility(View.GONE);
                                            }
                                            else{
                                                Toast.makeText(Register.this,"Registration failed",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        }else
                            {  Toast.makeText(Register.this,"Registration failed",Toast.LENGTH_LONG).show();
                        }
                            pr.setVisibility(View.GONE);

                    }
                });
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,LoginActivity.class));
                finish();
            }
        });
    }
}