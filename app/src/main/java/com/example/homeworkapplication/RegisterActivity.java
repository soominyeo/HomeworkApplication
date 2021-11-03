package com.example.homeworkapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText idEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private RadioGroup termRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView textView = findViewById(R.id.text_register_term_content);
        textView.setMovementMethod(new ScrollingMovementMethod());

        idEditText = this.findViewById(R.id.et_register_id);
        passwordEditText = this.findViewById(R.id.et_register_password);
        nameEditText = this.findViewById(R.id.et_register_name);
        phoneNumberEditText = this.findViewById(R.id.et_register_phone_number);
        addressEditText = this.findViewById(R.id.et_register_address);
        termRadioGroup = this.findViewById(R.id.grp_register_term);

        // add filters
        List<InputFilter> filters = new ArrayList<>(Arrays.asList(idEditText.getFilters()));
        filters.add(0, new IDInputFilter());
        idEditText.setFilters(filters.toArray(new InputFilter[0]));
        filters = new ArrayList<>(Arrays.asList(passwordEditText.getFilters()));
        filters.add(0, new PasswordInputFilter());
        passwordEditText.setFilters(filters.toArray(new InputFilter[0]));

        // add validation hints
        idEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!IDInputFilter.isValid(charSequence)) {
                    idEditText.setError(getResources().getString(R.string.register_error_id));
                } else {
                    idEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!PasswordInputFilter.isValid(charSequence)) {
                    passwordEditText.setError(getResources().getString(R.string.register_error_password));
                } else {
                    passwordEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        // restore id and password
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        idEditText.setText(data.getString("id", ""));
        passwordEditText.setText(data.getString("password", ""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_register_confirm) {
            confirm();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirm() {
        if(!validate()) {
            return;
        }

        User user = new User(idEditText.getText().toString(),
                passwordEditText.getText().toString(),
                nameEditText.getText().toString(),
                phoneNumberEditText.getText().toString(),
                addressEditText.getText().toString(),
                termRadioGroup.getCheckedRadioButtonId() == R.id.btn_register_accept
        );

        boolean saveResult = User.saveUser(getApplicationContext(), user);
        Log.d("result", saveResult ? "true" : "false");
        Log.d("source", getIntent().getStringExtra("ACTIVITY_SOURCE"));

        try {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", saveResult);
            resultIntent.putExtra("id", user.getId());
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean validate() {
        return IDInputFilter.isValid(idEditText.getText().toString())
                && PasswordInputFilter.isValid(passwordEditText.getText().toString());
    }

}