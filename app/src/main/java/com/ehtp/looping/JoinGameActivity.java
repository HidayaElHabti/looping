package com.ehtp.looping;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class JoinGameActivity extends AppCompatActivity {

    EditText edit_text_gameID;
    String playerName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    String playerID;
    TextView textView;
    Button button_join_game;
    int nbPlayers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        ((looping) getApplication()).setIsHost(false);

        edit_text_gameID = findViewById(R.id.edit_text_gameID);
        textView = findViewById(R.id.textViewJoin);
        button_join_game = findViewById(R.id.button_join_game);

        /*textView.setTranslationY(300);
        textView.setAlpha(0);
        textView.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(1200).start();

        button_join_game.setTranslationY(300);
        button_join_game.setAlpha(0);
        button_join_game.animate().translationY(0).alpha(1).setDuration(1500).setStartDelay(2700).start();*/

        //Getting the player username entered previously
        playerName = ((looping) getApplication()).getPlayerName();
    }

    public void joinGame(View view){
        //Getting the game ID
        String gameID = "";
        gameID = edit_text_gameID.getText().toString().trim();
        ((looping) getApplication()).setGameID(gameID);
        //Creating a simple player with the username entered
        Map<String, Object> player = new HashMap<>();
        player.put("username", playerName);
        player.put("isHost", false);

        //Referencing the game by the ID entered
        gameRef = db.collection("games").document(gameID);
        //Adding the player to the sub collection of players
        //Adding the host to a sub collection of the generated game document
        DocumentReference playerRef = gameRef.collection("players").document();
        playerID = playerRef.getId();
        playerRef.set(player);
        ((looping) getApplication()).setPlayerID(playerID);
        ((looping) getApplication()).setIsHost(false);

        Intent intent = new Intent(JoinGameActivity.this, LoadingActivity.class);
        startActivity(intent);
        finish();
    }
}