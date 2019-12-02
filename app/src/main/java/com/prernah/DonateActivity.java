package com.prernah;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.prernah.utils.Cache;
import com.prernah.utils.PaytmHelper;
import com.prernah.utils.Tools;
import com.prernah.viewpagercards.CardItem;
import com.prernah.viewpagercards.CardPagerAdapter;
import com.prernah.viewpagercards.ShadowTransformer;

import java.util.HashMap;
import java.util.Map;

import static com.prernah.utils.Tools.dpToPx;
import static com.prernah.utils.Tools.showSnackBar;


public class DonateActivity extends AppCompatActivity {

    private Button mButton;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    public void checkPermissionNCheckOut() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        } else genChecksum();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        genChecksum();
    }

    public void genChecksum() {
        String k = Cache.getDatabase().child("Donations/" + Cache.getUser().getUid()).push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", k);
        map.put("txn_amt", mCardAdapter.getPrice(mViewPager.getCurrentItem()));
        Tools.call(map, "genChecksum").addOnCompleteListener(t -> {
            if (t.isSuccessful() && t.getResult() != null) {
                Log.e("Return", t.getResult().getData().toString());
                HashMap<String, Object> m = (HashMap<String, Object>) t.getResult().getData();
                HashMap<String, String> paytmMap = new HashMap<>();
                for (String s : m.keySet()) {
                    paytmMap.put(s, m.get(s).toString());
                }
                PaytmHelper.sentToPaytm(this, paytmMap,adopted,1);
            }
        });
    }

    boolean adopted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        adopted = getIntent().getBooleanExtra("adopted", false);
        mViewPager = findViewById(R.id.viewPager);
        mButton = findViewById(R.id.btn_pay);
        mButton.setOnClickListener(v -> checkPermissionNCheckOut());
        mCardAdapter = new CardPagerAdapter();
        if (adopted) {
            mCardAdapter.addCardItem(new CardItem(CardItem.CARD_ADOPT, "Education", 500));
        } else {
            mCardAdapter.addCardItem(new CardItem(500,v -> {
                EditText input = new EditText(DonateActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                int pad = dpToPx(24);
                int p = dpToPx(16);
                input.setPadding(pad, pad, pad, p);
                new MaterialAlertDialogBuilder(DonateActivity.this, R.style.AlertDialogTheme).setTitle("Enter Amount").setView(input)
                        .setPositiveButton("OK", (dialog, which) -> {
                            try {
                                int amt = Integer.parseInt(input.getText().toString());
                                mCardAdapter.setPrice(mViewPager.getCurrentItem(),amt);
                                TextView t = v.findViewById(R.id.contentTextView);
                                t.setText("₹" + amt);
                            } catch (Exception e) {
                                showSnackBar(DonateActivity.this, "Please Enter Valid Amount!");
                            }
                        })
                        .show();
                input.requestFocus();
            }));
            mCardAdapter.addCardItem(new CardItem(CardItem.CARD_DONATE, "Stationary", 50));
            mCardAdapter.addCardItem(new CardItem(CardItem.CARD_DONATE, "Education for 1 month", 100));
            mCardAdapter.addCardItem(new CardItem(CardItem.CARD_DONATE, "Education for 2 month", 200));
            mCardAdapter.addCardItem(new CardItem(CardItem.CARD_DONATE, "Education for 3 month", 300));

        }
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        mCardShadowTransformer.enableScaling(true);
    }

}
