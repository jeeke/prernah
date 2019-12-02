package com.prernah.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.prernah.MyApplication;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefAdapter {
    private static final String PASS_SHARED_PREF = "PASS_PREF";
    private static final String TOKEN = "token";
    private static final String INTRO_STATUS = "intro_shown";
    //    private static final String UNSEEN_NOTIFICATION = "unseen_notification";
    private final static SharedPrefAdapter sharedPrefAdapter = new SharedPrefAdapter();
    private SharedPreferences prefs;

    private SharedPrefAdapter() {
        prefs = MyApplication.getInstance().getSharedPreferences(PASS_SHARED_PREF, MODE_PRIVATE);
    }

    public static SharedPrefAdapter getInstance() {
        return sharedPrefAdapter;
    }

    public boolean isIntroShown() {
        return prefs.getBoolean(INTRO_STATUS, false);
    }

    public void setIntroShown() {
        prefs.edit().putBoolean(INTRO_STATUS, true).apply();
    }

    public String getToken() {
        return prefs.getString(TOKEN, null);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

}
