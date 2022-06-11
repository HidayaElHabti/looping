package com.ehtp.looping;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayActivity extends AppCompatActivity {

    //getting the database reference
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference gameRef;
    EditText edit_text_username;
    String gameID;
    String playerID;
    TextView textView;
    Button button_start_game, button_join_game;
    ProgressBar progres ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        edit_text_username = findViewById(R.id.edit_text_username);
        textView = findViewById(R.id.textViewEnter);
        button_start_game = findViewById(R.id.button_start_game);
        button_join_game = findViewById(R.id.button_join_game);
        progres = findViewById(R.id.progressBar);
        progres.setVisibility(View.INVISIBLE);

    }

    public void startGame(View v) {
        //Getting the username entered
        String username = String.valueOf(edit_text_username.getText());
        if (!username.equals("")) {
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
            new FetchImage().start();
            newGameRef.set(game);
            ((looping) getApplication()).setGameID(gameID);

            //Adding the host to a sub collection of the generated game document
            DocumentReference playerRef = newGameRef.collection("players").document();
            playerID = playerRef.getId();
            playerRef.set(host);
            ((looping) getApplication()).setPlayerID(playerID);
            //Redirecting to a new activity with a message containing the game ID

        } else {
            edit_text_username.setError("Please enter your name.");
            edit_text_username.requestFocus();
        }
    }

    public void joinNewGame(View view) {
        //Getting the username entered
        String username = String.valueOf(edit_text_username.getText());
        if (!username.equals("")) {
            ((looping) getApplication()).setPlayerName(username);


            Intent intent = new Intent(this, JoinGameActivity.class);
            startActivity(intent);
            finish();
        } else {
            edit_text_username.setError("Please enter your name.");
            edit_text_username.requestFocus();
        }
    }

    class FetchImage extends Thread {
        Bitmap bitmap;

        @Override
        public void run() {
            System.out.println("test");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progres.setVisibility(View.VISIBLE);
                    progres.bringToFront();
                }
            });

            OkHttpClient client = new OkHttpClient();
            String url = "http://40.123.248.207:200/api/game";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("gameid", gameID.trim())
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    System.out.println("url not added");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progres.setVisibility(View.INVISIBLE);
                            }
                        });
                        System.out.println("url added");
                        Intent intent = new Intent(PlayActivity.this, StartGameActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}