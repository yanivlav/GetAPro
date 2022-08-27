package com.example.getapro.Common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getapro.Common.Auth.LoginActivity;
import com.example.getapro.OnBoarding;
import com.example.getapro.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 3000;

    //Variables
    ImageView backgroundImage;
    TextView slogan;

    Animation sideAnim, bottomAnim;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //Hooks
//        backgroundImage = findViewById(R.id.background_image);
        slogan =  findViewById(R.id.spetzSlogan);

        //Animation
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //Set animation
//        backgroundImage.setAnimation(sideAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                if (isFirstTime == true){
                    SharedPreferences.Editor editor  = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(SplashScreen.this,  OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashScreen.this,  LoginActivity.class);
//                    Intent intent = new Intent(SplashScreen.this,  OnBoarding.class);

                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_TIMER);
    }

}