package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.commandcenter.devchat.R;

public class Chatbox_Activity extends AppCompatActivity {

    EditText et_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        et_message = findViewById(R.id.chatbox_et_message);

    }

    private void welcomeUser(String username) {
        et_message.setText(username);
    }
}
