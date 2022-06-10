package com.ehtp.looping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StartGameActivity extends AppCompatActivity {

    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    TextView textView, text_view_gameID, text_view_players;
    Button button_share_game_id, button_launch_game;
    int nbPlayers=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        ((looping) getApplication()).setIsHost(true);

        textView = findViewById(R.id.textViewShare);
        text_view_gameID = findViewById(R.id.text_view_gameID);
        text_view_players = findViewById(R.id.text_view_players);
        button_share_game_id = findViewById(R.id.button_share_game_id);
        button_launch_game = findViewById(R.id.button_launch_game);

        text_view_players.setMovementMethod(new ScrollingMovementMethod());

        //Getting the game ID as a message from the previous activity
        gameID = ((looping) getApplication()).getGameID();

        text_view_gameID.setText(gameID);

        //Referencing to that game in the database
        gameRef = db.collection("games").document(gameID);

        //get image url for this game
        gameRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("log", e.toString());
                    return;
                }
                if (documentSnapshot.exists()) {
                    ((looping) getApplication()).setImage((String) documentSnapshot.get("image"));
                    ((looping) getApplication()).setImage((String) documentSnapshot.get("hint"));
                    System.out.println(documentSnapshot.get("image"));
                }
            }

        });
        
        //Changing the status of the game to loading
        gameRef.update("status", "loading")
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d("log", "Game status updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("log", "Error adding document", e);
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Adding an event listener on the game reference
        gameRef.collection("players").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("log", error.toString());
                    return;
                }
                text_view_players.setText("");
                for (DocumentSnapshot documentSnapshot : value) {
                    if(documentSnapshot.get("isHost").toString().equals("false"))
                        text_view_players.append(documentSnapshot.get("username").toString() + " joined...\n");
                }
            }
        });

    }

    public void shareGame(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, gameID);

        startActivity(Intent.createChooser(intent, "Share Game ID"));
    }

    public void launchGame(View view){

        //changing the game status to launched after the host clicks on launch game
        gameRef.update("status", "launched");

        //get all other players
        gameRef.collection("players").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    ((looping) getApplication()).playersIDs.add(documentSnapshot.getId());
                    ((looping) getApplication()).playersNames.add((String) documentSnapshot.get("username"));
                }
                nbPlayers = queryDocumentSnapshots.size();
//                if(nbPlayers<3 || nbPlayers>12){
//                    Toast.makeText(StartGameActivity.this,
//                            "You need more than 2 players and less than 13!", Toast.LENGTH_LONG).show();
//                    return;
//                }
                //add nbPlayers to database
                gameRef.update("nbPlayers", nbPlayers);
                ((looping) getApplication()).setNbPlayers(nbPlayers);

                //choose an imposter
                Random rand = new Random();
                int imposterPos = rand.nextInt(nbPlayers);

                //add player ID in imposter field of the game
                gameRef.update("imposter", ((looping) getApplication()).playersIDs.get(imposterPos));

                //initialize round
                ((looping) getApplication()).setNbRounds(nbPlayers/3 );
                gameRef.update("nbRounds", nbPlayers/3 );
                gameRef.update("currentRound", 1);
                //Redirecting to a test activity for now
                Intent intent = new Intent(StartGameActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
    }
}