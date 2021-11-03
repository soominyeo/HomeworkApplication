package com.example.homeworkapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            try {
                                boolean registerResult = intent.getBooleanExtra("result", false);
                                Toast.makeText(getApplicationContext(),
                                        getString((registerResult) ? R.string.login_toast_register_succeed : R.string.login_toast_register_failed),
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
    private EditText idEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEditText = (EditText) this.findViewById(R.id.et_login_id);
        passwordEditText = (EditText) this.findViewById(R.id.et_login_password);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signIn();
                    return true;
                }
                return false;
            }
        });

        List<InputFilter> filters = new ArrayList<InputFilter>(Arrays.asList(idEditText.getFilters()));
        filters.add(0, new IDInputFilter());
        idEditText.setFilters(filters.toArray(new InputFilter[0]));
        filters = new ArrayList<InputFilter>(Arrays.asList(passwordEditText.getFilters()));
        filters.add(0, new PasswordInputFilter());
        passwordEditText.setFilters(filters.toArray(new InputFilter[0]));
    }

    public void onSignIn(View view) {
        signIn();
    }

    private void signIn() {
        String id = idEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        try {
            if (User.authenticate(getApplicationContext(), id, password)) {
                MainApplication app = (MainApplication) getApplication();
                app.setUser(User.getUser(getApplicationContext(), id));
                app.setLoggedIn(true);
                toMain();
            } else {
                dialogBuilder.setMessage(R.string.login_error_authenticate);
                dialogBuilder.setPositiveButton(R.string.all_confirm, null);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }

        } catch (User.UserDoesNotExistsException e) {
            dialogBuilder.setMessage(R.string.login_error_id);
            dialogBuilder.setPositiveButton(R.string.login_error_id_positive, null);
            dialogBuilder.setNegativeButton(R.string.all_cancel, null);
            final AlertDialog dialog = dialogBuilder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    signUp();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    public void onSignUp(View view) {
        signUp();
    }

    private void signUp() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        intent.putExtra("ACTIVITY_SOURCE", this.getClass().getName());
        intent.putExtra("id", idEditText.getText().toString());
        intent.putExtra("password", passwordEditText.getText().toString());

        resultLauncher.launch(intent);
    }

    public void onToMain(View view) {
        toMain();
    }

    private void toMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("ACTIVITY_SOURCE", this.getClass().getName());

        resultLauncher.launch(intent);
    }
}