package com.example.revaevent;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class Slider_view_adapter extends SliderViewAdapter<Slider_view_adapter.Holder> {

   // ArrayList<Integer> images=new ArrayList<Integer>();
    ArrayList<Drawable> image=new ArrayList<>();

    public Slider_view_adapter(ArrayList<Drawable> images) {
        this.image = images;
    }


    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_items,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        //viewHolder.imageview.setImageResource(images.get(position));

       // viewHolder.imageview.setImage(Bitmap.createScaledBitmap(image.get(position), viewHolder.imageview.getWidth(), viewHolder.imageview.getHeight(), false));

        viewHolder.imageview.setImageDrawable(image.get(position));

    }

    @Override
    public int getCount() {
        return image.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageview;
        public Holder(View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.image_view_slider_item);
        }
    }
}
