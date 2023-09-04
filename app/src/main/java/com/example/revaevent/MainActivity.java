package com.example.revaevent;
//bitmap to store and represent pixels
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView ongoing_rv;
    SliderView sliderview;
    RecyclerView recyclerView;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseDatabase database2;
    DatabaseReference reference;
    DatabaseReference reference2;
    CardView search;
    ImageView profile;
    ImageView registered;
    ImageView home;

    FirebaseAuth mAuth;
    Student user_detail;
    ArrayList<Event_details> event_details=new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliderview=findViewById(R.id.sliderView2);
        recyclerView=findViewById(R.id.main_activity_recycleview);
        ongoing_rv=findViewById(R.id.main_activity_ongoing_recycleview);
        search=findViewById(R.id.card_view_search);
        profile=findViewById(R.id.main_activity_down_navigation_bar_profile);
        registered=findViewById(R.id.main_activity_down_navigation_bar_registered);
        home=findViewById(R.id.main_activity_down_navigation_bar_home);
        mAuth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();



        profile.setImageResource(R.drawable.baseline_person_24_grey);
        registered.setImageResource(R.drawable.baseline_format_list_bulleted_24_grey);
        home.setImageResource(R.drawable.baseline_home_24);


      //  get_user_detail();
        try {
            get_event_detail();
        }catch (Exception e){
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            start_slider_view();
        }catch (Exception e){
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
            return;
        }





        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Reva_Event_workflow", "Register btn");
                Intent intent = new Intent(MainActivity.this, Registered_events.class);
                intent.putExtra("uid",mAuth.getUid());
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Reva_Event_workflow", "Profile btn");
                Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Reva_Event_workflow", "search btn");
                Intent intent=new Intent(MainActivity.this,Search_bar.class);
                startActivity(intent);
                finish();
            }
        });

    }


   /* private void get_user_detail(){
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Student cur=ds.getValue(Student.class);
                    assert cur != null;
                    if(cur.uid.equals(mAuth.getUid())){
                        user_detail= cur;
                    }
                }

                // g.setUser_detail(user_detail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("get_user_detail", "onCancelled: failed to load");
            }
        });
    }

    */

    private void get_event_detail(){
        database2=FirebaseDatabase.getInstance();
        reference2= database2.getReference("Event_details");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                event_details.clear();
                LocalDate mydate=LocalDate.now().minusDays(1);
                DateTimeFormatter dformat = DateTimeFormatter.ofPattern("ddMMyyyy");
                String formatted=mydate.format(dformat);
                String nformatted="d"+formatted;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Event_details cur=ds.getValue(Event_details.class);
                    if(cur.date.equals(nformatted)){
                        delete_document(cur.name);
                        break;
                    }
                    else{
                        event_details.add(cur);
                    }
                }
                Log.d("Reva_Event_workflow", "Event details retrieved");
                start_recycleview(event_details);
                start_ongoing(event_details);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error loading data\nCheck network connection", Toast.LENGTH_SHORT).show();
                Log.d("get_event_detail", "onCancelled: failed to load event detail");
            }
        });
    }
    public void start_sliderview(ArrayList<Drawable> images){
        Log.d("Reva_Event_workflow", "Start slider view");
        Slider_view_adapter sva=new Slider_view_adapter(images);
        sliderview.setSliderAdapter(sva);
        sliderview.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderview.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderview.startAutoCycle();
    }

    public void start_recycleview(@NonNull ArrayList<Event_details> event_details){
        Log.d("Reva_Event_workflow", "Start recycle view");
        ArrayList<String> category=new ArrayList<String>();
        for(Event_details e :event_details){
            if(!category.contains(e.category)){
                Log.d("check1", "start_recycleview: "+e.category);
                category.add(e.category);
            }
        }
        ArrayList<ArrayList<String>> events= new ArrayList<>();
        for(int i=0;i<category.size();i++){
            ArrayList<String> temp=new ArrayList<>();
            for(int j=0;j<event_details.size();j++){
                Log.d("check1", "start_recycleview: ");
                if(event_details.get(j).category.equals(category.get(i))){
                    Log.d("check1", "start_recycleview: "+event_details.get(j).name);
                    temp.add(event_details.get(j).name);
                }
            }
            events.add(temp);
        }
        Parent_adapter pa=new Parent_adapter(category,events,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.suppressLayout(true);
        recyclerView.setAdapter(pa);
    }

    private void start_ongoing(@NonNull ArrayList<Event_details> event_details){
        Log.d("Reva_Event_workflow", "get on going event details");
        ArrayList<String> ongoing=new ArrayList<String>();
        ArrayList<String> ongoing_cat=new ArrayList<String>();
        LocalDate mydate=LocalDate.now();
        DateTimeFormatter dformat = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formatted=mydate.format(dformat);
        String nformatted="d"+formatted;
        //@SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        //Event_details a=event_details.get(0);
        if(!event_details.isEmpty()){
            for(Event_details e:event_details){
                if(e.date.equals(nformatted)){
                    ongoing.add(e.name);
                    ongoing_cat.add(e.category);
                }
            }
        }
        if(ongoing_cat.isEmpty() && ongoing.isEmpty()){
            ongoing_cat.add("none");
            ongoing.add("No Events For Today");
        }
        Child_adapter_onging ca=new Child_adapter_onging(ongoing,ongoing_cat,MainActivity.this);
        ongoing_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ongoing_rv.setAdapter(ca);
    }

    public void start_slider_view(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ArrayList<Bitmap> image=new ArrayList<>();
        ArrayList<Drawable> images=new ArrayList<>();
        for(int i=1;i<=3;i++){
            String impath="slider_images/slider_image"+i+".jpg";
            StorageReference img=storage.getReference().child(impath);
            try{
                Log.d("Reva_Event_workflow", "Retrieving data for slider view ");
                File localfile=File.createTempFile("tempfille",".jpg");
                img.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap im=BitmapFactory.decodeFile(localfile.getAbsolutePath());
                        Drawable d= new BitmapDrawable(getResources(),im);
                        images.add(d);
                        start_sliderview(images);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("check1", "onFailure: sliderview");
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
      //  Log.d("check1", "start_slider_view: start");

    }


    public void delete_document(String doc){
        Log.d("Reva_Event_workflow", "Deleting outdated data");
        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Event_details").child(doc);
        DatabaseReference stdref=FirebaseDatabase.getInstance().getReference("student_registered").child(doc);
        Task del=db.removeValue();
        del.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Log.d("Application status", "onComplete: deleted "+doc);
                }
                else{
                    Log.d("Application status", "onComplete: deleted "+doc+" unsuccessful");
                }
            }
        });
        Task stddel=stdref.removeValue();
        stddel.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Log.d("Application status", "onComplete: deleted "+doc);
                }
                else{
                    Log.d("Application status", "onComplete: deleted "+doc+" unsuccessful");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
     /*   MainActivity.this.finish();
        System.exit(0);

      */
    }
}