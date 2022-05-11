package com.ehtp.looping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StartGameActivity extends AppCompatActivity {

    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    TextView textView, text_view_gameID, text_view_players;
    Button button_share_game_id, button_launch_game;
    LinearLayout card, background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        textView = findViewById(R.id.textViewShare);
        text_view_gameID = findViewById(R.id.text_view_gameID);
        text_view_players = findViewById(R.id.text_view_players);
        button_share_game_id = findViewById(R.id.button_share_game_id);
        button_launch_game = findViewById(R.id.button_launch_game);
        card = findViewById(R.id.card_start);
        background = findViewById(R.id.start_bg);

        text_view_players.setMovementMethod(new ScrollingMovementMethod());

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

        text_view_gameID.setAlpha(0);
        text_view_gameID.animate().alpha(1).setDuration(1500).setStartDelay(3000).start();//1800

        button_share_game_id.setTranslationY(300);
        button_share_game_id.setAlpha(0);
        button_share_game_id.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1800).start();//2300

        button_launch_game.setTranslationY(300);
        button_launch_game.setAlpha(0);
        button_launch_game.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(2300).start();//2800

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

        //Redirecting to a test activity for now
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("gameID", gameID);
        startActivity(intent);
    }
}