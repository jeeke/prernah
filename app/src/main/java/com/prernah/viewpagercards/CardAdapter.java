package com.prernah.viewpagercards;

import androidx.cardview.widget.CardView;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getPrice(int position);

    void setPrice(int position,int amount);

    int getCount();
}
