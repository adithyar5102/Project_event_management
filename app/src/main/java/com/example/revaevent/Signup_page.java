package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_page extends AppCompatActivity {
    EditText emailt;
    EditText passwordt;
    EditText namet;
    EditText semister;
    TextView signinbtn;
    EditText branch;
    Button signup;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        emailt=findViewById(R.id.Hotel_application_signup_email);
        passwordt=findViewById(R.id.Hotel_application_signup_password);
        semister=findViewById(R.id.Hotel_application_signup_semester);
        branch=findViewById(R.id.Hotel_application_signup_branch);
        namet=findViewById(R.id.Hotel_application_signup_name);
        signinbtn=findViewById(R.id.Hotel_application_signup_signin_button);
        signup=findViewById(R.id.Hotel_application_signup_signup_btn);
        mAuth=FirebaseAuth.getInstance();


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Signup_page.this, Login_page.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailt.getText().toString().isEmpty() || namet.getText().toString().isEmpty() || passwordt.getText().toString().isEmpty()
                || branch.getText().toString().isEmpty() || semister.getText().toString().isEmpty()){
                    Toast.makeText(Signup_page.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                user_signin(emailt.getText().toString(),passwordt.getText().toString());

            }
        });

    }

    private void user_signin(String email,String password){
        Log.d("Reva_Event_workflow", "Sign up authentication");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Reva_Event_workflow", "Sign up successful");
                            add_user_cred(email,namet.getText().toString(),mAuth.getUid(),semister.getText().toString(),branch.getText().toString());
                            Intent intent=new Intent(Signup_page.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.d("Reva_Event_workflow", "Singup failed");
                            Toast.makeText(Signup_page.this, "Invalid details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void add_user_cred(String email,String name,String uid,String sem,String branch){
        Log.d("Reva_Event_workflow", "User details added to cloud");
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("User");
        reference.child(uid).setValue(new Student(uid,name,email,sem,branch));
    }
    @Override
    public void onBackPressed() {
    }

}