package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button_play, button_how_to_play;
    LinearLayout card, background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card = findViewById(R.id.card_main);
        background = findViewById(R.id.main_bg);
        textView = findViewById(R.id.textViewWelcome);
        button_play = findViewById(R.id.button_play);
        button_how_to_play = findViewById(R.id.button_how_to_play);

        AnimationDrawable animationDrawable= (AnimationDrawable) background.getBackground();
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        card.setTranslationY(300);
        card.setAlpha(0);
        card.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1000).start();
        card.setTranslationZ(100);

        textView.setTranslationY(300);
        textView.setAlpha(0);
        textView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();

        button_play.setTranslationY(300);
        button_play.setAlpha(0);
        button_play.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(2800).start();

        button_how_to_play.setTranslationY(300);
        button_how_to_play.setAlpha(0);
        button_how_to_play.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(3300).start();
    }

    public void playGame(View v){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}