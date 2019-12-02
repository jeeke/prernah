package com.prernah.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.prernah.DonateActivity;
import com.prernah.R;
import com.prernah.viewpagercards.CardItem;
import com.prernah.viewpagercards.CardPagerAdapter;
import com.prernah.viewpagercards.ShadowTransformer;

public class HomeFragment extends Fragment {

    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_home, container, false);
        mViewPager = v.findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(CardItem.CARD_GOALS, "Donate for education", "DONATE NOW", v1 -> startActivity(new Intent(getContext(), DonateActivity.class))));
        mCardAdapter.addCardItem(new CardItem(CardItem.CARD_GOALS, "Adopt a student and provide him education", "ADOPT NOW", v12 -> {
            Intent i = new Intent(getContext(), DonateActivity.class);
            i.putExtra("adopted", true);
            startActivity(i);
        }));
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        mCardShadowTransformer.enableScaling(true);
        return v;
    }


}