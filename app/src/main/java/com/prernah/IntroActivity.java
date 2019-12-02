package com.prernah;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("PRERNAH");
        sliderPage2.setDescription("Provide a child education so that he can have future");
        sliderPage2.setImageDrawable(R.drawable.intro3);
        sliderPage2.setBgColor(getResources().getColor(R.color.blue_200));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("PRERNAH");
        sliderPage.setDescription("Provide a child education so that he can have future");
        sliderPage.setImageDrawable(R.drawable.intro1);
        sliderPage.setBgColor(getResources().getColor(R.color.blue_300));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("PRERNAH");
        sliderPage3.setDescription("Provide a child education so that he can have future");
        sliderPage3.setImageDrawable(R.drawable.intro4);
        sliderPage3.setBgColor(getResources().getColor(R.color.blue_400));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("PRERNAH");
        sliderPage4.setDescription("Provide a child education so that he can have future");
        sliderPage4.setImageDrawable(R.drawable.intro2);
        sliderPage4.setBgColor(getResources().getColor(R.color.blue_500));
        addSlide(AppIntroFragment.newInstance(sliderPage4));


        // Hide Skip/Done button.
        showSkipButton(false);
        showStatusBar(false);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}