package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_page extends AppCompatActivity {

    EditText emailt;
    EditText passwordt;
    TextView signupbtn;
    Button login;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        emailt=findViewById(R.id.login_email);
        passwordt=findViewById(R.id.login_password);
        signupbtn=findViewById(R.id.login_signup_button);
        login=findViewById(R.id.login_button);
        mAuth=FirebaseAuth.getInstance();
        //get_det();


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_page.this, Signup_page.class);
                Log.d("Reva_Event_workflow", "Sign up btn clicked");
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailt.getText().toString().isEmpty() || passwordt.getText().toString().isEmpty()){
                    Toast.makeText(Login_page.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Reva_Event_workflow", "Login btn clicked");
                user_login(emailt.getText().toString(),passwordt.getText().toString());
            }
        });

    }

    private void user_login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(Login_page.this,MainActivity.class);
                            Log.d("Reva_Event_workflow", "Login Successful");
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("Reva_Event_workflow", "Failed to login");
                            Toast.makeText(Login_page.this, "Enter valid Email and Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(Login_page.this,MainActivity.class);
            Log.d("Reva_Event_workflow", "Logged in current user");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
