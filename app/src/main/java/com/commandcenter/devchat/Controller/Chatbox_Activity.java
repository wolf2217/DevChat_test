package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.commandcenter.devchat.Adapter.FirebaseMessageAdapter;
import com.commandcenter.devchat.Model.ChatboxMessage;
import com.commandcenter.devchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Chatbox_Activity extends AppCompatActivity {

    private EditText et_message;
    private Button btnSend;

    //recyclerview
    private RecyclerView messageRecView;

    //list of messages
    private List<ChatboxMessage> messageList;
    private List<String> userList;
    private FirebaseMessageAdapter messageAdapter;
    //Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;

    private String user;
    private String rank;

    private String curDate;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDataRef = mDatabase.getReference("messages");
        mUsers = mDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());

        messageRecView = findViewById(R.id.chatbox_recView);
        et_message = findViewById(R.id.chatbox_et_message);
        messageList = new ArrayList<>();
        userList = new ArrayList<>();

        Date date = new Date();
        curDate = DateFormat.getDateInstance().format(date);

        btnSend =  findViewById(R.id.chatbox_btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_message.getText().toString())) {

                }else {
                    SimpleDateFormat dFormat = new SimpleDateFormat("hh/mm/ss a");
                    time = dFormat.format(new Date()).toString();
                    ChatboxMessage message = new ChatboxMessage(user, et_message.getText().toString(), rank,  curDate, time);
                    processMessage(user, et_message.getText().toString());
                    mDataRef.child(curDate).push().setValue(message);
                    et_message.setText("");
                }
            }
        });

        mDataRef.child(curDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    ChatboxMessage message = child.getValue(ChatboxMessage.class);
                    if (!messageList.contains(message)) {
                        messageList.add(message);
                       // processMessage(message.getUser(), message.getChatMessage());
                    }
                }
                messageAdapter = new FirebaseMessageAdapter(getApplicationContext(), messageList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Chatbox_Activity.this);
                messageRecView.setLayoutManager(mLayoutManager);
                messageRecView.setItemAnimator(new DefaultItemAnimator());
                messageRecView.setAdapter(messageAdapter);
                mLayoutManager.scrollToPosition(messageList.size() - 1);
              //  messageRecView.scrollToPosition(messageList.size());
               // String test1 = "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("username").getValue().toString();
                rank = dataSnapshot.child("rank").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void processMessage(String user, String message) {

        if (message.startsWith("~")) {
            String[] messageValues = message.split(" ");
            String command = messageValues[0].replace("~", "");

            switch (command) {
                case "ban":

                    break;
                case "silence":

                    break;
                case "block":

                    break;
            }
        }else {
            if (!userList.contains(user)) {
                welcomeUser(user);
                userList.add(user);
            }
        }
    }

    private void welcomeUser(String username) {

        SimpleDateFormat dFormat = new SimpleDateFormat("hh/mm/ss a");
        String curTime = dFormat.format(new Date()).toString();

        ChatboxMessage message = new ChatboxMessage("DevChat Bot", "Welcome to DevChat : " + username, "Moderator Bot", curDate, curTime);
        mDataRef.child(curDate).push().setValue(message);
    }
}
