package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Getting the game ID
        Intent intent = getIntent();
        gameID = intent.getStringExtra("gameID");

        gameRef = db.collection("games").document(gameID);
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
                        Intent intent = new Intent(LoadingActivity.this,TestActivity.class);
                        intent.putExtra("gameID", gameID);
                        startActivity(intent);
                    }
                }
            }
        });

    }
}