package com.ehtp.looping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CalculatingVotesActivity extends AppCompatActivity {

    Handler handler = new Handler();
    String gameID = "";
    DocumentReference gameRef;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculating_votes);

        if(((looping) getApplication()).getIsHost()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(CalculatingVotesActivity.this, ResultActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 15000);
        } else {
            gameID = ((looping) getApplication()).getGameID();
            Log.d("WTF", "onCreate: " + gameID);
            //Referencing to that game in the database
            gameRef = db.collection("games").document(gameID);

            gameRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d("log", error.toString());
                        return;
                    }
                    Intent intent = new Intent(CalculatingVotesActivity.this, ResultActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}