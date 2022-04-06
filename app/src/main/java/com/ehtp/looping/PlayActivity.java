package com.ehtp.looping;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        edit_text_username = findViewById(R.id.edit_text_username);
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
    }

    public void joinGame(View view){
        Intent intent = new Intent(this, JoinGameActivity.class);
        intent.putExtra(PLAYER_NAME, edit_text_username.getText().toString());
        Log.d("log", "joinGame: "+edit_text_username.getText());
        startActivity(intent);
    }
}