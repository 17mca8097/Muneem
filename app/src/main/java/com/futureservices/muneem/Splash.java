package com.futureservices.muneem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private static  int SPLASH_TIMEOUT = 2000;
    //Hooks
    View first,second,third,fourth;
    TextView txtTile;
    ImageView imgTitle;

    //Animations
    Animation top,mid,bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Animations
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        mid = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);

        txtTile = findViewById(R.id.txtTitle);
        imgTitle = findViewById(R.id.imgTitle);

        first.setAnimation(top);
        second.setAnimation(top);
        third.setAnimation(top);
        fourth.setAnimation(top);

        txtTile.setAnimation(bottom);
        imgTitle.setAnimation(mid);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIMEOUT);
    }
}