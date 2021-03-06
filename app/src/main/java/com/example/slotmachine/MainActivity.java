package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.slotmachine.WheelImageView.IEventEnd;
import com.example.slotmachine.WheelImageView.WheelImageView;

import java.util.Random;

public class  MainActivity extends AppCompatActivity implements IEventEnd {



    private RelativeLayout layout;
    private Button colorButton,musicBtn;

    private Button depositBtn;
    private Button logout;

    private long backPressedTime;
    private Toast backToast;

    ImageView btn_up, btn_down, plusBtn, minusBtn;
    WheelImageView image, image2,image3;
    TextView txt_score, input;
    DatabaseHelper slotmachineDB;
    int count_done = 0;

    Animation animation;

    MediaPlayer coin;
    MediaPlayer small;
    MediaPlayer big;

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


        coin = MediaPlayer.create(this, R.raw.coin);
        small = MediaPlayer.create(this, R.raw.small);
        big = MediaPlayer.create(this, R.raw.big);

        depositBtn = (Button) findViewById(R.id.depositBtn);

        txt_score = (TextView) findViewById(R.id.txt_score);
        input =  findViewById(R.id.input);
        input.setText(R.string._50);
        //ok_button.setOnClickListener(this);

        logout = (Button) findViewById(R.id.logout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        colorButton = findViewById(R.id.colorButton);
        layout = findViewById(R.id.bg_color);
        musicBtn = findViewById(R.id.musicBtn);
        if(mServ!=null ){
            mServ.resumeMusic();
        }

        doBindService();
        final Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);


        slotmachineDB = new DatabaseHelper(this);

        btn_down = (ImageView)findViewById(R.id.btn_down);
        btn_up = (ImageView)findViewById(R.id.btn_up);

        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);


        image = (WheelImageView) findViewById(R.id.image);
        image2 = (WheelImageView) findViewById(R.id.image2);
        image3 = (WheelImageView) findViewById(R.id.image3);


        txt_score = (TextView ) findViewById(R.id.txt_score);
        txt_score.setText(String.valueOf(Common.SCORE));


        image.setEventEnd(MainActivity.this);
        image2.setEventEnd(MainActivity.this);
        image3.setEventEnd(MainActivity.this);

        colorButton.setOnClickListener(new View.OnClickListener() {
            Drawable backgroud = colorButton.getBackground();

            @Override
            public void onClick(View view) {
                if (colorButton.getText().equals("Dark Mode")) {
                    layout.setBackgroundResource(R.drawable.background);
                    colorButton.setText("Light Mode");

                } else if (colorButton.getText().equals("Light Mode")) {
                    colorButton.setText("Dark Mode");
                    layout.setBackgroundResource(R.drawable.background_sun);
                }
            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBtn.getText().toString().equals("Music off")){
                    musicBtn.setText("Music on");
                    mServ.pauseMusic();
                } else {
                    musicBtn.setText("Music off");
                    mServ.resumeMusic();
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText(Integer.toString(Integer.parseInt(input.getText().toString())+50));
            }
        });
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(input.getText().toString())>50){
                    input.setText(Integer.toString(Integer.parseInt(input.getText().toString())-50));
                } else{
                    Toast.makeText(MainActivity.this, "Minimum bet", Toast.LENGTH_SHORT).show();
                }
            }
        });




        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.SCORE >= Integer.parseInt(input.getText().toString())) {
                    btn_up.setVisibility(View.GONE);
                    btn_down.setVisibility(View.VISIBLE);

                    image.setValueRandom(new Random().nextInt(6), new Random().nextInt((15 - 5) + 1) + 5);
                    image2.setValueRandom(new Random().nextInt(6), new Random().nextInt((15 - 5) + 1) + 5);
                    image3.setValueRandom(new Random().nextInt(6), new Random().nextInt((15 - 5) + 1) + 5);

                    Common.SCORE -= Integer.parseInt(input.getText().toString());
                    slotmachineDB.updateCoins(Common.SCORE, Common.playingUser);
                    txt_score.setText(String.valueOf(Common.SCORE));
                } else {
                    Toast.makeText(MainActivity.this, "Not enough Money", Toast.LENGTH_SHORT).show();
                }
            }
        });

        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DepositActivity.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MainActivity.this.finish();
                mServ.pauseMusic();


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

            //Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.mixed_anim);

            if (image.getValue()== image2.getValue() && image2.getValue() == image3.getValue()){
               
                if (image.getValue()==1){
                    Toast.makeText(this, "JACKPOT!!!! You won "+ (Integer.parseInt(input.getText().toString())*777)+ " Coins", Toast.LENGTH_SHORT).show();
                    Common.SCORE += Integer.parseInt(input.getText().toString())*777;
                } else {
                    int winAmount = 0;
                    switch (image.getValue()) {
                        case 0:
                            winAmount = Integer.parseInt(input.getText().toString())*20;
                            break;
                        case 2:
                            winAmount = Integer.parseInt(input.getText().toString())*40;
                            break;
                        case 3:
                            winAmount = Integer.parseInt(input.getText().toString())*60;
                            break;
                        case 4:
                            winAmount = Integer.parseInt(input.getText().toString())*80;
                            break;
                        case 5:
                            winAmount = Integer.parseInt(input.getText().toString())*100;
                            break;
                    }
                    Toast.makeText(this, "Congratulations! You won "+ winAmount+ " Coins", Toast.LENGTH_SHORT).show();
                    Common.SCORE += winAmount;
                }


                slotmachineDB.updateCoins(Common.SCORE, Common.playingUser);

                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                image.startAnimation(animation);

                txt_score.setText(String.valueOf(Common.SCORE));
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in);
                image.startAnimation(animation);
                image2.startAnimation(animation);
                image3.startAnimation(animation);
                small.start();

            }else if (image.getValue() == image2.getValue() || image2.getValue() == image3.getValue() || image.getValue() == image3.getValue()){
                int winAmount = 0;
                int imageValue = 0;
                if (image.getValue() == image2.getValue() ||image.getValue() == image3.getValue()){
                    imageValue = image.getValue();
                }else imageValue = image2.getValue();

                switch (imageValue){
                    case 0:
                        winAmount = Integer.parseInt(input.getText().toString())*2;
                        break;
                    case 1:
                        winAmount = Integer.parseInt(input.getText().toString())*10;
                        break;
                    case 2:
                        winAmount = Integer.parseInt(input.getText().toString())*3;
                        break;
                    case 3:
                        winAmount = Integer.parseInt(input.getText().toString())*5;
                        break;
                    case 4:
                        winAmount = Integer.parseInt(input.getText().toString())*7;
                        break;
                    case 5:
                        winAmount = Integer.parseInt(input.getText().toString())*8;
                        break;
                }
                        Toast.makeText(this, "You won "+ winAmount+ " Coins", Toast.LENGTH_SHORT).show();
                Common.SCORE += winAmount;
                slotmachineDB.updateCoins(Common.SCORE, Common.playingUser);
                txt_score.setText(String.valueOf(Common.SCORE));
                animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
                image.startAnimation(animation);
                image2.startAnimation(animation);
                image3.startAnimation(animation);
                big.start();
            }else{
                Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
/*
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast= Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }*/
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        mServ.onDestroy();
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();
        mServ.startMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void doBindService() {
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }


}