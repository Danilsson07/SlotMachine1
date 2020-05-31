package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameBox, passwordBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameBox = findViewById(R.id.userNameBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(usernameBox.getText().toString(), passwordBox.getText().toString());
            }
        });

    }

    private void validate(String username, String password) {
        if (username.equals("admin") && password.equals("password")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}