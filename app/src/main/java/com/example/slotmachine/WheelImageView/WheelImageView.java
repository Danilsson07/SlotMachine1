package com.example.slotmachine.WheelImageView;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.slotmachine.R;

public class WheelImageView extends FrameLayout {

    private static int ANIMATION_DURATION = 150;
    ImageView current_image, next_image;

    int last_result = 0, old_value=0;

    IEventEnd eventEnd;

    public void setEventEnd(IEventEnd eventEnd) {
        this.eventEnd = eventEnd;
    }

    public WheelImageView(Context context) {
        super(context);
        init (context);
    }

    public WheelImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init (context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.wheel_image_view, this);
        current_image = (ImageView)getRootView().findViewById(R.id.current_image);
        next_image = (ImageView)getRootView().findViewById(R.id.next_image);

        next_image.setTranslationY(getHeight());
    }

    public void setValueRandom (final int image, final int rotate_count){
        current_image.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        next_image.setTranslationY(next_image.getHeight());
        next_image.animate().translationY(0).setDuration(ANIMATION_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setImage(current_image,old_value%5);
                current_image.setTranslationY(0);
                if (old_value != rotate_count){
                    setValueRandom(image, rotate_count);
                    old_value++;
                }else{
                    last_result = 0;
                    old_value = 0;
                    setImage(next_image,image);
                    System.out.println("ima"+image);
                    System.out.println("ro"+rotate_count);
                    eventEnd.eventEnd(image%6, rotate_count);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void setImage(ImageView imageView, int value) {
        if(value == Util.BAR){
            imageView.setImageResource(R.drawable.slot4); // ima
        }else if(value == Util.BELL){
            imageView.setImageResource(R.drawable.slot6); // ima
        }else if(value == Util.ORANGE){
            imageView.setImageResource(R.drawable.slot1); // ima
        }else if(value == Util.SEVEN){
            imageView.setImageResource(R.drawable.slot3); // ima
        }else if(value == Util.PLUM){
            imageView.setImageResource(R.drawable.slot4); // ima
        }else if (value == Util.CHERRY){
            imageView.setImageResource(R.drawable.slot2); // ima
        }

        imageView.setTag(value);
        last_result = value;
    }

    public int getValue (){
        return Integer.parseInt(next_image.getTag().toString());
    }
}
