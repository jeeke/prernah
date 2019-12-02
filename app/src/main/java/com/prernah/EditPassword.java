package com.prernah;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.prernah.utils.BaseActivity;
import com.prernah.utils.Tools;

import static com.prernah.utils.Cache.getUser;
import static com.prernah.utils.Tools.showSnackBar;

public class EditPassword extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        Tools.initMinToolbar(this, "Edit Password");
        initFields();
    }

    EditText pass, newPass;
    Button save;

    private void initFields() {
        pass = findViewById(R.id.pass);
        newPass = findViewById(R.id.newPass);
        save = findViewById(R.id.save);
        save.setOnClickListener(v -> checkFields());
    }

    String password, newPassword;

    private void checkFields() {
        password = pass.getText().toString();
        newPassword = newPass.getText().toString();
        if (server == null) ;
        else if (password.equals("")) {
            showSnackBar(this, "Passwords can not be empty");
        } else if (newPassword.length() < 8) {
            showSnackBar(this, "Password less than 8 characters!");
        } else {
            server.editPassword(getUser(), password, newPassword);
        }
    }

    @Override
    public void onServerCallSuccess(int methodId, String title) {
        super.onServerCallSuccess(methodId, title);
        if (methodId == Server.SERVER_EDIT_PASSWORD) {
            new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle("Password Updated")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();
        }
    }
}
