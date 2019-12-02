package com.prernah.cards;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prernah.R;

public class SliderCard extends RecyclerView.ViewHolder{
    private final ImageView imageView;

    public SliderCard(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
    }

    void setContent(String image) {
        Glide.with(imageView.getContext()).load(image).into(imageView);
    }

}