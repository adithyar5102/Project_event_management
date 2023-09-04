package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Event_in_detail extends AppCompatActivity {


    ArrayList<Event_details> event_details=new ArrayList<>();
    ImageView poster;
    Button register;
    TextView description;
    FirebaseStorage storage;
    String event;
    StorageReference stref;
    String url;
    FirebaseDatabase database2;
    DatabaseReference reference2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_in_detail);
        register=findViewById(R.id.in_detail_register);
        poster=findViewById(R.id.in_detail_poster);
        description=findViewById(R.id.in_detail_description);
        storage=FirebaseStorage.getInstance();

        event=getIntent().getStringExtra("event_name");
        stref=storage.getReference().child("event_poster/"+event);

        get_event_detail();


        try {
            File localfile= File.createTempFile("poster","jpg");
            Log.d("Reva_Event_workflow", "Local file created");
            stref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Reva_Event_workflow", "Image stored in local file");
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    poster.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Reva_Event_workflow", "Unable to store in local file");
                    poster.setImageResource(R.drawable.not_available);
                }
            });
        } catch (IOException e) {
            Log.d("Reva_Event_workflow", "Error in creating file");
            throw new RuntimeException(e);
        }




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Reva_Event_workflow", "clicked on register btn");
                Intent intent = new Intent(Event_in_detail.this,Register_page.class);
                intent.putExtra("event_name",event);
                intent.putExtra("event_url",url);
                startActivity(intent);
            }
        });
    }

    private void get_event_detail(){
        database2=FirebaseDatabase.getInstance();
        reference2= database2.getReference("Event_details");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Event_details cur=ds.getValue(Event_details.class);
                    Log.d("Reva_Event_workflow", "Retrieved event details");

                    event_details.add(cur);
                }
                set_datas();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("get_event_detail", "onCancelled: failed to load event detail");
            }
        });
    }

    public void set_datas(){
        for(Event_details e: event_details){
            if(e.name.equals(event)){
                url=e.url;
                String ns=e.date.substring(1);
                String dat=(ns.substring(0,2))+" "+(ns.substring(2,4))+" "+ns.substring(4);
                description.setText(e.description+"\n\n"+"Venue "+e.venue+"\n\n"+"Date "+dat+"\n\n\n\n");
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Event_in_detail.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}