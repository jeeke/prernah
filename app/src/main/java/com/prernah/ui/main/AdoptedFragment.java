package com.prernah.ui.main;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.prernah.ChildModel;
import com.prernah.R;
import com.prernah.cards.SliderAdapter;
import com.prernah.utils.Cache;
import com.prernah.utils.Tools;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;

public class AdoptedFragment extends Fragment {

    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    //    private ImageSwitcher mapSwitcher;
    private TextSwitcher grade;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;

    private TextView name1;
    private TextView name2;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;


    public AdoptedFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_adopted, container, false);
        v.setVisibility(View.INVISIBLE);
        try {
            initChildList(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private SliderAdapter sliderAdapter;
    ArrayList<ChildModel> list = new ArrayList<>();

    ArrayList<ChildModel> initChildList(View v) {
        list.clear();
        Tools.showProgressBar(getActivity(), true);
        Cache.getDatabase().child("Adopted/" + Cache.getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    list.add(x.getValue(ChildModel.class));
                }
                if (list.size() > 0) {
                    v.setVisibility(View.VISIBLE);
                    sliderAdapter = new SliderAdapter(list);
                    initRecyclerView(v);
                    initCountryText(v);
                    initSwitchers(v);
                }
                Tools.showProgressBar(getActivity(), false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Tools.showProgressBar(getActivity(), false);
            }
        });
        return list;
    }

    private void initRecyclerView(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });
        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setLayoutManager(layoutManger);
        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    private void initSwitchers(View v) {
        grade = (TextSwitcher) v.findViewById(R.id.grade);
        grade.setFactory(new AdoptedFragment.TextViewFactory(R.style.TemperatureTextView, true));
        grade.setCurrentText(list.get(0).grade);

//        placeSwitcher = (TextSwitcher) v.findViewById(R.id.ts_place);
//        placeSwitcher.setFactory(new AdoptedFragment.TextViewFactory(R.style.PlaceTextView, false));
//        placeSwitcher.setCurrentText(places[0]);

//        clockSwitcher = (TextSwitcher) v.findViewById(R.id.ts_clock);
//        clockSwitcher.setFactory(new AdoptedFragment.TextViewFactory(R.style.ClockTextView, false));
//        clockSwitcher.setCurrentText(times[0]);

        descriptionsSwitcher = v.findViewById(R.id.desc);
        descriptionsSwitcher.setInAnimation(getContext(), android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(getContext(), android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new AdoptedFragment.TextViewFactory(R.style.DescriptionTextView, false));
        descriptionsSwitcher.setCurrentText(list.get(0).desc);

    }

    private void initCountryText(View v) {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        name1 = (TextView) v.findViewById(R.id.name_1);
        name2 = (TextView) v.findViewById(R.id.name_2);

        name1.setX(countryOffset1);
        name2.setX(countryOffset2);
        name1.setText(list.get(0).name);
        name2.setAlpha(0f);
    }


    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (name1.getAlpha() > name2.getAlpha()) {
            visibleText = name1;
            invisibleText = name2;
        } else {
            visibleText = name2;
            invisibleText = name1;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }
        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};
        int size = list.size();

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        setCountryText(list.get(pos % size).name, left2right);

        grade.setInAnimation(getContext(), animH[0]);
        grade.setOutAnimation(getContext(), animH[1]);
        grade.setText(list.get(pos % size).grade);
        placeSwitcher.setInAnimation(getContext(), animV[0]);
        placeSwitcher.setOutAnimation(getContext(), animV[1]);
//        placeSwitcher.setText(places[pos % places.length]);
//        clockSwitcher.setInAnimation(getContext(), animV[0]);
        clockSwitcher.setOutAnimation(getContext(), animV[1]);
//        clockSwitcher.setText(times[pos % times.length]);
        descriptionsSwitcher.setText(list.get(pos % size).desc);
        currentPosition = pos;
    }

    private class TextViewFactory implements ViewSwitcher.ViewFactory {
        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @Override
        public View makeView() {
            final TextView textView = new TextView(getContext());
            if (center) {
                textView.setGravity(Gravity.CENTER);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(getContext(), styleId);
            } else {
                textView.setTextAppearance(styleId);
            }
            return textView;
        }

    }
}
