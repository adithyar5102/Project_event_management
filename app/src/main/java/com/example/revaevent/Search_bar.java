package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Search_bar extends AppCompatActivity {
    ArrayList<Event_details> event_details=new ArrayList<>();
    RecyclerView recyclerView;
    SearchView searchview;
    ArrayList<String> event;
    Recycle_view_str_al rva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        recyclerView=findViewById(R.id.search_view_recycleview);
        searchview=findViewById(R.id.searchView);
        get_event_details();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void get_event_details() {
        FirebaseDatabase database2= FirebaseDatabase.getInstance();
        DatabaseReference reference2= database2.getReference("Event_details");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                event_details.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Event_details cur = ds.getValue(Event_details.class);
                    event_details.add(cur);
                }
                start_recycleview(event_details);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Search_bar.this, "Error loading data\nCheck network connection", Toast.LENGTH_SHORT).show();
                Log.d("get_event_detail", "onCancelled: failed to load event detail");
            }
        });
    }

    private void start_recycleview(ArrayList<Event_details> event_details) {
        event=new ArrayList<>();
        try {
            for(Event_details e:event_details){
                event.add(e.name);
            }
            Log.d("Reva_Event_workflow", "Start recycle view of search bar");
            rva=new Recycle_view_str_al(event,Search_bar.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recyclerView.setAdapter(rva);

        }catch (NullPointerException e){
            Log.d("Check1", "start_recycleview: "+e);
        }
    }

    private void filterList(String s) {
        Log.d("Reva_Event_workflow", "Search bar Filter List ");
        ArrayList<String> filterlist=new ArrayList<>();
        for (String name: event){
            if(name.toLowerCase().contains(s.toLowerCase())){
                filterlist.add(name);
            }
        }
        if(filterlist.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            rva.set_search_data(filterlist,Search_bar.this);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Search_bar.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}