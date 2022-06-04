package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageActivity extends AppCompatActivity {
    Button button_show_or_hide;
    ImageView imageViewInformation;
    TextView textViewTimer;
    long duration;
    String imposterID;
    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef;
    int nbPlayers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        gameID = ((looping) getApplication()).getGameID();
        //Referencing to that game in the database
        gameRef = db.collection("games").document(gameID);

        button_show_or_hide = findViewById(R.id.show_or_hide);
        imageViewInformation = findViewById(R.id.imageViewInformation);
        imageViewInformation.setVisibility(View.INVISIBLE);
        textViewTimer = findViewById(R.id.imageTimer);

        ((looping) getApplication()).setCurrentRound(1);
        //((looping) getApplication()).setNewRound(true);

        isImposter();

        timer(5);


    }


    private void isImposter() {
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot!=null) {
                    imposterID = documentSnapshot.getString("imposter");
                    //change player status
                    if (imposterID!=null && imposterID.equals(((looping) getApplication()).getPlayerID())) {
                        //if the player was chosen to be the imposter
                        ((looping) getApplication()).setIsImposter(true);
                    } else {
                        //else it's a simple player
                        ((looping) getApplication()).setIsImposter(false);
                    }
                }
            }
        });

    }

    public void timer(int s){
        duration = TimeUnit.SECONDS.toMillis(s);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.getDefault(),
                        "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textViewTimer.setText(sDuration);
            }
            @Override
            public void onFinish() {
                Toast.makeText(ImageActivity.this, "Discussion starts now.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ImageActivity.this, DiscussionActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public void showOrHide(View view) {
        if(button_show_or_hide.getText().toString().equals("Show")){
            button_show_or_hide.setText("Hide");
            imageViewInformation.setVisibility(View.VISIBLE);
        } else {
            button_show_or_hide.setText("Show");
            imageViewInformation.setVisibility(View.INVISIBLE);
        }
    }
}