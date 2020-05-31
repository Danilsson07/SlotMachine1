package com.example.slotmachine;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slotmachine.WheelImageView.IEventEnd;
import com.example.slotmachine.WheelImageView.WheelImageView;

import org.w3c.dom.Text;

import java.util.Random;

public class  MainActivity extends AppCompatActivity implements IEventEnd {

    ImageView btn_up, btn_down;
    WheelImageView image, image2,image3;
    TextView txt_score;

    int count_done = 0;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       btn_down = (ImageView)findViewById(R.id.btn_down);
       btn_up = (ImageView)findViewById(R.id.btn_up);

       image = (WheelImageView) findViewById(R.id.image);
       image2 = (WheelImageView) findViewById(R.id.image2);
       image3 = (WheelImageView) findViewById(R.id.image3);

       txt_score = (TextView ) findViewById(R.id.txt_score);


       image.setEventEnd(MainActivity.this);
       image3.setEventEnd(MainActivity.this);
       image3.setEventEnd(MainActivity.this);

       btn_up.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (Common.SCORE >= 50) {
                   btn_up.setVisibility(View.GONE);
                   btn_down.setVisibility(View.VISIBLE);

                   image.setValueRandom(new Random().nextInt(6), new Random().nextInt(15 - 5) + 1);
                   image2.setValueRandom(new Random().nextInt(6), new Random().nextInt(15 - 5) + 1);
                   image3.setValueRandom(new Random().nextInt(6), new Random().nextInt(15 - 5) + 1);

                   Common.SCORE -= 50;
                   txt_score.setText(String.valueOf(Common.SCORE));
               } else {
                   Toast.makeText(MainActivity.this, "Not enough Money", Toast.LENGTH_SHORT).show();
               }
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
}
