package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.commandcenter.devchat.R;

public class Chatbox_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        Intent intent = getIntent();
        Bundle details = intent.getExtras();

        if (details != null) {
            welcomeUser(details.get("user").toString());
        }
    }

    private void welcomeUser(String username) {

    }
}
