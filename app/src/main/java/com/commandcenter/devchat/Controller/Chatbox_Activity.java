package com.commandcenter.devchat.Controller;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.commandcenter.devchat.Adapter.FirebaseMessageAdapter;
import com.commandcenter.devchat.Model.ChatboxMessage;
import com.commandcenter.devchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

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
   // private DatabaseReference mDataRef;
    private DatabaseReference mNewMessageRef;
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;

    public String user;
    private String userID;
    private String rank;
    private String status;

    private String curDate;
    private String time;

    NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        curDate = setDate();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
      //  mDataRef = mDatabase.getReference("messages");
        mNewMessageRef = mDatabase.getReference("messages").child(curDate);
        mUsers = mDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());

        messageRecView = findViewById(R.id.chatbox_recView);
        et_message = findViewById(R.id.chatbox_et_message);
        messageList = new ArrayList<>();
        userList = new ArrayList<>();

        //Button click event
        btnSend =  findViewById(R.id.chatbox_btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_message.getText().toString())) {

                }else {
                    SimpleDateFormat dFormat = new SimpleDateFormat("hh:mm:ss a");
                    dFormat.setTimeZone(TimeZone.getDefault());
                    time = dFormat.format(new Date()).toString();
                    if (TextUtils.isEmpty(et_message.getText().toString()) || et_message.getText().toString().length() == 0) {
                        Toast.makeText(Chatbox_Activity.this, "Please enter a message to send!", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        ChatboxMessage message = new ChatboxMessage(user, et_message.getText().toString(), rank,  curDate, time);
                        processMessage(message);

                    }

                }
            }
        });
        //this removes the specific node and all the child nodes from firebase
        // mNewMessageRef.removeValue();

        //Load messages from current date

        mNewMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children) {
                    ChatboxMessage message = child.getValue(ChatboxMessage.class);
                    //processMessage(message);
                    if (!messageList.contains(message)) {
                        messageList.add(message);
                    }
                }
                messageAdapter = new FirebaseMessageAdapter(getApplicationContext(), messageList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Chatbox_Activity.this);
                messageRecView.setLayoutManager(mLayoutManager);
                messageRecView.setItemAnimator(new DefaultItemAnimator());
                messageRecView.setAdapter(messageAdapter);
                mLayoutManager.scrollToPosition(messageList.size() - 1);
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
                status = dataSnapshot.child("status").getValue().toString();
                welcomeUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //get current user
    private String getUser() {

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String thisUser = dataSnapshot.child("username").getValue().toString();
                user = thisUser;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user;
    }
    //set time zone for the date
    private String setDate() {

        String thisDate = "";
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Florida"));
        DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/Florida"));
        thisDate = formatter.format(cal.getTime());
        return thisDate;

    }
    // process message before it leaves
    private void processMessage(ChatboxMessage message) {

        notification.setSmallIcon(R.drawable.ic_person);
        notification.setTicker("New DevChat Message");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("This is the title");
        notification.setContentText("You have a new message on DevChat");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());

        String curMessage = message.getChatMessage();//gets message
        String curUser = message.getUser();//gets user
        String curRank = message.getRank();//gets rank

        switch (curRank) {
            case "Admin"://user is Admin and has more privilages
            case "Owner"://user is Owner and has more privilages
                if (curMessage.startsWith("~")) {
                    String[] messageDetails = curMessage.split(" ");//split the current message on the space to get the command(param 0), the username(param 1) i.e ~ban Inked
                    switch(messageDetails[0]) {
                        //commands
                        case "~ban":
                            //set user status
                            break;
                        case "~kick":

                            break;
                        case "~silence":

                            break;
                        case "~warn":

                            break;
                    }
                }else {
                    mNewMessageRef.push().setValue(message);
                    et_message.setText("");
                }
                break;
            case "Moderator":

                break;

            default: //all other user privilages just send the message , when we decide to add more ranks we will put them here to process.
                     //maybe we can add different color text for different ranks.
                String[] ban_words = new String[] {"shit", "fuck", "dick", "pussy", "asshole", "ass"};
                for (int i = 0; i < curMessage.length(); i++) {
                    for (int x = 0; x < ban_words.length; x++) {
                        if (curMessage.equalsIgnoreCase(ban_words[x])) {
                            curMessage.replace(ban_words[i], "piggy");
                        }
                    }

                }
                ChatboxMessage newMessage = new ChatboxMessage(message.getUser(), curMessage,message.getRank(), message.getDate(), message.getTime());
                mNewMessageRef.push().setValue(newMessage);
                et_message.setText("");
                break;

        }
    }

    private void welcomeUser(String username) {

        SimpleDateFormat dFormat = new SimpleDateFormat("hh:mm:ss a");
        String curTime = dFormat.format(new Date()).toString();

        ChatboxMessage message = new ChatboxMessage("DevChat Bot", "Welcome to DevChat : " + user + " is now ONLINE!", "Moderator Bot", curDate, curTime);
        mNewMessageRef.push().setValue(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUser();
        setStatus("Online");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  setStatus("Offline");
       // mAuth.signOut();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setStatus("Offline");
        mAuth.signOut();
    }

    private void setStatus(String status) {

        SimpleDateFormat dFormat;
        ChatboxMessage message;

        switch(status) {
            case "Online":
                mUsers.child("status").setValue("Online");
            /*    dFormat = new SimpleDateFormat("hh/mm/ss a");
                dFormat.setTimeZone(TimeZone.getDefault());
                time = dFormat.format(new Date()).toString();
                String  date = setDate();
                message = new ChatboxMessage("DevChat Bot ", user + " is now Online", "BOT", date, time);
                mNewMessageRef.push().setValue(message);*/

                break;
            case "Offline":
                mUsers.child("status").setValue("Offline");
                dFormat = new SimpleDateFormat("hh/mm/ss a");
                dFormat.setTimeZone(TimeZone.getDefault());
                time = dFormat.format(new Date()).toString();
                String date = setDate();
                message = new ChatboxMessage("DevChat Bot ",  user + " is now Offline", "BOT", date, time);
                mNewMessageRef.push().setValue(message);
                break;
            case "Banned":

                break;
            case "Silenced":

                break;
        }
    }
}
