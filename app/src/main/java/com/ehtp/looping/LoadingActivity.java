package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoadingActivity extends AppCompatActivity {

    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    int nbPlayers=0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        textView = findViewById(R.id.textView);
        //Getting the game ID
        Intent intent = getIntent();
        gameID = ((looping) getApplication()).getGameID();

        gameRef = db.collection("games").document(gameID);
        Log.d("12 loading", "onCreate: "+((looping) getApplication()).playersNames.toString());
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
                    if(documentSnapshot.get("status").equals("launched")){
                                //Redirecting to the test activity
                                Intent intent = new Intent(LoadingActivity.this,ImageActivity.class);
                                startActivity(intent);
                                finish();
                    }
                } else {
                    textView.setText("Room not found!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoadingActivity.this, JoinGameActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },2000);
                }
            }
        });

    }
}