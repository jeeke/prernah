package com.prernah.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prernah.MainActivity;
import com.prernah.MyApplication;

import java.util.ArrayList;

public class Cache {
    private static DatabaseReference mDataBase;

    public static FirebaseUser mUser;
    private static String token;

    public static void emptyCache() {
        token = null;
        mUser = null;
    }

    public static void getToken(OnTokenReceivedListener listener) {
        if (token == null) {
            getUser().getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                        } else {
                            token = "null";
                            Log.e("Cache getToken Error", "Token could not be fetched");
                        }
                        listener.onTokenReceived(token);
                    });
        } else listener.onTokenReceived(token);
    }

    public static FirebaseUser getUser() {
        if (mUser == null) {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            if (mUser == null) {
                Context context = MyApplication.getInstance().getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }
        return mUser;
    }

    public static DatabaseReference getDatabase() {
        if (mDataBase == null) {
            mDataBase = FirebaseDatabase.getInstance().getReference();
        }
        return mDataBase;
    }

    public interface OnTokenReceivedListener {
        void onTokenReceived(String token);
    }
}
