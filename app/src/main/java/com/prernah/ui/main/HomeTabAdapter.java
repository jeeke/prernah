package com.prernah.ui.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class HomeTabAdapter extends FragmentPagerAdapter {
    //    @StringRes
    private static final String[] TAB_TITLES = new String[]{"Home", "Adopted"};

    public HomeTabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return position==0 ? new HomeFragment() : new AdoptedFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }



    @Override
    public int getCount() {
        return 2;
    }
}