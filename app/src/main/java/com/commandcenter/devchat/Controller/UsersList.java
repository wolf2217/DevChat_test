package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.commandcenter.devchat.Model.User;
import com.commandcenter.devchat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsers;
    private FirebaseUser currentUser;
    private RecyclerView userRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("users");
        currentUser = mAuth.getCurrentUser();

        userRecView = findViewById(R.id.usersList_RecView);
        userRecView.setHasFixedSize(true);
        userRecView.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User, UsersViewHolder> allUsersAdapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(
                User.class,
                R.layout.single_user_row_layout,
                UsersViewHolder.class,
                mUsers) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, User users, int position) {

                if (mAuth.getCurrentUser() != null) {
                    String userUID = mAuth.getCurrentUser().getUid();

                    if (!userUID.equalsIgnoreCase(users.getUserID())) {
                        viewHolder.setName(users.getUsername());
                        viewHolder.setStatus(users.getStatus());
                        viewHolder.setGender(users.getGender());
                    }
                }

            }
        };

        userRecView.setAdapter(allUsersAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (mAuth.getCurrentUser() != null) {

            switch (item.getItemId()) {
                case R.id.logout:
                    Toast.makeText(this, "User signed out!", Toast.LENGTH_SHORT).show();
                    signOut();
                    Intent intent = new Intent(UsersList.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navFriends:

                    return true;
                case R.id.navUsers:

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void signOut() {

        if (currentUser != null) {
            mUsers.child(currentUser.getUid()).child("status").setValue("Offline");
            mAuth.signOut();
            Intent intent = new Intent(UsersList.this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(UsersList.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView tv_DisplayName = mView.findViewById(R.id.single_user_tv_displayName);
            tv_DisplayName.setText(name);
        }

        public void setStatus(String status) {
            TextView tv_userStatus = mView.findViewById(R.id.single_user_tv_Status);
            tv_userStatus.setText(status);
        }

        public void setGender(String gender) {
            TextView tv_userGender = mView.findViewById(R.id.single_user_tv_Gender);
            tv_userGender.setText(gender);
        }

    }
}
