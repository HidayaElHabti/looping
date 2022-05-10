package com.ehtp.looping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
    TextView text_view_gameID;
    TextView text_view_players;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    LinearLayout colorBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        colorBg = findViewById(R.id.color_background);
        colorBg.setMinimumHeight(height/2);
        AnimationDrawable animationDrawable= (AnimationDrawable) colorBg.getBackground();
        animationDrawable.setEnterFadeDuration(20);
        animationDrawable.setExitFadeDuration(20);
        animationDrawable.start();
        LinearLayout ll=findViewById(R.id.card_start);
        ll.setTranslationY(300);
        ll.setAlpha(0);
        ll.animate().translationY(0).alpha(1).setDuration(5000).setStartDelay(400).start();

        colorBg = findViewById(R.id.color_background);
        colorBg.setMinimumHeight(height/3);

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