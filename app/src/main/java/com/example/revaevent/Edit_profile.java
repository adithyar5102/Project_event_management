package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Edit_profile extends AppCompatActivity {
    Button edit_profile;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth=FirebaseAuth.getInstance();
        edit_profile=findViewById(R.id.edit_profile_edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Reva_Event_workflow", "OnClick Edit profile button");
                EditText name=findViewById(R.id.edit_profile_name);
                EditText email=findViewById(R.id.edit_profile_email);
                EditText semester=findViewById(R.id.edit_profile_semester);
                EditText branch=findViewById(R.id.edit_profile_branch);
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("User");
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || semester.getText().toString().isEmpty() || branch.getText().toString().isEmpty()){
                    Toast.makeText(Edit_profile.this, "Enter all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.child(mAuth.getUid()).setValue(new Student(mAuth.getUid(),name.getText().toString(),email.getText().toString(),semester.getText().toString(),branch.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Reva_Event_workflow", "Details changed");
                            Toast.makeText(Edit_profile.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Edit_profile.this,Profile.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Edit_profile.this, "Could not Updated\nTry again later", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Edit_profile.this,Profile.class);
                            Log.d("Reva_Event_workflow", "Details not changed");
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Edit_profile.this,Profile.class);
        startActivity(intent);
        finish();
    }
}