package com.example.revaevent;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycle_view_str_al extends RecyclerView.Adapter<Recycle_view_str_al.Holder> {

    ArrayList<String> data=new ArrayList<>();
    Activity activity;

    public Recycle_view_str_al(ArrayList<String> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    public void set_search_data(ArrayList<String> data,Activity activity){
        this.data=data;
        this.activity=activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_only,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.textView.setText(data.get(position));
        String ev=data.get(position);
        holder.textView.setOnClickListener(new View.OnClickListener() {
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
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView6);
        }
    }
}
