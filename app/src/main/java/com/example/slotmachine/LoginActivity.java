package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private EditText usernameBox, passwordBox;
    DatabaseHelper slotmachineDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameBox = findViewById(R.id.userNameBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        slotmachineDB = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(usernameBox.getText().toString(), passwordBox.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validate(String username, String password) {
        if (slotmachineDB.getPassword(username)!=null && password.equals(slotmachineDB.getPassword(username))) {
            Common.playingUser = username;
            Common.SCORE = slotmachineDB.getCoins(username);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(LoginActivity.this, "Wrong credentials!", Toast.LENGTH_LONG).show();

        }
    }

}