package com.prernah;


import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.prernah.utils.Tools;

import java.util.Date;

import static com.prernah.utils.Cache.getDatabase;
import static com.prernah.utils.Tools.avatars;

public class Server extends Service {
    public static final int SERVER_DELETE_LOCATION = 1159;
    public static final int SERVER_UPDATE_IMAGE = 1110;
    public static final int SERVER_POST_BID = 1111;
    public static final int SERVER_EDIT_PASSWORD = 1112;
    public static final int SERVER_RATE = 1113;
    public static int SERVER_SIGNUP = 1114;
    public static int SERVER_RESEND_MAIL = 1126;
    public static int SERVER_LOGIN = 1115;
    public static int SERVER_POST_FEED = 1116;
    public static int SERVER_POST_TASK = 1117;
    public static int SERVER_POST_QUESTION = 1118;
    public static int SERVER_DELETE_QUESTION = 1119;
    public static int SERVER_ASSIGN_TASK = 1120;
    public static int SERVER_CANCEL_BID = 1121;
    public static int SERVER_TASK_DONE = 1122;
    public static int SERVER_SAVE_ABOUT = 1123;
    public static int SERVER_ADD_SKILL = 1124;
    public static int SERVER_REMOVE_SKILL = 1125;
    public static int SERVER_RESET_PASSWORD = 1154;
    public static int SERVER_SEND_FEEDBACK = 1157;
    public static int SERVER_SAVE_LOCATION = 1158;

    static Dialog dialog;

    public static void setServerCallCompleteListener(ServerCallCompleteListener listener) {
        if (listener == null) dialog = null;
        mListener = listener;
    }

    public void sendFeedback(String text, String uid) {
        showProgressBar();
        getDatabase()
                .child("/Feedback/" + uid).push().setValue(text)
                .addOnCompleteListener(task -> {
                    notifyListener(task.isSuccessful(),
                            SERVER_SEND_FEEDBACK,
                            "Feedback Sent",
                            "Couldn't Send Feedback", () -> sendFeedback(text, uid));
                });
    }

    private void showProgressBar() {
        try {
            Activity activity = ((Activity) mListener);
            if (mListener != null) {
//                ProgressBar progressBar = activity.findViewById(R.id.progress_bar);
//                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
//                else {
                dialog = Tools.getLoadingDialog(activity);
                dialog.show();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyListener(boolean success, int methodId, String titlePos, String titleNeg, OnRetryListener retryListener) {
        if (mListener != null)
            if (success) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                    mListener.onServerCallSuccess(methodId, null);
                } else mListener.onServerCallSuccess(methodId, titlePos);
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                mListener.onServerCallFailure(methodId, titleNeg, retryListener);
            }
        //TODO send notification
        //not for auth actions
    }

    private static ServerCallCompleteListener mListener;
    private final ServerBinder mBinder = new ServerBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    //method 3
    public void editPassword(FirebaseUser user, String password, String newPassword) {
        showProgressBar();
        if (user != null) {
            final OnRetryListener retry = () -> editPassword(user, password, newPassword);
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful())
                            notifyListener(false, SERVER_EDIT_PASSWORD,
                                    "", "Wrong Password!", null);
                        else user.updatePassword(newPassword)
                                .addOnCompleteListener(task1 -> notifyListener(task1.isSuccessful(),
                                        SERVER_EDIT_PASSWORD, "Password Updated",
                                        "Password Updating Unsuccessful", retry));
                    });
        } else notifyListener(false,
                SERVER_EDIT_PASSWORD, "",
                "Password Updating Unsuccessful", null);

    }

    // method 5
    public void signUp(String name, String email, String password) {
        showProgressBar();
        FirebaseAuth.getInstance().
                createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerificationLink(name);
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        notifyListener(false, SERVER_SIGNUP, "",
                                "Email already in use!, SIGN IN", null);
                    } else notifyListener(false, SERVER_SIGNUP, "",
                            "SignUp Unsuccessful", () -> signUp(name, email, password));
                });
    }

    private void initProfile(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(avatars[(int) (new Date().getTime() % 3)]))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task ->
                            notifyListener(task.isSuccessful(), SERVER_SIGNUP, "SignUp Successful",
                                    "SignUp Unsuccessful", () -> initProfile(name)));
        } else {
            notifyListener(false, SERVER_SIGNUP, "",
                    "SignUp Unsuccessful", () -> initProfile(name));
        }
    }

    public void resendVerificationMail(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            notifyListener(task.isSuccessful(), SERVER_RESEND_MAIL, "Mail Resent",
                    "Mail Couldn't be sent", () -> resendVerificationMail(user));
        });
    }

    private void sendEmailVerificationLink(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            initProfile(name);
                        } else notifyListener(false, SERVER_SIGNUP, "",
                                "SignUp Unsuccessful", () -> sendEmailVerificationLink(name));
                    });
        } else {
            notifyListener(false, SERVER_SIGNUP, "",
                    "SignUp Unsuccessful", () -> sendEmailVerificationLink(name));
        }
    }

    public void sendPasswordResetMail(String emailAddress) {
        showProgressBar();
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    Exception e = task.getException();
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        notifyListener(
                                false, SERVER_RESET_PASSWORD,
                                "", "Invalid Email",
                                null);
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        notifyListener(
                                false, SERVER_RESET_PASSWORD,
                                "", "Email Not Registered",
                                null);
                    } else notifyListener(task.isSuccessful(), SERVER_RESET_PASSWORD,
                            "Password reset email sent", "Some error occurred",
                            () -> sendPasswordResetMail(emailAddress));
                });
    }

    // Method 6
    public void login(String email, String password) {
        showProgressBar();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    Exception e = task.getException();
                    if (e == null) notifyListener(task.isSuccessful(),
                            SERVER_LOGIN,
                            "Login Successful",
                            "Login Unsuccessful", () -> login(email, password));
                    else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        notifyListener(false,
                                SERVER_LOGIN,
                                "",
                                "Invalid Email or Password", null);
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        notifyListener(false,
                                SERVER_LOGIN,
                                "",
                                "Email not registered!, SIGN UP", null);
                    }

                });
    }

    public interface OnRetryListener {
        void retryTask();
    }

    public interface ServerCallCompleteListener {
        void onServerCallSuccess(int methodId, String title);

        void onServerCallFailure(int methodId, String title, OnRetryListener retryListener);

    }

    private interface OnInternalCallCompleteListener {
        void onCallComplete(boolean success, String url);
    }

    public class ServerBinder extends Binder {
        public Server getService() {
            return Server.this;
        }
    }


}
