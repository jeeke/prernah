package com.prernah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import com.prernah.utils.BaseActivity;
import com.prernah.utils.Tools;


import static com.prernah.utils.Cache.getUser;
import static com.prernah.utils.Tools.launchActivity;
import static com.prernah.utils.Tools.showSnackBar;


public class ContactUs extends BaseActivity {
    EditText feedBackEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("CONTACT US");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.cardView6).setOnClickListener(this::callUs);
        feedBackEdit = findViewById(R.id.edit_feedback);
        findViewById(R.id.btn_feedback).setOnClickListener(v -> {
            String feedBack = feedBackEdit.getText().toString();
            if (feedBack.equals("")) {
                showSnackBar(this, "Please write some message");
            } else if (server != null) {
                server.sendFeedback(feedBack, getUser().getUid());
            }
        });
    }

    private void callUs(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+919679280831"));
        launchActivity(this, intent);
    }

    @Override
    public void onServerCallSuccess(int methodId, String title) {
        super.onServerCallSuccess(methodId, title);
        if (methodId == Server.SERVER_SEND_FEEDBACK) {
            feedBackEdit.setText("");
            Tools.closeKeyboard(this);
        }
    }
}
