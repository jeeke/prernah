package com.prernah;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.prernah.ui.main.HomeTabAdapter;
import com.prernah.utils.CustomViewPager;

import static com.prernah.utils.Tools.CODE_SETTINGS_ACTIVITY;
import static com.prernah.utils.Tools.launchActivityForResult;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
        HomeTabAdapter sectionsPagerAdapter = new HomeTabAdapter(getSupportFragmentManager());
        CustomViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.notification) {
//            launchActivityForResult(activity, new Intent(this, NotificationActivity.class), CODE_NOTIFICATION_ACTIVITY);
//            SharedPrefAdapter.getInstance().setHasNotification();
//            activity.invalidateOptionsMenu();
        if (item.getItemId() == R.id.setting) {
            launchActivityForResult(this, new Intent(this, SettingActivity.class), CODE_SETTINGS_ACTIVITY);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }


}