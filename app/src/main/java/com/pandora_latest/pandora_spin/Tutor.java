package com.pandora_latest.pandora_spin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tutor extends AppCompatActivity {
    FirebaseDatabase database;
    VideoView videoPlayer;
    View loader ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        database = FirebaseDatabase.getInstance();
        videoPlayer = findViewById(R.id.videoView);
        loader = findViewById(R.id.saver);

        DatabaseReference video = database.getReference().child("Tutorial");
        loader.setVisibility(View.VISIBLE);

        video.child("video").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String uri = snapshot.getValue().toString();
                    if(videoPlayer.isPlaying()){
                        videoPlayer.pause();
                        loader.setVisibility(View.VISIBLE);
                    }else{
                        videoPlayer.setVideoURI(Uri.parse(uri));
                        videoPlayer.start();
                        loader.setVisibility(View.GONE);

                        videoPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(videoPlayer.getDuration() > 0){
                                    if(videoPlayer.isPlaying()){
                                        videoPlayer.pause();
                                    }else{
                                        videoPlayer.start();
                                    }
                                }else{
                                    Toast.makeText(Tutor.this, "Video not ready please wait", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getSupportActionBar().setTitle("Depositing tutorial");
    }
}