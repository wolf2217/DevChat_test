package com.commandcenter.devchat.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.commandcenter.devchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login, btn_register;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        et_email    = findViewById(R.id.login_et_email);
        et_password = findViewById(R.id.login_et_password);

        btn_login    = findViewById(R.id.login_btnLogin);
        btn_register = findViewById(R.id.login_btnRegister);

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
                                currentUser = mAuth.getCurrentUser();
                                Intent chatBoxIntent = new Intent(MainActivity.this, Chatbox_Activity.class);
                                Bundle details = new Bundle();
                                details.putString("user", currentUser.getDisplayName());
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

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}
