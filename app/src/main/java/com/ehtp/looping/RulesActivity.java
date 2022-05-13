package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        LinearLayout background = findViewById(R.id.regle_bg);
        TextView textView1 = findViewById(R.id.Regle1);
        TextView textView11 = findViewById(R.id.Regle11);
        TextView textView12 = findViewById(R.id.Regle12);
        TextView textView2= findViewById(R.id.Regle2);
        TextView textView21= findViewById(R.id.Regle21);
        TextView textView22= findViewById(R.id.Regle22);
        TextView textView3 = findViewById(R.id.Regle3);
        TextView textView31 = findViewById(R.id.Regle31);
        TextView textView32 = findViewById(R.id.Regle32);
        TextView textView4 = findViewById(R.id.Regle4);
        TextView textView41 = findViewById(R.id.Regle41);

        AnimationDrawable animationDrawable= (AnimationDrawable) background.getBackground();
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        textView1.setTranslationY(200);
        textView1.setAlpha(0);
        textView1.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1000).start();

        textView11.setTranslationY(200);
        textView11.setAlpha(0);
        textView11.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1000).start();textView1.setTranslationY(300);

        textView12.setTranslationY(200);
        textView12.setAlpha(0);
        textView12.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1000).start();

        textView2.setTranslationY(200);
        textView2.setAlpha(0);
        textView2.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(2000).start();

        textView21.setTranslationY(200);
        textView21.setAlpha(0);
        textView21.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(2000).start();

        textView22.setTranslationY(200);
        textView22.setAlpha(0);
        textView22.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(2000).start();

        textView3.setTranslationY(200);
        textView3.setAlpha(0);
        textView3.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(3000).start();

        textView3.setTranslationY(200);
        textView31.setAlpha(0);
        textView31.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(3000).start();

        textView3.setTranslationY(200);
        textView32.setAlpha(0);
        textView32.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(3000).start();

        textView4.setTranslationY(200);
        textView4.setAlpha(0);
        textView4.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(4000).start();

        textView4.setTranslationY(200);
        textView41.setAlpha(0);
        textView41.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(4000).start();


    }
}