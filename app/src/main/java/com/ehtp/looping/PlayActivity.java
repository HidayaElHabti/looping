package com.ehtp.looping;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayActivity extends AppCompatActivity {

    //getting the database reference
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText edit_text_username;
    public static final String GAME_ID = "com.ehtp.looping.MESSAGE";
    public static final String PLAYER_NAME = "com.ehtp.looping.MESSAGE";
    String gameID;
    TextView textView;
    Button button_start_game, button_join_game;
    LinearLayout card, background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        edit_text_username = findViewById(R.id.edit_text_username);
        textView = findViewById(R.id.textViewEnter);
        button_start_game = findViewById(R.id.button_start_game);
        button_join_game = findViewById(R.id.button_join_game);
        card = findViewById(R.id.card_play);
        background = findViewById(R.id.play_bg);

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

        edit_text_username.setTranslationY(300);
        edit_text_username.setAlpha(0);
        edit_text_username.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1800).start();

        button_start_game.setTranslationY(300);
        button_start_game.setAlpha(0);
        button_start_game.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(2300).start();

        button_join_game.setTranslationY(300);
        button_join_game.setAlpha(0);
        button_join_game.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(2800).start();

    }

    public void startGame(View v){
        //Getting the username entered
        String username = String.valueOf(edit_text_username.getText());

        //Creating a host player with the username entered
        Map<String, Object> host = new HashMap<>();
        host.put("username", username);
        host.put("isHost", true);

        //Creating a new game
        Map<String, Object> game = new HashMap<>();
        game.put("status", "created");

        //Adding the game to the database and getting its ID
        DocumentReference newGameRef = db.collection("games").document();
        gameID = newGameRef.getId();
        newGameRef.set(game);

        //Adding the host to a sub collection of the generated game document
        newGameRef.collection("players").add(host);

        //Redirecting to a new activity with a message containing the game ID
        Intent intent = new Intent(this, StartGameActivity.class);
        Log.d("log", "Play Activity gameID : "+gameID);
        intent.putExtra(GAME_ID, gameID);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void joinGame(View view){
        Intent intent = new Intent(this, JoinGameActivity.class);
        intent.putExtra(PLAYER_NAME, edit_text_username.getText().toString());
        Log.d("log", "joinGame: "+edit_text_username.getText());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}