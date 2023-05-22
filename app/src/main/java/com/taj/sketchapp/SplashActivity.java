package com.taj.sketchapp;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3200;
    private static final long CHECK_DELAY = 1500;
    ImageView check;
    ImageView circle;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    AnimatedVectorDrawableCompat avd3;
    AnimatedVectorDrawable avd4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        check = findViewById(R.id.check);
        circle = findViewById(R.id.circle);

        Drawable drawable2 = circle.getDrawable();

        if (drawable2 instanceof AnimatedVectorDrawableCompat) {
            avd3 = (AnimatedVectorDrawableCompat) drawable2;
            avd3.start();
        } else if (drawable2 instanceof AnimatedVectorDrawable) {
            avd4 = (AnimatedVectorDrawable) drawable2;
            avd4.start();
        }

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = check.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    avd = (AnimatedVectorDrawableCompat) drawable;
                    avd.start();
                } else if (drawable instanceof AnimatedVectorDrawable) {
                    avd2 = (AnimatedVectorDrawable) drawable;
                    avd2.start();
                }
            }
        }, CHECK_DELAY);
    }
}
