package com.prernah.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.prernah.BuildConfig;
import com.prernah.R;
import com.prernah.Server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Tools {
    public static Task<HttpsCallableResult> call(Map data, String fn) {
        FirebaseFunctions functions = FirebaseFunctions.getInstance("asia-east2");
        return functions
                .getHttpsCallable(fn)
                .call(data)
                .continueWith(Task::getResult);
    }

    public static final int CODE_SETTINGS_ACTIVITY = 537;
    public static String[] avatars = {
            "https://firebasestorage.googleapis.com/v0/b/esselion-pass.appspot.com/o/avatar2.png?alt=media&token=768b9748-b959-409f-865a-af3ca195d53e",
            "https://firebasestorage.googleapis.com/v0/b/esselion-pass.appspot.com/o/avatar3.png?alt=media&token=1a488954-cd6d-454d-ac53-5e9bfc073145",
            "https://firebasestorage.googleapis.com/v0/b/esselion-pass.appspot.com/o/avatar4.png?alt=media&token=3a5cbca5-cbb2-405f-931d-97012fa14f1e"
    };

    public static Dialog getLoadingDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }

    public static Dialog getThanksDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_thanks);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        return dialog;
    }

    public static void showProgressBar(Activity activity, boolean visibility) {
        ProgressBar progressBar = activity.findViewById(R.id.progress_bar);
        if (progressBar != null && visibility) progressBar.setVisibility(View.VISIBLE);
        else if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter =
                new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formatCount(long count) {
        if (count > 1000000) {
            return (count / 1000000) + "m+";
        } else if (count > 1000) {
            return (count / 1000) + "k+";
        } else return count + "";
    }

    public static String formatDate(Long date) {

        SimpleDateFormat formatter =
                new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public static void redirectToPlayStore(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    public static String elapsedTime(long cDate) {
        long different = new Date().getTime() - cDate;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthsInMilli = daysInMilli * 30;

        long elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;

        String elapsed;
        long e;
        if (elapsedMonths != 0) {
            elapsed = elapsedMonths + " month";
            e = elapsedMonths;
        } else if (elapsedDays != 0) {
            elapsed = elapsedDays + " day";
            e = elapsedDays;
        } else if (elapsedHours != 0) {
            elapsed = elapsedHours + " hour";
            e = elapsedHours;
        } else if (elapsedMinutes != 0) {
            elapsed = elapsedMinutes + " minute";
            e = elapsedMinutes;
        } else {
            elapsed = elapsedSeconds + " second";
            e = elapsedSeconds;
        }
        if (e > 1) elapsed += 's';
        return elapsed + " ago";
    }

    public static void initMinToolbar(AppCompatActivity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void launchActivity(Activity activity, Class className) {
        if (activity != null) activity.startActivity(new Intent(activity, className));
    }

    public static void launchActivity(Activity activity, Intent intent) {
        if (activity != null) activity.startActivity(intent);
    }

    public static void launchActivityForResult(Activity activity, Intent intent, int requestCode) {
        if (activity != null) activity.startActivityForResult(intent, requestCode);
    }

    public static void finishNLaunchActivity(Activity activity, Intent intent) {
        if (activity != null) {
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public static void finishNLaunchActivity(Activity activity, Class className) {
        if (activity != null) {
            activity.startActivity(new Intent(activity, className));
            activity.finish();
        }
    }


    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getCurrentVersionCode() {
        return BuildConfig.VERSION_CODE;
    }


    public static void showSnackBar(Activity activity, String mes) {
        Snackbar.make(activity.findViewById(android.R.id.content), mes, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View view, String mes) {
        Snackbar.make(view, mes, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(Activity activity, String mes, Server.OnRetryListener retryListener) {
        if (retryListener != null) {
            Snackbar.make(activity.findViewById(android.R.id.content)
                    , mes, Snackbar.LENGTH_SHORT)
                    .setAction("RETRY", v -> retryListener.retryTask()).show();
        } else {
            Snackbar.make(activity.findViewById(android.R.id.content)
                    , mes, Snackbar.LENGTH_SHORT).show();
        }

    }

    public static void showSnackBar(View view, String mes, Server.OnRetryListener retryListener) {
        if (retryListener != null) {
            Snackbar.make(view, mes, Snackbar.LENGTH_SHORT)
                    .setAction("RETRY", v -> retryListener.retryTask()).show();
        } else {
            Snackbar.make(view, mes, Snackbar.LENGTH_SHORT).show();
        }
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
