package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DepositActivity extends AppCompatActivity {

    Button depositBtn, goBackBtn;
    EditText amount;
    DatabaseHelper slotmachineDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        depositBtn = findViewById(R.id.depositBtn2);
        goBackBtn = findViewById(R.id.goBackBtn2);
        amount = findViewById(R.id.amount);
        slotmachineDB = new DatabaseHelper(this);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositActivity.this.finish();
            }
        });
        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().length()>0){
                    Common.SCORE += Integer.parseInt(amount.getText().toString());
                    slotmachineDB.updateCoins(Common.SCORE, Common.playingUser);
                    amount.getText().clear();
                    Toast.makeText(DepositActivity.this, "Deposit successful", Toast.LENGTH_SHORT).show();


                } else Toast.makeText(DepositActivity.this, "Amount not valid", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
