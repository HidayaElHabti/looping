package com.ehtp.looping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JoinGameActivity extends AppCompatActivity {

    EditText edit_text_gameID;
    String playerName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        edit_text_gameID = findViewById(R.id.edit_text_gameID);

        //Getting the player username entered previously
        Intent intent = getIntent();
        playerName = intent.getStringExtra(PlayActivity.PLAYER_NAME);
        Log.d("log", "onCreate: "+playerName);
    }

    public void joinGame(View view){
        //Getting the game ID
        String gameID = "";
        gameID = edit_text_gameID.getText().toString();

        //Creating a simple player with the username entered
        Map<String, Object> player = new HashMap<>();
        player.put("username", playerName);
        player.put("isHost", false);

        //Referencing the game by the ID entered
        gameRef = db.collection("games").document(gameID);
        //Adding the player to the sub collection of players
        gameRef.collection("players").add(player);

        //Redirecting to the loading activity
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.putExtra("gameID", gameID);
        startActivity(intent);
    }
}