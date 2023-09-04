package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button edit_profile;
    Button logout;
    ImageView registered;
    ImageView home;
    ImageView profile;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        logout=findViewById(R.id.profile_logout);
        edit_profile=findViewById(R.id.profile_edit_profile);

        profile=findViewById(R.id.profile_activity_down_navigation_bar_profile);
        registered=findViewById(R.id.profile_activity_down_navigation_bar_registered);
        home=findViewById(R.id.profile_activity_down_navigation_bar_home);


        profile.setImageResource(R.drawable.baseline_person_24_white);
        registered.setImageResource(R.drawable.baseline_format_list_bulleted_24_grey);
        home.setImageResource(R.drawable.baseline_home_24_grey);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile.this,Registered_events.class);
                startActivity(intent);
                finish();
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(Profile.this,Login_page.class);
                startActivity(intent);
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile.this,Edit_profile.class);
                startActivity(intent);
                finish();
            }
        });
        get_user_detail();
    }



    private void get_user_detail(){
        Log.d("Reva_Event_workflow", "Getting user details");
        database= FirebaseDatabase.getInstance();
        reference=database.getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Student cur=ds.getValue(Student.class);
                    assert cur != null;
                    if(cur.uid.equals(mAuth.getUid())){
                        set_list(cur);
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("get_user_detail", "onCancelled: failed to load");
            }
        });
    }

    public void set_list(@NonNull Student cur){
        Log.d("Reva_Event_workflow", "Setting user details");
        TextView name=findViewById(R.id.profile_name);
        name.setText(cur.name);
        TextView email=findViewById(R.id.profile_email);
        email.setText(cur.email);
        TextView sem=findViewById(R.id.profile_semester);
        sem.setText(cur.sem);
        TextView branch=findViewById(R.id.profile_branch);
        branch.setText(cur.branch);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}