package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slotmachine.WheelImageView.IEventEnd;
import com.example.slotmachine.WheelImageView.WheelImageView;

import org.w3c.dom.Text;

import java.util.Random;

public class  MainActivity extends AppCompatActivity implements IEventEnd {

    private RelativeLayout layout;
    private Button colorButton;

    private EditText input;
    private Button ok_button;
    private Button logout;


    ImageView btn_up, btn_down;
    WheelImageView image, image2,image3;
    TextView txt_score;

    int count_done = 0;

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ok_button = (Button) findViewById(R.id.okButton);
        txt_score = (TextView) findViewById(R.id.txt_score);
        input = (EditText) findViewById(R.id.input);
        //ok_button.setOnClickListener(this);

        logout = (Button) findViewById(R.id.logout);
        colorButton = findViewById(R.id.colorButton);
        layout = findViewById(R.id.bg_color);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

        btn_down = (ImageView) findViewById(R.id.btn_down);
        btn_up = (ImageView) findViewById(R.id.btn_up);

        image = (WheelImageView) findViewById(R.id.image);
        image2 = (WheelImageView) findViewById(R.id.image2);
        image3 = (WheelImageView) findViewById(R.id.image3);

        txt_score = (TextView) findViewById(R.id.txt_score);


        image.setEventEnd(MainActivity.this);
        image2.setEventEnd(MainActivity.this);
        image3.setEventEnd(MainActivity.this);

        colorButton.setOnClickListener(new View.OnClickListener() {
            Drawable backgroud = colorButton.getBackground();

            @Override
            public void onClick(View view) {
                if (colorButton.getText().equals("Dark Mode")) {
                    colorButton.setText("Light Mode");
                    layout.setBackgroundResource(R.color.newColor);
                } else if (colorButton.getText().equals("Light Mode")) {
                    colorButton.setText("Dark Mode");
                    layout.setBackgroundResource(R.color.backgroud);
                }
            }
        });

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.SCORE >= 50) {
                    btn_up.setVisibility(View.GONE);
                    btn_down.setVisibility(View.VISIBLE);

                    image.setValueRandom(new Random().nextInt(6), new Random().nextInt((15 - 5) + 1) + 5);
                    image2.setValueRandom(new Random().nextInt(6), new Random().nextInt((15 - 5) + 1) + 5);
                    image3.setValueRandom(new Random().nextInt(6), new Random().nextInt((15 - 5) + 1) + 5);

                    Common.SCORE -= 50;
                    txt_score.setText(String.valueOf(Common.SCORE));
                } else {
                    Toast.makeText(MainActivity.this, "Not enough Money", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  int amount = Integer.parseInt(input.getText().toString());
                    Common.SCORE += amount;
                    txt_score.setText(String.valueOf(Common.SCORE));

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                mServ.onDestroy();

            }
        });

    }
    @Override
    public void eventEnd(int result, int count) {
        if(count_done < 2){
            count_done++;
        }else{
            btn_down.setVisibility(View.GONE);
            btn_up.setVisibility(View.VISIBLE);

            count_done= 0; // reset

            if (image.getValue()== image2.getValue() && image2.getValue() == image3.getValue()){
                Toast.makeText(this, "You win big prize", Toast.LENGTH_SHORT).show();
                Common.SCORE += 300;
                txt_score.setText(String.valueOf(Common.SCORE));
            }else if (image.getValue() == image2.getValue() || image2.getValue() == image3.getValue() || image.getValue() == image3.getValue()){
                Toast.makeText(this, "You win small prize", Toast.LENGTH_SHORT).show();
                Common.SCORE += 300;
                txt_score.setText(String.valueOf(Common.SCORE));
            }else{
                Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    private void doBindService() {
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

}