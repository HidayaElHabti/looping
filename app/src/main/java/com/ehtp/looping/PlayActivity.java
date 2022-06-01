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
    String playerID;
    TextView textView;
    Button button_start_game, button_join_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        edit_text_username = findViewById(R.id.edit_text_username);
        textView = findViewById(R.id.textViewEnter);
        button_start_game = findViewById(R.id.button_start_game);
        button_join_game = findViewById(R.id.button_join_game);

    }

    public void startGame(View v){
        //Getting the username entered
        String username = String.valueOf(edit_text_username.getText());
        ((looping) getApplication()).setPlayerName(username);

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
        ((looping) getApplication()).setGameID(gameID);

        //Adding the host to a sub collection of the generated game document
        DocumentReference playerRef = newGameRef.collection("players").document();
        playerID = playerRef.getId();
        playerRef.set(host);
        ((looping) getApplication()).setPlayerID(playerID);

        //Redirecting to a new activity with a message containing the game ID
        Intent intent = new Intent(this, StartGameActivity.class);
        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void joinNewGame(View view){
        //Getting the username entered
        String username = String.valueOf(edit_text_username.getText());
        ((looping) getApplication()).setPlayerName(username);


        Intent intent = new Intent(this, JoinGameActivity.class);
        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}