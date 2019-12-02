package com.prernah.viewpagercards;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.prernah.R;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getPrice(int position) {
        return mData.get(position).getPrice();
    }

    @Override
    public void setPrice(int position,int amt) {
        mData.get(position).setPrice(amt);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int resId = mData.get(position).getCardType() == CardItem.CARD_GOALS ? R.layout.card_goal : R.layout.card_donate;
        if (mData.get(position).getCardType() == -1) resId = R.layout.card_custom_entry;
        View view = LayoutInflater.from(container.getContext())
                .inflate(resId, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        if (item.getCardType() == CardItem.CARD_GOALS) {
            Button btn = view.findViewById(R.id.btn);
            btn.setText(item.getBtnTitle());
            btn.setOnClickListener(item.getOnClickListener());
            titleTextView.setText(item.getTitle());
        } else if (item.getCardType() == -1) {
            view.findViewById(R.id.entry).setOnClickListener(item.getOnClickListener());
        } else {
            TextView contentTextView = view.findViewById(R.id.contentTextView);
            titleTextView.setText(item.getTitle());
            contentTextView.setText("₹" + item.getPrice());
        }
    }

}
