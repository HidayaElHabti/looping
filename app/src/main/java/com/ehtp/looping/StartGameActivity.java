package com.ehtp.looping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StartGameActivity extends AppCompatActivity {

    String gameID = "";
    TextView text_view_gameID;
    TextView text_view_players;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        text_view_gameID = findViewById(R.id.text_view_gameID);
        text_view_players = findViewById(R.id.text_view_players);

        //Getting the game ID as a message from the previous activity
        Intent intent = getIntent();
        gameID = intent.getStringExtra(PlayActivity.GAME_ID);
        Log.d("log", "gameID : "+gameID);
        text_view_gameID.setText(gameID);

        //Referencing to that game in the database
        gameRef = db.collection("games").document(gameID);
        
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
        gameRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("log", e.toString());
                    return;
                }

                if (documentSnapshot.exists()) {
                    Object players = documentSnapshot.get("players");
                    Log.d("log", "onEvent: "+documentSnapshot.get("players"));
                    //List<String> players = (List<String>) documentSnapshot.get("players");
                    if(players!=null)
                        text_view_players.setText(players.toString());

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
}