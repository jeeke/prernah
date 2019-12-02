package com.prernah;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prernah.utils.BaseActivity;
import com.prernah.utils.Tools;

import static com.prernah.utils.Tools.launchActivity;
import static com.prernah.utils.Tools.redirectToPlayStore;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Tools.initMinToolbar(this, "SETTINGS");
        initViews();

    }

    private void initViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        findViewById(R.id.textView84).setOnClickListener((View v) -> launchActivity(this, EditPassword.class));
        findViewById(R.id.textView92).setOnClickListener(this::invite);
        findViewById(R.id.textView90).setOnClickListener((View v) -> redirectToPlayStore(this));
        findViewById(R.id.textView94).setOnClickListener((View v) -> openUri("https://facebook.com"));
        findViewById(R.id.textView93).setOnClickListener((View v) -> openUri("https://linkedin.com"));
        findViewById(R.id.textView91).setOnClickListener((View v) -> openUri("https://twitter.com"));
        findViewById(R.id.textView76).setOnClickListener((View v) -> launchActivity(this, PrivacyPolicy.class));
        findViewById(R.id.textView69).setOnClickListener((View v) -> launchActivity(this, TandC.class));
        findViewById(R.id.textView58).setOnClickListener((View v) -> launchActivity(this, ContactUs.class));
        findViewById(R.id.logout).setOnClickListener(v -> {
            MainActivity.signOut(this);
            finish();
        });
        TextView name, contact, contactHead, version;
        version = findViewById(R.id.versionName);
        version.setText("Version " + Tools.getVersionName());
        name = findViewById(R.id.textView85);
        contact = findViewById(R.id.textView87);
        contactHead = findViewById(R.id.textView79);
        if (user != null) {
            name.setText(user.getDisplayName());
            if (user.getEmail() != null) contact.setText(user.getEmail());
            else {
                contact.setText(user.getPhoneNumber());
                contactHead.setText("Phone No");
            }
        }
    }


    private void openUri(String uri) {
        launchActivity(this, new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    private void invite(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "PASS");
        intent.putExtra(Intent.EXTRA_TEXT, "Checkout this awesome app! \nhttps://play.google.com/store/apps/details?id=com.esselion.pass");
        intent.setType("text/plain");
        launchActivity(this, Intent.createChooser(intent, "Share PASS"));
    }
}