package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.commandcenter.devchat.R;

public class ActivityRegister extends AppCompatActivity {

    EditText et_email, et_password, et_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email    = findViewById(R.id.register_et_email);
        et_password = findViewById(R.id.register_et_password);
        et_username = findViewById(R.id.register_et_username);

        Intent intent = getIntent();
        Bundle details = intent.getExtras();

        if (details != null) {
            String[] values = intent.getStringArrayExtra("details");
            et_email.setText(values[0]);
            et_password.setText(values[1]);
        }
    }

    private void SignUp(String email, String password) {

    }
}
