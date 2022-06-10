package com.aseds.reviewapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import com.aseds.reviewapp.R;

public class SplashScreen extends AppCompatActivity {
ImageView backgroundImage;
TextView poweredByLine;
Animation sideAnim, bottomAnim;
SharedPreferences onBoardingScreen;

private static int SPLASH_TIMER=2500;//duration of animation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Above code to remove status bar
        setContentView(R.layout.splash_screen);
        //Hooks
        backgroundImage=findViewById(R.id.background_image);
        poweredByLine=findViewById(R.id.powered_by_line);
        //Animations
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        //set animations on elements
        backgroundImage.setAnimation(sideAnim);
        backgroundImage.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime=onBoardingScreen.getBoolean("isFirstTime",true);

                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                 SharedPreferences.Editor editor=onBoardingScreen.edit();
                 editor.putBoolean("isFirstTime",false);
                 editor.commit();
                    Intent intent=new Intent(SplashScreen.this, LoginActivity.class);


                    startActivity(intent);

                    finish();
                }
                else{
                    Intent intent=new Intent(SplashScreen.this, MainActivity.class);//We can also use getApplication as context of present activity
                    startActivity(intent);// finish() will destoyed this activity
                    //call the next activity
                    finish();
                }
            }
        },SPLASH_TIMER);
    }
}
