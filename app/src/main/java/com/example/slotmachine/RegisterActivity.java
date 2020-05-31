package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Button goBackBtn, submitBtn;
    private EditText username, password, repeatPassword;
    DatabaseHelper slotmachineDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        goBackBtn = findViewById(R.id.goBackBtn);
        submitBtn = findViewById(R.id.submitRegistrationBtn);
        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        repeatPassword = findViewById(R.id.registerPasswordRepeat);
        slotmachineDB = new DatabaseHelper(this);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(repeatPassword.getText().toString())&& username.length()!=0){
                    String newUsername;
                    addUser(username.getText().toString(), password.getText().toString());
                } else{
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void addUser(String username, String password){
        boolean insertData = slotmachineDB.addData(username, password);
        if(insertData){
            Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
