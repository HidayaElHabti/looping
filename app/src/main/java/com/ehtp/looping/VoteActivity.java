package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VoteActivity extends AppCompatActivity {
    ListView listView;
    int votePos;
    String playerVotedOn, playerVotedOnID;
    String gameID = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference gameRef, playerVotedOnRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        gameID = ((looping) getApplication()).getGameID();
        Log.d("WTF", "onCreate: " + gameID);
        //Referencing to that game in the database
        gameRef = db.collection("games").document(gameID);

        listView = findViewById(R.id.voteListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        initListViewData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                votePos = position;
            }
        });

    }

    private void initListViewData() {

        String[] playerNames = new String[((looping) getApplication()).playersNames.size()];
        ((looping) getApplication()).playersNames.toArray(playerNames);
        Log.d("12 vote", "initListViewData: "+((looping) getApplication()).playersNames.toString());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_single_choice, playerNames);
        listView.setAdapter(arrayAdapter);
    }

    public void vote(View view) {
        playerVotedOn = ((looping) getApplication()).playersNames.get(votePos);
        playerVotedOnID = ((looping) getApplication()).playersIDs.get(votePos);
        playerVotedOnRef = gameRef.collection("players").document(playerVotedOnID);
        playerVotedOnRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long votes = 0;
                try {
                    votes = documentSnapshot.getLong("votes");
                } catch (Exception e){ }
                playerVotedOnRef.update("votes", votes + 1);
            }
        });
        Intent intent = new Intent(VoteActivity.this, CalculatingVotesActivity.class);
        startActivity(intent);
        finish();
    }
}