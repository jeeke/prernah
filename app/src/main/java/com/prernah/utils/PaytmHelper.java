package com.prernah.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.prernah.ChildModel;
import com.prernah.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class PaytmHelper {
    private static String localUrl;

    public static void sentToPaytm(Context context, HashMap<String, String> paramMap, boolean adopted , int count) {
        adoptCount = count;
        PaytmPGService Service = PaytmPGService.getProductionService();
        try {
            int randomNumber = new Random().nextInt(1);
            final File localFile = File.createTempFile("images", "jpg");
            FirebaseStorage.getInstance().getReference().child("images/" + randomNumber + ".jpg").getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> localUrl = localFile.getAbsolutePath())
                    .addOnFailureListener(Throwable::printStackTrace);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PaytmOrder order = new PaytmOrder(paramMap);
        Service.initialize(order, null);
        Dialog dialog = Tools.getLoadingDialog(context);
        dialog.show();
        Service.startPaymentTransaction(context, true, true, new PaytmPaymentTransactionCallback() {
            public void onTransactionResponse(Bundle inResponse) {
                if (adopted) addChildren(context,dialog);
                else onSuccess(context,dialog);
            }

            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(context, "Transaction failed" + inErrorMessage, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            public void networkNotAvailable() {
                Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(context, "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(context, "Unable to load Paytm" + inErrorMessage, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            public void onBackPressedCancelTransaction() {
                Toast.makeText(context, "Transaction cancelled", Toast.LENGTH_LONG).show();
                if (adopted) addChildren(context,dialog);
                else onSuccess(context,dialog);
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(context, "Transaction failed", Toast.LENGTH_LONG).show();
                if (adopted) addChildren(context,dialog);
                else onSuccess(context,dialog);
            }
        });
    }

    private static void onSuccess(Context context,Dialog dialog) {
        dialog.dismiss();
        Dialog d = Tools.getThanksDialog(context);
        ImageView imageView = d.findViewById(R.id.image);
        Glide.with(context).load(localUrl).into(imageView);
        d.findViewById(R.id.share).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, "Please help someone in need....");
            intent.putExtra(Intent.EXTRA_STREAM, localUrl);
            Intent openInChooser = new Intent(intent);
            context.startActivity(openInChooser);
        });
        d.setOnKeyListener((arg0, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                d.dismiss();
            }
            return true;
        });
        d.show();
    }

    private static int adoptCount = 0;
    private static void addChildren(Context context,Dialog dialog) {
        HashSet<String> adopted = new HashSet<>();
        Cache.getDatabase().child("Adopted/" + Cache.getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    try{
                        adopted.add(d.getValue(ChildModel.class).id);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Cache.getDatabase().child("Children").addListenerForSingleValueEvent(new ValueEventListener() {
                    HashMap<String,Object> toAdopt = new HashMap<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String rootPath = "Adopted/" + Cache.getUser().getUid() + "/";
                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            if(adoptCount <= 0) break;
                            ChildModel child = d.getValue(ChildModel.class);
                            if(!adopted.contains(child.id) ){
                                toAdopt.put(rootPath + child.id,child);
                            }
                            adoptCount--;
                        }
                        Cache.getDatabase().updateChildren(toAdopt, (databaseError, databaseReference) -> onSuccess(context,dialog));

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
