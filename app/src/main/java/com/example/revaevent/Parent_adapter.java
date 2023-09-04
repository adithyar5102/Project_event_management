package com.example.revaevent;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Parent_adapter extends RecyclerView.Adapter<Parent_adapter.Holder> {

    ArrayList<String> categories=new ArrayList<String>();
    ArrayList<ArrayList<String>> events;

    public Parent_adapter(ArrayList<String> categories, ArrayList<ArrayList<String>> events, Activity activity) {
        this.categories = categories;
        this.events = events;
        this.activity = activity;
    }

    private Activity activity;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_parent_recycleview,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView.setText(categories.get(position));

        Child_adapter ch=new Child_adapter(events.get(position),activity,categories.get(position));
        holder.child_rv.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        holder.child_rv.setAdapter(ch);

    }

    @Override
    public int getItemCount() {
        return categories.size() ;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        RecyclerView child_rv;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.main_activity_parent_rv_textview);
            child_rv=itemView.findViewById(R.id.main_activity_parent_rv_recycleview);
        }
    }
}
