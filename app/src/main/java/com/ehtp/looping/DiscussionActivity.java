package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DiscussionActivity extends AppCompatActivity {

    long duration;
    TextView textViewTimer;
    TextView roundNumber, person1, person2;
    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    ArrayList<Question> questions = new ArrayList<Question>();
    int nbPlayers, currentDiscussion;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        textViewTimer = findViewById(R.id.discussionTimer);
        roundNumber = findViewById(R.id.roundNumber);
        person1 = findViewById(R.id.person1);
        person2 = findViewById(R.id.person2);

        gameID = ((looping) getApplication()).getGameID();
        Log.d("WTF", "onCreate: " + gameID);
        gameRef = db.collection("games").document(gameID);


        roundNumber.setText(((looping) getApplication()).getCurrentRound()+"");
        //moves = new ArrayList<Map<String,String>>();

        if(((looping) getApplication()).getIsHost())
            setMoves();

        getQuestions();
    }

    private void setMoves() {
        //number of players
        nbPlayers = ((looping) getApplication()).getNbPlayers();

        //array of names
        ArrayList<String> playersToAsk = new ArrayList<String>(nbPlayers);
        //ArrayList<String> playerNames = (((looping) getApplication()).playersNames);
        for (String name :
                (((looping) getApplication()).playersNames)) {
            playersToAsk.add(name);
        }

        Random rand = new Random();
        int personToAsk;

        //moves = new ArrayList<Map<String, String>>();
        for(int i = 0; i< nbPlayers; i++){
            boolean wasThere = false;
            String currentPlayer = ((looping) getApplication()).playersNames.get(i);
            if(playersToAsk.contains(currentPlayer)){
                playersToAsk.remove(currentPlayer);
                wasThere = true;
            }
            personToAsk = rand.nextInt(playersToAsk.size());
            String playerToAsk = playersToAsk.get(personToAsk);
            Question question = new Question(currentPlayer, playerToAsk);
            DocumentReference questionRef = gameRef.collection("questions").document();
            questionRef.set(question);
            if(wasThere)
                playersToAsk.add(currentPlayer);
            playersToAsk.remove(playerToAsk);
        }
    }

    private void getQuestions() {
        gameRef.collection("questions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Question question = documentSnapshot.toObject(Question.class);
                    questions.add(question);
                }
                currentDiscussion = 0;
                person1.setText(questions.get(currentDiscussion).sender);
                person2.setText(questions.get(currentDiscussion).receiver);
                timer();
            }
        });

    }

    private void timer() {
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                textViewTimer.setText(timeLeftFormatted);
            }
            @Override
            public void onFinish() {
                currentDiscussion++;
                if(currentDiscussion<nbPlayers){
                    person1.setText(questions.get(currentDiscussion).sender);
                    person2.setText(questions.get(currentDiscussion).receiver);
                    countDownTimer.start();
                } else {
                    Intent intent = new Intent(DiscussionActivity.this, VoteActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }
}