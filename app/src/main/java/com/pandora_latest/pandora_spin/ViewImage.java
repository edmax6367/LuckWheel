package com.pandora_latest.pandora_spin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ViewImage extends AppCompatActivity {

    String imageLink = "";
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        imageView = findViewById(R.id.viewer);

        Intent i = getIntent();
        imageLink = i.getStringExtra("image");
        Glide.with(getApplicationContext()).load(imageLink).into(imageView);
    }
}