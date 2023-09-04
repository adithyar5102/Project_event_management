package com.example.revaevent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Child_adapter extends RecyclerView.Adapter<Child_adapter.Holder> {
    FirebaseStorage storage;
    StorageReference stref;
    public Child_adapter(ArrayList<String> events, Activity activity,String category) {
        this.events = events;
        this.activity=activity;
        this.category=category;
    }
    Activity activity;
    ArrayList<String> events=new ArrayList<String>();
    String category;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_child_recycleview,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView.setText(events.get(position));
        String ev=events.get(position);

        storage=FirebaseStorage.getInstance();
        stref=storage.getReference().child("event_cover_photo/"+ev);
        try {
            File localfile=File.createTempFile(ev,"jpg");
            stref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    holder.imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.imageView.setImageResource(R.drawable.not_available);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity,Event_in_detail.class);
                intent.putExtra("event_name",ev);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.main_activity_child_rv_textview);
            imageView=itemView.findViewById(R.id.main_activity_child_rv_imageview);
        }
    }
}
