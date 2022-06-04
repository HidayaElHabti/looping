package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ResultActivity extends AppCompatActivity {
    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    String mostVotedOnId, mostVotedOnName;
    TextView result;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.roundResult);

        gameID = ((looping) getApplication()).getGameID();
        Log.d("WTF", "onCreate: " + gameID);
        //Referencing to that game in the database
        gameRef = db.collection("games").document(gameID);

        gameRef.collection("questions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    documentSnapshot.getReference().delete();
                }
            }
        });

        if(((looping) getApplication()).getIsHost())
            mostVotedOn();

        next();
    }

    private void mostVotedOn() {
        gameRef.collection("players").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                long maxVotes = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    long votes = 0;
                    try {
                        votes = documentSnapshot.getLong("votes");
                    } catch (Exception e){}

                    if(votes>maxVotes){
                        maxVotes = votes;
                        mostVotedOnId = documentSnapshot.getId();
                        mostVotedOnName = documentSnapshot.getString("username");
                    }
                }
                Log.d("WTF", "onSuccess: "+mostVotedOnName);
                gameRef.update("votesCalculated", true);
            }
        });
    }

    private void next() {
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String imposterID = documentSnapshot.getString("imposter");
                if(mostVotedOnId.equals(imposterID)){
                    ((looping) getApplication()).playersIDs.clear();
                    ((looping) getApplication()).playersNames.clear();
                    result.setText("Simple players won.");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ResultActivity.this, ReplayActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },5000);

                } else {
                    int currentRound = ((looping) getApplication()).getCurrentRound();
                    int nbRounds = ((looping) getApplication()).getNbRounds();
                    if(currentRound<nbRounds){
                        ((looping) getApplication()).setCurrentRound(currentRound + 1);
                        gameRef.update("currentRound", currentRound + 1);


                        ((looping) getApplication()).playersNames.remove(mostVotedOnName);
                        ((looping) getApplication()).playersIDs.remove(mostVotedOnId);
                        int nbPlayers;
                        nbPlayers = ((looping) getApplication()).getNbPlayers();
                        nbPlayers--;
                        ((looping) getApplication()).setNbPlayers(nbPlayers);
                        gameRef.update("nbPlayers",nbPlayers);

                        for(String playerID : ((looping) getApplication()).playersIDs){
                            gameRef.collection("players")
                                    .document(playerID).update("votes", 0);
                        }

                        result.setText("You have voted on " + mostVotedOnName + ".\n" +
                                "It's not the imposter.\n" +
                                "New round will start soon.");
                        gameRef.update("votesCalculated", false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ResultActivity.this, "New round starts now.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResultActivity.this, DiscussionActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },5000);
                    } else {
                        ((looping) getApplication()).playersIDs.clear();
                        ((looping) getApplication()).playersNames.clear();
                        result.setText("Imposter won!");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ResultActivity.this, ReplayActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },5000);
                    }
                }
            }
        });
    }
}