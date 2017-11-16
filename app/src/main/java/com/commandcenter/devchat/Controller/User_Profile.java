package com.commandcenter.devchat.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.commandcenter.devchat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity {

    private TextView tv_displayName, tv_Rank, tv_Status;
    private Button btn_sed_request;

    private DatabaseReference mUsersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);

        String userID = getIntent().getStringExtra("user_id");

        tv_displayName = (TextView) findViewById(R.id.user_profile_tv_displayName);
        tv_Rank        = (TextView) findViewById(R.id.user_profile_tv_Rank);
        tv_Status      = (TextView) findViewById(R.id.user_profile_tv_Status);
        btn_sed_request = (Button) findViewById(R.id.user_profile_btnSendRequest);
        mUsersData = FirebaseDatabase.getInstance().getReference("users").child(userID);

        mUsersData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String displayName = dataSnapshot.child("username").getValue().toString();
                String rank = dataSnapshot.child("rank").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                tv_displayName.setText(displayName);
                tv_Rank.setText(rank);
                tv_Status.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
