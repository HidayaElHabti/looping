package com.ehtp.looping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    TextView hintInfo;
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
        hintInfo = findViewById(R.id.hint);
        new ShowInfo().start();
        ((looping) getApplication()).setCurrentRound(1);
        //((looping) getApplication()).setNewRound(true);

        isImposter();

        timer(30);


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
            hintInfo.setVisibility(View.VISIBLE);
        } else {
            button_show_or_hide.setText("Show");
            imageViewInformation.setVisibility(View.INVISIBLE);
            hintInfo.setVisibility(View.INVISIBLE);
        }
    }
    class ShowInfo extends Thread{
        String url;
        String hint;

        public void ShowInfo(){
            url = ((looping) getApplication()).getImage();
            hint = ((looping) getApplication()).getHint();
        }

        @Override
        public void run(){
            System.out.println(((looping) getApplication()).getImage());
            String image = ((looping) getApplication()).getImage().replace('\\','/');
            String info = ((looping) getApplication()).getHint();
            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(((looping) getApplication()).getIsImposter())
                                        Picasso.get().load(image).into(imageViewInformation);
                                    else
                                        hintInfo.setText(info);
                                }
                            });

//            OkHttpClient client= new OkHttpClient();
//            String url = "http://172.16.219.196:5000/api/images/Images/1654455301547.jpg";
//            Request request = new Request.Builder()
//                    .url(url)
//                    .get()
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    System.out.println("not found!!!");
////                    Intent intent = new Intent(PlayActivity.this, PlayActivity.class);
////                    startActivity(intent);
//                }
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    if(response.isSuccessful())
//                    {
//                        System.out.println("url added");
//                        ResponseBody responseBody = response.body();
//                        if (responseBody != null) {
//                            InputStream instream = responseBody.byteStream();
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            int bufferSize = 1024;
//                            byte[] buffer = new byte[bufferSize];
//                            int len = 0;
//                            try {
//                                // instream is content got from httpentity.getContent()
//                                while ((len = instream.read(buffer)) != -1) {
//                                    baos.write(buffer, 0, len);
//                                }
//                                baos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            byte[] b = baos.toByteArray();
//                            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    imageViewInformation.setImageBitmap(bmp);
//                                }
//                            });
//                        }
//
//                    }
//                }
//            });
        }
    }
}