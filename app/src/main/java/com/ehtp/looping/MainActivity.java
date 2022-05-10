package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button_play;
    Button button_how_to_play;
    LinearLayout colorBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        colorBg = findViewById(R.id.color_background);
        colorBg.setMinimumHeight(height/2);

        textView = findViewById(R.id.textViewWelcome);
        button_play = findViewById(R.id.button_play);
        button_how_to_play = findViewById(R.id.button_how_to_play);

        AnimationDrawable animationDrawable= (AnimationDrawable) colorBg.getBackground();
        //animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
        LinearLayout ll=findViewById(R.id.card_main);
        ll.setTranslationY(300);
        ll.setAlpha(0);
        ll.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(400).start();
        textView.setTranslationY(300);
        textView.setAlpha(0);
        textView.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(400).start();
        button_play.setTranslationX(300);
        button_play.setAlpha(0);
        button_play.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(1000).start();
        button_how_to_play.setTranslationX(300);
        button_how_to_play.setAlpha(0);
        button_how_to_play.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(1000).start();

        colorBg = findViewById(R.id.color_background);
        colorBg.setMinimumHeight(height/2);
    }

    public void playGame(View v){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
}