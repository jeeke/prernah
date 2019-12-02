package com.prernah.cards;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.prernah.ChildModel;
import com.prernah.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    private ArrayList<ChildModel> children;

    public SliderAdapter(ArrayList<ChildModel> list) {
        this.children = list;
    }

    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);
        return new SliderCard(view);
    }

    @Override
    public void onBindViewHolder(@NotNull SliderCard holder, int position) {
        holder.setContent(children.get(position % children.size()).image);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

}
