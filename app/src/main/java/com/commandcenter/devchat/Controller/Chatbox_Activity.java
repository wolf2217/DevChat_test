package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    //Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;

    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDataRef = mDatabase.getReference("messages");
        mUsers = mDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());

        messageList = new ArrayList<>();
        Date date = new Date();
        final String curDate = DateFormat.getDateInstance().format(date);

        et_message = findViewById(R.id.chatbox_et_message);
        btnSend =  findViewById(R.id.chatbox_btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_message.getText().toString())) {

                }else {
                    SimpleDateFormat dFormat = new SimpleDateFormat("hh/mm/ss a");
                    String time = dFormat.format(new Date()).toString();
                    ChatboxMessage message = new ChatboxMessage(user, et_message.getText().toString(), curDate, time);
                    mDataRef.child(curDate).push().setValue(message);
                }
            }
        });

        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    String child_values = child.getValue().toString();
                    String[] values = child_values.split(",");
                  //  ChatboxMessage message = new ChatboxMessage(values[2]);
                   // messageList.add(message);

                }
                String test1 = "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void welcomeUser(String username) {
        et_message.setText(username);
    }
}
