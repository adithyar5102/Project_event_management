package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registered_events extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference ref;
    String uid;
    ImageView registered;
    ImageView home;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_events);
        uid = getIntent().getStringExtra("uid");
        recyclerView = findViewById(R.id.registered_event_recycle_view);
        profile = findViewById(R.id.registered_activity_down_navigation_bar_profile);
        registered = findViewById(R.id.registered_activity_down_navigation_bar_registered);
        home = findViewById(R.id.registered_activity_down_navigation_bar_home);
        profile.setImageResource(R.drawable.baseline_person_24_grey);
        registered.setImageResource(R.drawable.baseline_format_list_bulleted_24);
        home.setImageResource(R.drawable.baseline_home_24_grey);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registered_events.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registered_events.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        try{
            get_data();
        }catch (Exception e){
            ArrayList<String> temp=new ArrayList<>();

            temp.add("No registered event");
            start_recycleview(temp);
            return;
        }
    }


    public void get_data() {
        Log.d("Reva_Event_workflow", "Get registered data from firebase");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("student_registered");
        ArrayList<String> data=new ArrayList<>();
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    data.add((String) ds.getValue());
                }
                start_recycleview(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Reva_Event_workflow", "Failed to Get registered data from firebase");
            }
        });
    }


    @Override
        public void onBackPressed () {
            Intent intent = new Intent(Registered_events.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        public void start_recycleview(ArrayList < String > data) {
            Recycle_view_str_al rv = new Recycle_view_str_al(data, Registered_events.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(rv);
        }

}