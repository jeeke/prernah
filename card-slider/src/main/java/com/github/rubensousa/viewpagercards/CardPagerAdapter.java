package com.github.rubensousa.viewpagercards;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

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
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int resId =  mData.get(position).getCardType() == CardItem.CARD_GOALS ? R.layout.card_goal : R.layout.card_donate;
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
        titleTextView.setText(item.getTitle());
        if(item.getCardType()==CardItem.CARD_GOALS){
            Button btn = view.findViewById(R.id.btn);
            btn.setText(item.getBtnTitle());
            btn.setOnClickListener(item.getBtnListener());
        }else {
            TextView contentTextView = view.findViewById(R.id.contentTextView);
//        TextView preTitleTextView = (TextView) view.findViewById(R.id.preTitleTextView);
//        preTitleTextView.setText(item.getPreTitle());
            contentTextView.setText(item.getText());
        }
    }

}
