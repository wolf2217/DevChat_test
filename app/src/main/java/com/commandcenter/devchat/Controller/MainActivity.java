package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.commandcenter.devchat.Model.ChatboxMessage;
import com.commandcenter.devchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login, btn_register;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsers;
    private DatabaseReference mMessages;
    private FirebaseUser currentUser;

    String[] values;

    private String user;
    private String rank;
    private String time;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("users");
        mMessages = mDatabase.getReference("messages");

        et_email    = findViewById(R.id.login_et_email);
        et_password = findViewById(R.id.login_et_password);

        btn_login    = findViewById(R.id.login_btnLogin);
        btn_register = findViewById(R.id.login_btnRegister);


        Intent intent = getIntent();
        Bundle details = intent.getExtras();

        if (details != null) {
            values = intent.getStringArrayExtra("details");
            et_email.setText(values[0]);
            et_password.setText(values[1]);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    loginUser(et_email.getText().toString(), et_password.getText().toString());
                }else {
                    Intent chatBoxIntent = new Intent(MainActivity.this, Chatbox_Activity.class);
                    startActivity(chatBoxIntent);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, ActivityRegister.class);
                startActivity(registerIntent);
            }
        });

    }


    private void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All Fields Required", Toast.LENGTH_SHORT).show();
            return;
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //sign in successful
                                setStatus("Online");
                                Intent chatBoxIntent = new Intent(MainActivity.this, Chatbox_Activity.class);
                                Bundle details = new Bundle();
                                details.putString("user", user);
                                chatBoxIntent.putExtras(details);
                                startActivity(chatBoxIntent);
                            }else {
                                //sign in failure
                                String[] deatails = new String[] {et_email.getText().toString(), et_password.getText().toString()};

                                Toast.makeText(MainActivity.this, "Email not found, Please Register a new Account!", Toast.LENGTH_SHORT).show();
                                Intent registerIntent = new Intent(MainActivity.this, ActivityRegister.class);
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("details", deatails);
                                registerIntent.putExtras(bundle);
                                startActivity(registerIntent);
                            }
                        }
                    });
        }
    }

    private void getUser() {
        mUsers.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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


    private void setStatus(String status) {

        SimpleDateFormat dFormat;
        ChatboxMessage message;

        switch(status) {
            case "Online":
                mUsers.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
            /*    dFormat = new SimpleDateFormat("hh/mm/ss a");
                dFormat.setTimeZone(TimeZone.getDefault());
                time = dFormat.format(new Date()).toString();
                date = setDate();
                message = new ChatboxMessage("DevChat Bot ", user + " is now Online", "BOT", date, time);
                mMessages.child(date).push().setValue(message);*/

            break;
            case "Offline":
                mUsers.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
                dFormat = new SimpleDateFormat("hh:mm:ss a");
                dFormat.setTimeZone(TimeZone.getDefault());
                time = dFormat.format(new Date()).toString();
                date = setDate();
                message = new ChatboxMessage("DevChat Bot ",  user + " is now Offline", "BOT", date, time);
                mMessages.child(date).push().setValue(message);
                break;
            case "Banned":

                break;
            case "Silenced":

                break;
        }
    }

    private String setDate() {

        String thisDate = "";
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Florida"));
        DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/Florida"));
        thisDate = formatter.format(cal.getTime());
        return thisDate;

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, Chatbox_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user", currentUser.getUid());
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // setStatus("Offline");
        //mAuth.signOut();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //setStatus("Offline");
       // mAuth.signOut();
    }
}
